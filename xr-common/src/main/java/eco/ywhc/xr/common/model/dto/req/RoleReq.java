package eco.ywhc.xr.common.model.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 角色Request
 */
@Getter
@Setter
@ToString
public class RoleReq implements BaseRestRequest {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

}

