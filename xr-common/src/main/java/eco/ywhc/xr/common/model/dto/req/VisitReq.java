package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * 拜访信息Req
 */
@Value
public class VisitReq implements BaseRestRequest {

    /**
     * 拜访关联ID
     */
    Long refId;

    /**
     * 是否为正式拜访
     */
    @NotNull
    Boolean official = false;

    /**
     * 拜访日期
     */
    @NotNull
    LocalDate visitDate;

    /**
     * 邀请函附件ID
     */
    @NotNull
    Set<Long> invitationLetterAttachmentIds = new HashSet<>();

    /**
     * 拜访记录附件ID
     */
    @NotNull
    Set<Long> visitRecordAttachmentIds = new HashSet<>();

}
