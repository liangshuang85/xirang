package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础线索信息Req
 */
@Value
public class ClueReq implements BaseRestRequest {

    /**
     * 行政区代码
     */
    @NotNull
    Long adcode;

    /**
     * 线索负责人
     */
    @NotBlank
    String assigneeId;

    /**
     * 线索状态
     */
    @NotNull
    ClueStatusType status = ClueStatusType.CLUE_DISCOVERY;

    /**
     * 线索级别
     */
    ClueLevel level;

    /**
     * 线索渠道录入
     */
    @Valid
    @NotNull
    ChannelEntryReq clueChannelEntry;

    /**
     * 拜访信息
     */
    @Valid
    @NotNull
    List<VisitReq> clueVisits = new ArrayList<>();

    /**
     * 实例角色成员
     */
    @Valid
    @NotNull
    List<InstanceRoleLarkMemberReq> instanceRoleLarkMembers = new ArrayList<>();

    /**
     * 基础数据
     */
    @NotNull
    BasicDataReq basicData;

}
