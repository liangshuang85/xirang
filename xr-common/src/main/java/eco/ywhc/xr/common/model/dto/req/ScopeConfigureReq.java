package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置授权Scope的请求
 */
@Value
public class ScopeConfigureReq implements BaseRestRequest {

    /**
     * 权限编码集合
     */
    @NotNull
    Set<String> scopes = new HashSet<>();

}

