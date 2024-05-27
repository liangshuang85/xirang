package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 应用Req
 */
@Value
public class ApplicationReq implements BaseRestRequest {

    /**
     * 应用名称
     */
    @NotBlank
    @Size(max = 100)
    String name;

    /**
     * 描述
     */
    @Size(max = 200)
    String description;

    /**
     * 应用授权Scope列表
     */
    @NotNull
    Set<String> scopes = new HashSet<>();

}
