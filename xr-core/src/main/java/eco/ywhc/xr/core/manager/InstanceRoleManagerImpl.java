package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.InstanceRoleLarkMember;
import eco.ywhc.xr.common.model.entity.PermissionAssignment;
import eco.ywhc.xr.core.mapper.InstanceRoleLarkMemberMapper;
import eco.ywhc.xr.core.mapper.InstanceRoleMapper;
import eco.ywhc.xr.core.mapper.PermissionAssignmentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstanceRoleManagerImpl implements InstanceRoleManager {

    private final InstanceRoleMapper instanceRoleMapper;

    private final InstanceRoleLarkMemberMapper instanceRoleLarkMemberMapper;

    private final PermissionAssignmentMapper permissionAssignmentMapper;

    @Override
    public List<InstanceRole> findAllEntities() {
        QueryWrapper<InstanceRole> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRole::getDeleted, 0);
        return instanceRoleMapper.selectList(qw);
    }

    @Override
    public List<InstanceRole> findAllEntities(boolean onlyEnabled) {
        QueryWrapper<InstanceRole> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRole::getDeleted, 0)
                .eq(onlyEnabled, InstanceRole::getEnabled, 1);
        return instanceRoleMapper.selectList(qw);
    }

    @Override
    public InstanceRole findEntityById(long id) {
        return instanceRoleMapper.findEntityById(id);
    }

    @Override
    public InstanceRole mustFindEntityById(long id) {
        InstanceRole instanceRole = findEntityById(id);
        return Optional.ofNullable(instanceRole).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Set<String> listPermissionCodesById(long id) {
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .eq(PermissionAssignment::getRoleId, id);
        return permissionAssignmentMapper.selectList(qw).stream()
                .map(PermissionAssignment::getPermissionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isInstanceRoleAssigned(long id) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getInstanceRoleId, id);
        return instanceRoleLarkMemberMapper.exists(qw);
    }

    @Override
    public Set<String> listPermissionCodes(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptySet();
        }
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .in(PermissionAssignment::getRoleId, ids);
        return permissionAssignmentMapper.selectList(qw).stream()
                .map(PermissionAssignment::getPermissionCode)
                .collect(Collectors.toSet());
    }

    @Override
    public void grantPermissions(long id, Collection<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        Set<String> currentPermissionCodes = listPermissionCodesById(id);
        Set<String> pendingToGrant = new HashSet<>(permissionCodes);
        pendingToGrant.removeAll(currentPermissionCodes);
        if (pendingToGrant.isEmpty()) {
            return;
        }
        List<PermissionAssignment> permissionAssignments = pendingToGrant.stream()
                .map(i -> {
                    PermissionAssignment permissionAssignment = new PermissionAssignment();
                    permissionAssignment.setRoleId(id);
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
                .eq(PermissionAssignment::getRoleId, id)
                .in(PermissionAssignment::getPermissionCode, permissionCodes)
                .set(PermissionAssignment::getDeleted, 1);
        permissionAssignmentMapper.update(uw);
    }

    @Override
    public Set<Long> findAllInstanceRoleIdsByRefIdAndMemberId(long refId, String memberId) {
        QueryWrapper<InstanceRoleLarkMember> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefId, refId)
                .eq(InstanceRoleLarkMember::getMemberId, memberId);
        return instanceRoleLarkMemberMapper.selectList(qw1).stream()
                .map(InstanceRoleLarkMember::getInstanceRoleId)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<Long, Set<String>> listPermissionCodesByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        QueryWrapper<PermissionAssignment> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionAssignment::getDeleted, 0)
                .in(PermissionAssignment::getRoleId, ids);
        return permissionAssignmentMapper.selectList(qw).stream()
                .collect(Collectors.groupingBy(PermissionAssignment::getRoleId,
                        Collectors.mapping(PermissionAssignment::getPermissionCode, Collectors.toSet())));
    }

    @Override
    public Map<Long, Set<String>> listPermissionCodesByRefIdsAndMemberId(Collection<Long> refIds, String memberId) {
        if (CollectionUtils.isEmpty(refIds)) {
            return Collections.emptyMap();
        }
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .in(InstanceRoleLarkMember::getRefId, refIds)
                .eq(InstanceRoleLarkMember::getMemberId, memberId);
        Map<Long, List<Long>> instanceInstanceRoleIdMap = instanceRoleLarkMemberMapper.selectList(qw).stream()
                .collect(Collectors.groupingBy(InstanceRoleLarkMember::getRefId,
                        Collectors.mapping(InstanceRoleLarkMember::getInstanceRoleId, Collectors.toList())));
        if (instanceInstanceRoleIdMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, Set<String>> result = new HashMap<>();
        for (Map.Entry<Long, List<Long>> entry : instanceInstanceRoleIdMap.entrySet()) {
            result.put(entry.getKey(), listPermissionCodes(entry.getValue()));
        }
        return result;
    }

    @Override
    public Set<String> listPermissionCodesByRefIdAndMemberId(long refId, String memberId) {
        Set<Long> instanceRoleIds = findAllInstanceRoleIdsByRefIdAndMemberId(refId, memberId);
        return listPermissionCodes(instanceRoleIds);
    }

}
