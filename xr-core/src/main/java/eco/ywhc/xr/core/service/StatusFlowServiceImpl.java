package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.StatusTree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatusFlowServiceImpl<T extends Enum<T>> implements StatusFlowService<T> {

    @Override
    public List<StatusTree<T>> getStatusTree(Map<T, List<T>> statusMap) {
        List<T> roots = findRoots(statusMap);
        if (CollectionUtils.isEmpty(roots)) {
            return Collections.emptyList();
        } else {
            return roots.stream().map(i -> convertToTree(i, statusMap, new HashSet<>())).toList();
        }
    }

    /**
     * 获取根节
     */
    private <T1> List<T1> findRoots(Map<T1, List<T1>> statusMap) {
        List<T1> childList = statusMap.values().stream()
                .flatMap(List::stream)
                .toList();
        return statusMap.keySet().stream()
                .filter(key -> !childList.contains(key))
                .toList();
    }

    /**
     * 从根节点开始构建状态树
     *
     * @param root 根节点
     * @param <T2> 状态关系Map
     */
    private <T2> StatusTree<T2> convertToTree(T2 root, Map<T2, List<T2>> statusMap, Set<T2> visited) {
        StatusTree<T2> tree = new StatusTree<>();
        tree.setStatus(root);

        if (!visited.contains(root)) {
            visited.add(root);
            if (statusMap.containsKey(root)) {
                statusMap.get(root).forEach(child -> {
                    StatusTree<T2> childNode = convertToTree(child, statusMap, new HashSet<>(visited));
                    tree.getChildren().add(childNode);
                });
            }
        }
        return tree;
    }

}
