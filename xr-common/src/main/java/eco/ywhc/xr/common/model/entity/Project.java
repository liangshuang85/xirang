package eco.ywhc.xr.common.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.ProjectStatusType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 项目
 */
@Getter
@Setter
@TableName(value = "b_project")
public class Project extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 7796632702101907362L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目名
     */
    private String name;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目状态
     */
    private ProjectStatusType status;

    /**
     * 行政区划代码
     */
    private Long adcode;

    /**
     * 框架协议ID
     */
    private Long frameworkAgreementId;

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

}
