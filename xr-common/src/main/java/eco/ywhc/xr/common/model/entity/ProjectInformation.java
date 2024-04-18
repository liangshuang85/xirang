package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 项目信息
 */
@Getter
@Setter
@TableName(value = "b_project_information")
public class ProjectInformation extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 3212992192299105023L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 建设规模
     */
    private Double constructionScale;

    /**
     * 风能资源
     */
    private String windResource;

    /**
     * 光伏资源
     */
    private String solarResource;

    /**
     * 土地资源
     */
    private String landResource;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 拟建地点
     */
    private String proposedLocation;

    /**
     * 建设规模及内容
     */
    private String constructionScaleAndContent;

    /**
     * 总用地
     */
    private Double totalLand;

    /**
     * 项目总投资
     */
    private Double totalInvestment;

    /**
     * 资金来源
     */
    private String fundingSource;

    /**
     * 项目建设周期开始时间
     */
    private LocalDateTime projectStartTime;

    /**
     * 项目建设周期结束时间
     */
    private LocalDateTime projectEndTime;

}
