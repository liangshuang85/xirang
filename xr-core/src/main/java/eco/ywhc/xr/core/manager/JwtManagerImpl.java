package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.SignedJWT;
import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import eco.ywhc.xr.common.converter.UserConverter;
import eco.ywhc.xr.common.model.PemKeyPair;
import eco.ywhc.xr.common.model.entity.Application;
import eco.ywhc.xr.common.model.entity.User;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.common.security.SecurityContext;
import eco.ywhc.xr.common.security.SecurityContextHolder;
import eco.ywhc.xr.core.mapper.ApplicationMapper;
import eco.ywhc.xr.core.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtManagerImpl implements JwtManager {

    public static final String AGENT_USERNAME = "agent";

    private final ApplicationManager applicationManager;

    private final ApplicationMapper applicationMapper;

    private final UserConverter userConverter;

    private final UserMapper userMapper;

    @Override
    public KeyPair generateKeyPair(String algorithm) {
        KeyPairGenerator generator;
        try {
            switch (algorithm) {
                case "RSA" -> {
                    generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(2048);
                }
                case "EC" -> {
                    generator = KeyPairGenerator.getInstance("EC");
                    generator.initialize(Curve.P_256.toECParameterSpec());
                }
                default -> throw new InternalErrorException("不支持此算法: " + algorithm);
            }
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            log.error("生成密钥对失败：{}", e.getMessage());
            throw new InternalErrorException("生成密钥对失败");
        }
        return generator.generateKeyPair();
    }

    @Override
    public PemKeyPair generatePemKeyPair(String algorithm) {
        return generatePemKeyPair(algorithm, true);
    }

    @Override
    public PemKeyPair generatePemKeyPair(String algorithm, boolean opensslCompatible) {
        final KeyPair keyPair = generateKeyPair(algorithm);
        switch (algorithm) {
            case "RSA" -> {
                final String publicKey = writeAsPemObject(keyPair.getPublic());
                final String privateKey = writeAsPemObject(keyPair.getPrivate());
                return PemKeyPair.builder().publicKey(publicKey).privateKey(privateKey).build();
            }
            case "EC" -> {
                try {
                    final PemObject publicKeyPemObject = new PemObject("PUBLIC KEY", keyPair.getPublic().getEncoded());
                    final String publicKey = writeAsPemObject(publicKeyPemObject);
                    String privateKey;
                    if (opensslCompatible) {
                        // Ref: https://stackoverflow.com/a/61685115/2484595
                        final PrivateKeyInfo original = PrivateKeyInfo.getInstance(keyPair.getPrivate().getEncoded());
                        final ECPrivateKey originalPrivateKey = ECPrivateKey.getInstance(original.parsePrivateKey());
                        final ECPrivateKey newPrivateKey = new ECPrivateKey(256, originalPrivateKey.getKey(), originalPrivateKey.getPublicKey(), null);
                        final PrivateKeyInfo newPrivateKeyInfo = new PrivateKeyInfo(original.getPrivateKeyAlgorithm(), newPrivateKey);
                        final PemObject privateKeyPemObject = new PemObject("PRIVATE KEY", newPrivateKeyInfo.getEncoded());
                        privateKey = writeAsPemObject(privateKeyPemObject);
                    } else {
                        privateKey = writeAsPemObject(keyPair.getPrivate());
                    }
                    return PemKeyPair.builder().publicKey(publicKey).privateKey(privateKey).build();
                } catch (IOException e) {
                    log.error("生成密钥对失败：{}", e.getMessage());
                    throw new InternalErrorException("生成密钥对失败");
                }
            }
            default -> throw new InternalErrorException("Unsupported algorithm: " + algorithm);
        }
    }

    @Override
    public boolean validateJWT(String jwtStr) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(jwtStr);
        } catch (java.text.ParseException e) {
            log.debug("JWT解析失败：{}", e.getMessage());
            return false;
        }

        final Map<String, Object> jsonObject = signedJWT.getPayload().toJSONObject();
        final Object sub = jsonObject.get("sub");
        if (sub == null) {
            log.debug("缺少sub Claim");
            return false;
        }

        final Application application = findApplicationBySid((String) sub);
        if (application == null || StringUtils.isBlank(application.getPublicKey())) {
            log.debug("未找到与sub[{}]对应的应用", sub);
            return false;
        }

        JWSVerifier verifier;
        final JWSAlgorithm jwtAlgorithm = signedJWT.getHeader().getAlgorithm();
        if (application.getAlgorithm() == KeypairGeneratorAlgorithm.EC && JWSAlgorithm.Family.EC.contains(jwtAlgorithm)) {
            try {
                final JWK jwk = JWK.parseFromPEMEncodedObjects(application.getPublicKey());
                verifier = new ECDSAVerifier(jwk.toECKey().toECPublicKey());
            } catch (JOSEException e) {
                log.debug("应用公钥解析失败或Verifier构造失败：{}", e.getMessage());
                return false;
            }
        } else if (application.getAlgorithm() == KeypairGeneratorAlgorithm.RSA && JWSAlgorithm.Family.RSA.contains(jwtAlgorithm)) {
            try {
                final JWK jwk = JWK.parseFromPEMEncodedObjects(application.getPublicKey());
                verifier = new RSASSAVerifier(jwk.toRSAKey().toRSAPublicKey());
            } catch (JOSEException e) {
                log.debug("应用公钥解析失败或Verifier构造失败：{}", e.getMessage());
                return false;
            }
        } else {
            log.debug("不支持该算法：{}", jwtAlgorithm);
            return false;
        }

        try {
            final boolean verified = signedJWT.verify(verifier);
            if (verified) {
                log.debug("JWT签名验证成功");
                final CurrentUser agentUser = findAgentUser();
                final Set<String> currentPermissionCodes = applicationManager.findCurrentPermissionCodes(application.getId());
                final CurrentUser currentUser = agentUser.toBuilder().permissionCodes(currentPermissionCodes).build();
                final SecurityContext securityContext = new SecurityContext();
                securityContext.setCurrentUser(currentUser);
                SecurityContextHolder.setContext(securityContext);
            }
            return verified;
        } catch (JOSEException e) {
            log.debug("JWT签名校验失败：{}", e.getMessage());
            return false;
        }
    }

    private static String writeAsPemObject(Object o) {
        StringWriter sw = new StringWriter();
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(sw)) {
            if (o != null) {
                pemWriter.writeObject(o);
            }
        } catch (IOException e) {
            throw new InternalErrorException(e);
        }
        return sw.toString();
    }

    private Application findApplicationBySid(String applicationSid) {
        QueryWrapper<Application> qw = new QueryWrapper<>();
        qw.lambda().eq(Application::getDeleted, 0)
                .eq(Application::getApplicationSid, applicationSid);
        return applicationMapper.selectOne(qw);
    }

    public CurrentUser findAgentUser() {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getDeleted, 0).eq(User::getUsername, AGENT_USERNAME);
        User user = userMapper.selectOne(qw);
        return userConverter.toCurrentUser(user);
    }

}
