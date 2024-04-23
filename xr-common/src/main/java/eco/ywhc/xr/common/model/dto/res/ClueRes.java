package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.util.ArrayList;
import java.util.List;

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
     * 行政区代码
     */
    private Long adcode;

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
    private String assigneeId;

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
     * 审批信息Res
     */
    private List<ApprovalRes> clueApprovals = new ArrayList<>();

}
