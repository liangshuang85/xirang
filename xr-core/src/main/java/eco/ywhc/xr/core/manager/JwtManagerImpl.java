package eco.ywhc.xr.core.manager;

import com.nimbusds.jose.jwk.Curve;
import eco.ywhc.xr.common.model.PemKeyPair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtManagerImpl implements JwtManager {

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

}
