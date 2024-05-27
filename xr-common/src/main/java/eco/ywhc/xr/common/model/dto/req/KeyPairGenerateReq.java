package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 生成密钥对的请求
 */
@Value
public class KeyPairGenerateReq implements BaseRestRequest {

    /**
     * 算法
     */
    @NotNull
    KeypairGeneratorAlgorithm algorithm = KeypairGeneratorAlgorithm.RSA;

}

