package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.StatusTree;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(rollbackFor = {Exception.class})
public interface StatusFlowService<T extends Enum<T>> {

    /**
     * 获取状态流转树
     *
     * @param statusMap 状态关系Map，Key为状态，Value为对应的下一个状态
     */
    List<StatusTree<T>> getStatusTree(Map<T, List<T>> statusMap);

}
