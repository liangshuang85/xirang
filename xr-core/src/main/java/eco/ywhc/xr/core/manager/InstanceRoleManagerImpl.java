package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.model.dto.req.InstanceRoleLarkMemberReq;
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

    private final PermissionAssignmentManager permissionAssignmentManager;

    @Override
    public void configureInstanceRoleMembers(InstanceRefType refType, long refId, List<InstanceRoleLarkMemberReq> reqs) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefType, refType)
                .eq(InstanceRoleLarkMember::getRefId, refId);
        List<InstanceRoleLarkMember> current = instanceRoleLarkMemberMapper.selectList(qw);
        // 待删除
        List<Long> pendingDelete = new ArrayList<>();
        for (InstanceRoleLarkMember item : current) {
            boolean found = reqs.stream().parallel()
                    .anyMatch(i -> i.getInstanceRoleId().equals(item.getInstanceRoleId()) && i.getMemberIds().contains(item.getMemberId()));
            if (!found) {
                pendingDelete.add(item.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(pendingDelete)) {
            UpdateWrapper<InstanceRoleLarkMember> uw = new UpdateWrapper<>();
            uw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                    .in(InstanceRoleLarkMember::getId, pendingDelete)
                    .set(InstanceRoleLarkMember::getDeleted, 1);
            instanceRoleLarkMemberMapper.update(uw);
        }

        // 待新增
        List<InstanceRoleLarkMember> pendingAdd = new ArrayList<>();
        for (InstanceRoleLarkMemberReq item : reqs) {
            for (String memberId : item.getMemberIds()) {
                boolean found = current.stream().parallel()
                        .anyMatch(i -> item.getInstanceRoleId().equals(i.getInstanceRoleId()) && memberId.equals(i.getMemberId()));
                if (!found) {
                    InstanceRoleLarkMember instanceRoleLarkMember = InstanceRoleLarkMember.builder()
                            .instanceRoleId(item.getInstanceRoleId())
                            .refId(refId)
                            .refType(refType)
                            .memberId(memberId)
                            .memberType("")
                            .build();
                    pendingAdd.add(instanceRoleLarkMember);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(pendingAdd)) {
            instanceRoleLarkMemberMapper.bulkInsert(pendingAdd);
        }
    }

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
        return permissionAssignmentManager.listAllPermissionCodesBySubjectId(id);
    }

    @Override
    public boolean isInstanceRoleAssigned(long id) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getInstanceRoleId, id);
        return instanceRoleLarkMemberMapper.exists(qw);
    }

    @Override
    public Set<String> listGrantedPermissionCodes(Collection<Long> ids) {
        return permissionAssignmentManager.listAllPermissionCodesBySubjectIds(ids);
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
            result.put(entry.getKey(), listGrantedPermissionCodes(entry.getValue()));
        }
        return result;
    }

    @Override
    public Set<String> listPermissionCodesByRefIdAndMemberId(long refId, String memberId) {
        Set<Long> instanceRoleIds = findAllInstanceRoleIdsByRefIdAndMemberId(refId, memberId);
        return listGrantedPermissionCodes(instanceRoleIds);
    }

    @Override
    public InstanceRole findInstanceRoleForAssignee(InstanceRefType refType) {
        QueryWrapper<InstanceRole> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRole::getDeleted, 0)
                .eq(InstanceRole::getRefType, refType)
                .eq(InstanceRole::getAssignee, 1);
        return instanceRoleMapper.selectOne(qw);
    }

    @Override
    public boolean isInstanceRoleAssigned(long instanceRoleId, long refId, String memberId) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getInstanceRoleId, instanceRoleId)
                .eq(InstanceRoleLarkMember::getRefId, refId)
                .eq(InstanceRoleLarkMember::getMemberId, memberId);
        return instanceRoleLarkMemberMapper.exists(qw);
    }

    @Override
    public void assignInstanceRole(long instanceRoleId, long refId, InstanceRefType refType, String memberId) {
        boolean assigned = isInstanceRoleAssigned(instanceRoleId, refId, memberId);
        if (assigned) {
            return;
        }
        InstanceRoleLarkMember instanceRoleLarkMember = InstanceRoleLarkMember.builder()
                .instanceRoleId(instanceRoleId)
                .refId(refId)
                .refType(refType)
                .memberId(memberId)
                .memberType("")
                .build();
        instanceRoleLarkMemberMapper.insert(instanceRoleLarkMember);
    }

    @Override
    public void assignInstanceRoleToAssignee(long refId, InstanceRefType refType, String memberId) {
        InstanceRole instanceRole = findInstanceRoleForAssignee(refType);
        if (instanceRole == null) {
            return;
        }
        assignInstanceRole(instanceRole.getId(), refId, refType, memberId);
    }

    @Override
    public void reAssignInstanceRoleToAssignee(long refId, InstanceRefType refType, String memberId) {
        InstanceRole instanceRole = findInstanceRoleForAssignee(refType);
        if (instanceRole == null) {
            return;
        }
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefId, refId)
                .eq(InstanceRoleLarkMember::getRefType, refType)
                .eq(InstanceRoleLarkMember::getInstanceRoleId, instanceRole.getId());
        InstanceRoleLarkMember instanceRoleLarkMember = instanceRoleLarkMemberMapper.selectOne(qw);
        if (instanceRoleLarkMember != null) {
            if (!instanceRoleLarkMember.getMemberId().equals(memberId)) {
                instanceRoleLarkMember.setMemberId(memberId);
                instanceRoleLarkMemberMapper.updateById(instanceRoleLarkMember);
            }
        } else {
            assignInstanceRole(instanceRole.getId(), refId, refType, memberId);
        }
    }

}
