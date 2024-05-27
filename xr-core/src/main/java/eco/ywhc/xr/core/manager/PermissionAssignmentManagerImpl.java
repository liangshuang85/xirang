package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.model.entity.PermissionAssignment;
import eco.ywhc.xr.core.mapper.PermissionAssignmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionAssignmentManagerImpl implements PermissionAssignmentManager {

    private final PermissionAssignmentMapper permissionAssignmentMapper;

    @Override
    public void grantPermissions(long id, Collection<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        Set<String> currentPermissionCodes = listAllPermissionCodesBySubjectId(id);
        Set<String> pendingGrant = new HashSet<>(permissionCodes);
        pendingGrant.removeAll(currentPermissionCodes);
        if (pendingGrant.isEmpty()) {
            return;
        }
        List<PermissionAssignment> permissionAssignments = pendingGrant.stream()
                .map(i -> {
                    PermissionAssignment permissionAssignment = new PermissionAssignment();
                    permissionAssignment.setSubjectId(id);
                    permissionAssignment.setPermissionCode(i);
                    return permissionAssignment;
                })
                .toList();
        permissionAssignmentMapper.bulkInsert(permissionAssignments);
    }

    @Override
    public void revokePermissions(long id, Collection<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        UpdateWrapper<PermissionAssignment> uw = new UpdateWrapper<>();
        uw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .eq(PermissionAssignment::getSubjectId, id)
                .in(PermissionAssignment::getPermissionCode, permissionCodes)
                .set(PermissionAssignment::getDeleted, 1);
        permissionAssignmentMapper.update(uw);
    }

    @Override
    public Set<String> listAllPermissionCodesBySubjectId(long id) {
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .eq(PermissionAssignment::getSubjectId, id);
        return permissionAssignmentMapper.selectList(qw).stream()
                .map(PermissionAssignment::getPermissionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> listAllPermissionCodesBySubjectIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .in(PermissionAssignment::getSubjectId, ids);
        return permissionAssignmentMapper.selectList(qw).stream()
                .map(PermissionAssignment::getPermissionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Long> listAllSubjectIdsByPermissionCode(String permissionCode) {
        if (StringUtils.isBlank(permissionCode)) {
            return Collections.emptyList();
        }
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .eq(PermissionAssignment::getPermissionCode, permissionCode);
        return permissionAssignmentMapper.selectList(qw).stream()
                .map(PermissionAssignment::getSubjectId)
                .toList();
    }

}
