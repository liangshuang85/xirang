package eco.ywhc.xr.common.jackson;

/**
 * {@code @JsonView}所使用的View
 */
public class Views {

    /**
     * 最少字段视图
     */
    public interface MinimalView {

    }

    /**
     * 完全字段视图
     * <p>
     * 包含最少字段视图的字段
     */
    public interface FullView extends MinimalView {

    }

    /**
     * 创建Request视图
     */
    public interface CreationView {

    }

    /**
     * 更新Request视图
     */
    public interface UpdateView {

    }

    /**
     * 列表页视图
     */
    public interface ListView {

    }

    /**
     * 详情页视图
     * <p>
     * 包含列表页视图的字段
     */
    public interface DetailView extends ListView {

    }

    public interface Case1View {

    }

    public interface Case2View {

    }

    public interface Case3View {

    }

    public interface Case4View {

    }

    /**
     * 管理员视图
     */
    public interface AdminView {

    }

    /**
     * 公共视图
     */
    public interface PublicView {

    }

    /**
     * 个人视图
     */
    public interface SelfView {

    }

}
