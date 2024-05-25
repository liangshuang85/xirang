package eco.ywhc.xr.common.model.dto.req;

import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.time.LocalDateTime;

/**
 * 项目信息Req
 */
@Value
public class ProjectInformationReq implements BaseRestRequest {

    /**
     * 建设规模
     */
    Double constructionScale;

    /**
     * 风能资源
     */
    String windResource;

    /**
     * 光伏资源
     */
    String solarResource;

    /**
     * 土地资源
     */
    String landResource;

    /**
     * 项目ID
     */
    Long projectId;

    /**
     * 拟建地点
     */
    String proposedLocation;

    /**
     * 建设规模及内容
     */
    String constructionScaleAndContent;

    /**
     * 总用地
     */
    Double totalLand;

    /**
     * 项目总投资
     */
    Double totalInvestment;

    /**
     * 资金来源
     */
    String fundingSource;

    /**
     * 项目建设周期开始时间
     */
    LocalDateTime projectStartTime;

    /**
     * 项目建设周期结束时间
     */
    LocalDateTime projectEndTime;

}
