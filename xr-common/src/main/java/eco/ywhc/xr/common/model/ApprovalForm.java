package eco.ywhc.xr.common.model;

import lombok.Data;

import java.util.List;

/**
 * 审批定义控件信息
 */
@Data
public class ApprovalForm {

    /**
     * 控件ID
     */
    private String id;

    /**
     * 控件名称
     */
    private String name;

    /**
     * 控件类型
     */
    private String type;

    /**
     * 控件选项
     */
    private List<OptionItem> option;

    @Data
    public static class OptionItem {

        /**
         * 选项名
         */
        private String text;

        /**
         * 选项值
         */
        private String value;

    }

}
