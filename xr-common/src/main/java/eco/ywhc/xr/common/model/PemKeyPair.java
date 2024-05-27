package eco.ywhc.xr.common.model;

import lombok.Builder;
import lombok.Value;

/**
 * PEM格式的密钥对
 */
@Value
@Builder(toBuilder = true)
public class PemKeyPair {

    /**
     * 公钥
     */
    String publicKey;

    /**
     * 私钥
     */
    String privateKey;

}