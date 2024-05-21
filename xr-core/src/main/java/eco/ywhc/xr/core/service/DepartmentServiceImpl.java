package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.RoleMemberType;
import eco.ywhc.xr.common.converter.LarkDepartmentConverter;
import eco.ywhc.xr.common.model.dto.res.LarkDepartmentRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.LarkDepartment;
import eco.ywhc.xr.common.model.entity.RoleLarkMember;
import eco.ywhc.xr.core.manager.RoleManager;
import eco.ywhc.xr.core.mapper.LarkDepartmentMapper;
import eco.ywhc.xr.core.mapper.RoleLarkMemberMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;
import org.sugar.crud.model.PageableModelSet;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final LarkDepartmentConverter larkDepartmentConverter;

    private final LarkDepartmentMapper larkDepartmentMapper;

    private final RoleLarkMemberMapper roleLarkMemberMapper;

    private final RoleManager roleManager;

    @Override
    public PageableModelSet<LarkDepartmentRes> findMany() {
        QueryWrapper<LarkDepartment> qw = new QueryWrapper<>();
        qw.lambda().orderByAsc(LarkDepartment::getDepartmentId);
        List<LarkDepartment> larkDepartments = larkDepartmentMapper.selectList(qw);
        if (CollectionUtils.isEmpty(larkDepartments)) {
            return PageableModelSet.create();
        }
        List<String> larkDepartmentIds = larkDepartments.stream().map(LarkDepartment::getDepartmentId).toList();
        Map<String, Map<RoleMemberType, List<RoleRes>>> map = listRoles(larkDepartmentIds);
        List<LarkDepartmentRes> result = larkDepartments.stream()
                .map(i -> {
                    var res = larkDepartmentConverter.toResponse(i);
                    res.setRoles(map.getOrDefault(res.getDepartmentId(), Collections.emptyMap()));
                    return res;
                })
                .toList();
        return PageableModelSet.of(result);
    }

    @Override
    public LarkDepartment findEntityById(String id) {
        return larkDepartmentMapper.selectById(id);
    }

    @Override
    public LarkDepartment mustFindEntityById(String id) {
        LarkDepartment larkDepartment = findEntityById(id);
        return Optional.ofNullable(larkDepartment).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public LarkDepartmentRes findOne(String id) {
        LarkDepartment larkDepartment = mustFindEntityById(id);
        LarkDepartmentRes res = larkDepartmentConverter.toResponse(larkDepartment);
        res.setRoles(listRoles(id));
        return res;
    }

    @Override
    public void configureRoles(String id, Map<RoleMemberType, Set<Long>> roles) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByDepartmentId(id);
        // 待删除
        List<Long> pendingDelete = new ArrayList<>();
        for (RoleLarkMember roleLarkMember : roleLarkMembers) {
            if (!roles.getOrDefault(roleLarkMember.getMemberType(), Collections.emptySet()).contains(roleLarkMember.getRoleId())) {
                pendingDelete.add(roleLarkMember.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(pendingDelete)) {
            UpdateWrapper<RoleLarkMember> uw = new UpdateWrapper<>();
            uw.lambda().eq(RoleLarkMember::getDeleted, 0)
                    .eq(RoleLarkMember::getId, id)
                    .in(RoleLarkMember::getMemberType, List.of(RoleMemberType.DEPARTMENT_STAFF, RoleMemberType.DEPARTMENT_LEADER))
                    .in(RoleLarkMember::getRoleId, pendingDelete)
                    .set(RoleLarkMember::getDeleted, 1);
            roleLarkMemberMapper.update(uw);
        }
        // 待新增
        List<RoleLarkMember> pendingAdd = new ArrayList<>();
        for (Map.Entry<RoleMemberType, Set<Long>> entry : roles.entrySet()) {
            for (long roleId : entry.getValue()) {
                boolean ok = roleLarkMembers.stream()
                        .anyMatch(i -> entry.getKey() == i.getMemberType() && roleId == i.getRoleId());
                if (ok) {
                    continue;
                }
                RoleLarkMember roleLarkMember = RoleLarkMember.builder()
                        .roleId(roleId)
                        .memberId(id)
                        .memberType(entry.getKey())
                        .build();
                pendingAdd.add(roleLarkMember);
            }
        }
        if (CollectionUtils.isNotEmpty(pendingAdd)) {
            roleLarkMemberMapper.bulkInsert(pendingAdd);
        }
    }

    @Override
    public Map<RoleMemberType, List<RoleRes>> listRoles(String id) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByDepartmentId(id);
        if (CollectionUtils.isEmpty(roleLarkMembers)) {
            return Collections.emptyMap();
        }
        Set<Long> roleIds = roleLarkMembers.stream().map(RoleLarkMember::getRoleId).collect(Collectors.toSet());
        Map<Long, RoleRes> roleMap = roleManager.findAllByIds(roleIds).stream()
                .collect(Collectors.toMap(RoleRes::getId, Function.identity()));
        return roleLarkMembers.stream()
                .collect(Collectors.groupingBy(
                        RoleLarkMember::getMemberType,
                        Collectors.mapping(i -> roleMap.get(i.getRoleId()), Collectors.toList())
                ));
    }

    public List<RoleLarkMember> listRoleLarkMembersByDepartmentId(String departmentId) {
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .eq(RoleLarkMember::getMemberId, departmentId)
                .orderByAsc(RoleLarkMember::getRoleId);
        return roleLarkMemberMapper.selectList(qw);
    }

    public Map<String, Map<RoleMemberType, List<RoleRes>>> listRoles(Collection<String> ids) {
        List<RoleLarkMember> roleLarkMembers = listRoleLarkMembersByDepartmentIds(ids);
        if (CollectionUtils.isEmpty(roleLarkMembers)) {
            return Collections.emptyMap();
        }

        Set<Long> roleIds = roleLarkMembers.stream().map(RoleLarkMember::getRoleId).collect(Collectors.toSet());
        Map<Long, RoleRes> roleMap = roleManager.findAllByIds(roleIds).stream()
                .collect(Collectors.toMap(RoleRes::getId, Function.identity()));

        Map<String, Map<RoleMemberType, List<RoleRes>>> result = new HashMap<>();
        for (String id : ids) {
            Map<RoleMemberType, List<RoleRes>> value = roleLarkMembers.stream()
                    .filter(i -> id.equals(i.getMemberId()))
                    .collect(Collectors.groupingBy(
                            RoleLarkMember::getMemberType,
                            Collectors.mapping(i -> roleMap.get(i.getRoleId()), Collectors.toList())
                    ));
            result.put(id, value);
        }
        return result;
    }

    private List<RoleLarkMember> listRoleLarkMembersByDepartmentIds(Collection<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        QueryWrapper<RoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(RoleLarkMember::getDeleted, 0)
                .in(RoleLarkMember::getMemberId, ids)
                .in(RoleLarkMember::getMemberType, List.of(RoleMemberType.DEPARTMENT_STAFF, RoleMemberType.DEPARTMENT_LEADER))
                .orderByAsc(RoleLarkMember::getRoleId);
        return roleLarkMemberMapper.selectList(qw);
    }

}
