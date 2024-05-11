package eco.ywhc.xr.common.model.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划Response
 */
@Getter
@Setter
@ToString
public class AdministrativeDivisionRes implements BaseRestResponse {

    /**
     * 行政区名称
     */
    private String name;

    /**
     * 6位数字格式的行政区划代码
     */
    private Long adcode;

    /**
     * 父级行政区划代码
     */
    private Long parent;

    /**
     * 行政区划级别：0为国家、1为省级、2为地市、3为区县
     */
    private Short level;

    /**
     * 下级行政区
     */
    private List<AdministrativeDivisionRes> children = new ArrayList<>();

    /**
     * 上级行政区
     */
    private List<AdministrativeDivisionRes> parents = new ArrayList<>();

    /**
     * 完整行政区名称
     */
    private String fullName;

}
