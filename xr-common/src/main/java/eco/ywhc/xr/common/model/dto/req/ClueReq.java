package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础线索信息Req
 */
@Setter
@Getter
@ToString
public class ClueReq implements BaseRestRequest {

    /**
     * 行政区代码
     */
    @NotNull
    private Long adcode;

    /**
     * 线索负责人
     */
    @NotNull
    private String assigneeId;

    /**
     * 线索状态
     */
    @NotNull
    private ClueStatusType status;

    /**
     * 线索级别
     */
    @NotNull
    private ClueLevel level;

    /**
     * 项目收资信息
     */
    @Valid
    @NotNull
    private FundingReq clueFunding;

    /**
     * 线索渠道录入
     */
    @Valid
    @NotNull
    private ChannelEntryReq clueChannelEntry;

    /**
     * 拜访信息
     */
    @Valid
    @NotNull
    private List<VisitReq> clueVisits = new ArrayList<>();

}
