package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础线索信息Res
 */
@Data
public class ClueRes implements BaseRestResponse {

    /**
     * 线索ID
     */
    private Long id;

    /**
     * 行政区
     */
    private AdministrativeDivisionRes administrativeDivision;

    /**
     * 线索状态
     */
    private ClueStatusType status;

    /**
     * 线索级别
     */
    private ClueLevel level;

    /**
     * 线索编号
     */
    private String clueCode;

    /**
     * 负责人
     */
    private AssigneeRes assignee;

    /**
     * 线索渠道录入Res
     */
    private ChannelEntryRes clueChannelEntry;

    /**
     * 项目收资信息Res
     */
    private FundingRes clueFunding;

    /**
     * 拜访信息Res
     */
    private List<VisitRes> clueVisits = new ArrayList<>();

    /**
     * 线索关联的审批
     */
    private Map<ApprovalType, List<ApprovalRes>> approvalMap;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}
