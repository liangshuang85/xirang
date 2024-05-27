package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.PemKeyPair;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;

/**
 * JWT相关能力
 */
@Transactional(rollbackFor = {Exception.class})
public interface JwtManager {

    /**
     * 生成密钥对
     *
     * @param algorithm 算法的标准字符串名称
     */
    KeyPair generateKeyPair(String algorithm);

    /**
     * 生成PEM格式的密钥对
     *
     * @param algorithm 算法的标准字符串名称
     */
    PemKeyPair generatePemKeyPair(String algorithm);

    /**
     * 生成PEM格式的密钥对
     *
     * @param algorithm         算法的标准字符串名称
     * @param opensslCompatible 兼容OpenSSL
     */
    PemKeyPair generatePemKeyPair(String algorithm, boolean opensslCompatible);

}
