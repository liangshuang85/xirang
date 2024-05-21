package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.RoleMemberType;
import eco.ywhc.xr.common.model.dto.res.EmployeeRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.RoleLarkMember;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.RoleManager;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.RoleLarkMemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;
import org.sugar.crud.model.PageableModelSet;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final LarkEmployeeManager larkEmployeeManager;

    private final RoleLarkMemberMapper roleLarkMemberMapper;

    private final RoleManager roleManager;

    @Override
    public PageableModelSet<EmployeeRes> findMany() {
        Set<String> userIds = larkEmployeeManager.getAllLarkEmployeeUserIds();
        Map<String, List<RoleRes>> map = listRoles(userIds);
        List<EmployeeRes> employees = userIds.stream()
                .map(i -> {
                    LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i);
                    if (larkEmployee == null) {
                        return null;
                    }
                    return EmployeeRes.builder()
                            .larkOpenId(i)
                            .name(larkEmployee.getName())
                            .avatarInfo(larkEmployee.getAvatarInfo())
                            .roles(map.getOrDefault(i, Collections.emptyList()))
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
        return PageableModelSet.of(employees);
    }

    @Override
    public EmployeeRes findOne(String id) {
        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(id);
        if (larkEmployee == null) {
            throw new ResourceNotFoundException();
        }
        EmployeeRes res = EmployeeRes.builder()
                .larkOpenId(id)
                .name(larkEmployee.getName())
                .avatarInfo(larkEmployee.getAvatarInfo())
                .build();
        res.setRoles(listRoles(id));
        return res;
    }

    @Override
    public void configureRoles(String id, Set<Long> roleIds) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByEmployeeId(id);
        Set<Long> currentRoleIds = roleLarkMembers.stream().map(RoleLarkMember::getRoleId).collect(Collectors.toSet());
        // 待删除
        Collection<Long> pendingDelete = CollectionUtils.removeAll(currentRoleIds, roleIds);
        if (CollectionUtils.isNotEmpty(pendingDelete)) {
            UpdateWrapper<RoleLarkMember> uw = new UpdateWrapper<>();
            uw.lambda().eq(RoleLarkMember::getDeleted, 0)
                    .eq(RoleLarkMember::getMemberId, id)
                    .eq(RoleLarkMember::getMemberType, RoleMemberType.EMPLOYEE)
                    .in(RoleLarkMember::getRoleId, pendingDelete)
                    .set(RoleLarkMember::getDeleted, 1);
            roleLarkMemberMapper.update(uw);
        }
        // 待新增
        List<RoleLarkMember> pendingAdd = CollectionUtils.removeAll(roleIds, currentRoleIds).stream()
                .map(roleId -> RoleLarkMember.builder()
                        .roleId(roleId)
                        .memberId(id)
                        .memberType(RoleMemberType.EMPLOYEE)
                        .build())
                .toList();
        if (CollectionUtils.isNotEmpty(pendingAdd)) {
            roleLarkMemberMapper.bulkInsert(pendingAdd);
        }
    }

    @Override
    public List<RoleRes> listRoles(String id) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByEmployeeId(id);
        if (CollectionUtils.isEmpty(roleLarkMembers)) {
            return Collections.emptyList();
        }
        Set<Long> roleIds = roleLarkMembers.stream().map(RoleLarkMember::getRoleId).collect(Collectors.toSet());
        return roleManager.findAllByIds(roleIds);
    }

    private List<RoleLarkMember> listRoleLarkMembersByEmployeeId(String employeeId) {
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .eq(RoleLarkMember::getMemberId, employeeId)
                .eq(RoleLarkMember::getMemberType, RoleMemberType.EMPLOYEE)
                .orderByAsc(RoleLarkMember::getRoleId);
        return roleLarkMemberMapper.selectList(qw);
    }

    public Map<String, List<RoleRes>> listRoles(Collection<String> ids) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByEmployeeIds(ids);
        if (CollectionUtils.isEmpty(roleLarkMembers)) {
            return Collections.emptyMap();
        }
        Set<Long> roleIds = roleLarkMembers.stream().map(RoleLarkMember::getRoleId).collect(Collectors.toSet());
        Map<Long, RoleRes> roleMap = roleManager.findAllByIds(roleIds).stream()
                .collect(Collectors.toMap(RoleRes::getId, Function.identity()));
        return roleLarkMembers.stream()
                .collect(Collectors.groupingBy(
                        RoleLarkMember::getMemberId,
                        Collectors.mapping(i -> roleMap.get(i.getRoleId()), Collectors.toList())
                ));
    }

    private List<RoleLarkMember> listRoleLarkMembersByEmployeeIds(Collection<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .in(RoleLarkMember::getMemberId, ids)
                .eq(RoleLarkMember::getMemberType, RoleMemberType.EMPLOYEE)
                .orderByAsc(RoleLarkMember::getRoleId);
        return roleLarkMemberMapper.selectList(qw);
    }

}
