package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用Res
 */
@Data
public class ApplicationRes implements BaseRestResponse {

    /**
     * ID
     */
    private Long id;

    /**
     * 应用SID
     */
    private String applicationSid;

    /**
     * 应用名称
     */
    String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 是否生成过密钥对
     */
    private Boolean keyPairGenerated;

    /**
     * 密钥对生成算法
     */
    KeypairGeneratorAlgorithm algorithm;

    /**
     * 应用授权Scope列表
     */
    List<PermissionRes> scopes = new ArrayList<>();

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}
