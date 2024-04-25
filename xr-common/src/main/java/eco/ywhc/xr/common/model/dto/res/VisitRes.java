package eco.ywhc.xr.common.model.dto.res;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 拜访信息Res
 */
@Data
public class VisitRes {

    /**
     * 拜访信息ID
     */
    private Long id;

    /**
     * 是否为正式拜访
     */
    private Boolean official;

    /**
     * 拜访日期
     */
    private LocalDate visitDate;

    /**
     * 邀请函附件
     */
    @NotNull
    private List<AttachmentResponse> invitationLetterAttachments = new ArrayList<>();

    /**
     * 拜访记录附件
     */
    private List<AttachmentResponse> visitRecordAttachments = new ArrayList<>();

}
