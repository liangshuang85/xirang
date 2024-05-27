package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.RoleConverter;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import eco.ywhc.xr.common.model.entity.RoleLarkMember;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.lark.LarkDepartmentManager;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.RoleLarkMemberMapper;
import eco.ywhc.xr.core.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleManagerImpl implements RoleManager {

    private final LarkEmployeeManager larkEmployeeManager;

    private final LarkDepartmentManager larkDepartmentManager;

    private final RoleConverter roleConverter;

    private final RoleMapper roleMapper;

    private final RoleLarkMemberMapper roleLarkMemberMapper;

    private final PermissionAssignmentManager permissionAssignmentManager;

    @Override
    public List<Role> findAllEntitiesByIds(Collection<Long> ids) {
        return roleMapper.findAllByIds(ids);
    }

    @Override
    public List<RoleRes> findAllByIds(Collection<Long> ids) {
        return findAllEntitiesByIds(ids).stream().map(roleConverter::toResponse).toList();
    }

    @Override
    public Role findEntityById(long id) {
        return roleMapper.findEntityById(id);
    }

    @Override
    public Role mustFindEntityById(long id) {
        Role role = findEntityById(id);
        return Optional.ofNullable(role).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public boolean isRoleAssigned(long id) {
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .eq(RoleLarkMember::getRoleId, id);
        return roleLarkMemberMapper.exists(qw);
    }

    @Override
    public Set<String> findCurrentPermissionCodes(long id) {
        return permissionAssignmentManager.listAllPermissionCodesBySubjectId(id);
    }

    @Override
    public void grantPermissions(long id, Collection<String> permissionCodes) {
        permissionAssignmentManager.grantPermissions(id, permissionCodes);
    }

    @Override
    public void revokePermissions(long id, Collection<String> permissionCodes) {
        permissionAssignmentManager.revokePermissions(id, permissionCodes);
    }

    @Override
    public Set<Long> listAssignedRoleIds(String larkUserOpenId) {
        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployeeSync(larkUserOpenId);
        Set<String> departmentIds = Set.of(larkEmployee.getDepartmentIds());
        Set<String> memberIds = new HashSet<>(departmentIds);
        memberIds.add(larkUserOpenId);

        Set<Long> roleIds = new HashSet<>();
        List<RoleLarkMember> roleLarkMembers = findAllRoleLarkMembersByMemberIds(memberIds);
        for (RoleLarkMember roleLarkMember : roleLarkMembers) {
            switch (roleLarkMember.getMemberType()) {
                case EMPLOYEE -> roleIds.add(roleLarkMember.getRoleId());
                case DEPARTMENT_STAFF -> roleIds.add(roleLarkMember.getRoleId());
                case DEPARTMENT_LEADER -> {
                    var department = larkDepartmentManager.getLarkDepartmentByDepartmentId(roleLarkMember.getMemberId());
                    if (larkUserOpenId.equals(department.getLeaderUserId())) {
                        roleIds.add(roleLarkMember.getRoleId());
                    }
                }
            }
        }
        return roleIds;
    }

    @Override
    public Set<String> listBasicPermissionCodes() {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.lambda().eq(Role::getDeleted, 0).eq(Role::getBasic, 1);
        List<Long> basicRoleIds = roleMapper.selectList(qw).stream().map(Role::getId).toList();
        return listGrantedPermissionCodes(basicRoleIds);
    }

    @Override
    public Set<String> listGrantedPermissionCodes(Collection<Long> ids) {
        return permissionAssignmentManager.listAllPermissionCodesBySubjectIds(ids);
    }

    private List<RoleLarkMember> findAllRoleLarkMembersByMemberIds(Collection<String> memberIds) {
        if (CollectionUtils.isEmpty(memberIds)) {
            return Collections.emptyList();
        }
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .in(RoleLarkMember::getMemberId, memberIds);
        return roleLarkMemberMapper.selectList(qw);
    }

}
