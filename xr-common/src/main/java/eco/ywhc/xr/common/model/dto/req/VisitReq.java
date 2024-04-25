package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * 拜访信息Req
 */
@Data
public class VisitReq implements BaseRestRequest {

    /**
     * 线索id
     */
    private Long clueId;

    /**
     * 是否为正式拜访
     */
    @NotNull
    private Boolean official = false;

    /**
     * 拜访日期
     */
    @NotNull
    private LocalDate visitDate;

    /**
     * 邀请函附件ID
     */
    @NotNull
    private Set<Long> invitationLetterAttachmentIds = new HashSet<>();

    /**
     * 拜访记录附件ID
     */
    @NotNull
    private Set<Long> visitRecordAttachmentIds = new HashSet<>();

}
