package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleLarkMemberRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.InstanceRoleLarkMember;
import eco.ywhc.xr.core.mapper.InstanceRoleLarkMemberMapper;
import eco.ywhc.xr.core.mapper.InstanceRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstanceRoleLarkMemberManagerImpl implements InstanceRoleLarkMemberManager {

    private final InstanceRoleMapper instanceRoleMapper;

    private final InstanceRoleLarkMemberMapper instanceRoleLarkMemberMapper;

    @Override
    public List<InstanceRole> findAllInstanceRoles() {
        QueryWrapper<InstanceRole> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRole::getEnabled, 1);
        return instanceRoleMapper.selectList(qw);
    }

    @Override
    public void insertInstanceRoleLarkMember(Object req, long refId) {
        if (req instanceof ClueReq clueReq) {
            List<InstanceRoleLarkMember> instanceRoleLarkMembers = clueReq.getInstanceRoleLarkMembers().stream()
                    .flatMap(instanceRoleLarkMemberReq -> instanceRoleLarkMemberReq.getMemberIds().stream()
                            .map(memberId -> {
                                InstanceRoleLarkMember instanceRoleLarkMember = new InstanceRoleLarkMember();
                                instanceRoleLarkMember.setInstanceRoleId(instanceRoleLarkMemberReq.getInstanceRoleId());
                                instanceRoleLarkMember.setMemberId(memberId);
                                instanceRoleLarkMember.setMemberType("");
                                instanceRoleLarkMember.setRefType(InstanceRefType.CLUE);
                                instanceRoleLarkMember.setRefId(refId);
                                return instanceRoleLarkMember;
                            }))
                    .toList();
            instanceRoleLarkMemberMapper.bulkInsert(instanceRoleLarkMembers);
        } else if (req instanceof FrameworkAgreementReq frameworkAgreementReq) {
            List<InstanceRoleLarkMember> frameworkAgreementInstanceRoleLarkMembers = frameworkAgreementReq.getInstanceRoleLarkMembers().stream()
                    .flatMap(instanceRoleLarkMemberReq -> instanceRoleLarkMemberReq.getMemberIds().stream()
                            .map(memberId -> {
                                InstanceRoleLarkMember instanceRoleLarkMember = new InstanceRoleLarkMember();
                                instanceRoleLarkMember.setInstanceRoleId(instanceRoleLarkMemberReq.getInstanceRoleId());
                                instanceRoleLarkMember.setMemberId(memberId);
                                instanceRoleLarkMember.setMemberType("");
                                instanceRoleLarkMember.setRefType(InstanceRefType.FRAMEWORK_AGREEMENT);
                                instanceRoleLarkMember.setRefId(refId);
                                return instanceRoleLarkMember;
                            }))
                    .toList();
            instanceRoleLarkMemberMapper.bulkInsert(frameworkAgreementInstanceRoleLarkMembers);
        } else if (req instanceof ProjectReq projectReq) {
            List<InstanceRoleLarkMember> projectInstanceRoleLarkMembers = projectReq.getInstanceRoleLarkMembers().stream()
                    .flatMap(instanceRoleLarkMemberReq -> instanceRoleLarkMemberReq.getMemberIds().stream()
                            .map(memberId -> {
                                InstanceRoleLarkMember instanceRoleLarkMember = new InstanceRoleLarkMember();
                                instanceRoleLarkMember.setInstanceRoleId(instanceRoleLarkMemberReq.getInstanceRoleId());
                                instanceRoleLarkMember.setMemberId(memberId);
                                instanceRoleLarkMember.setMemberType("");
                                instanceRoleLarkMember.setRefType(InstanceRefType.PROJECT);
                                instanceRoleLarkMember.setRefId(refId);
                                return instanceRoleLarkMember;
                            }))
                    .toList();
            instanceRoleLarkMemberMapper.bulkInsert(projectInstanceRoleLarkMembers);
        }
    }

    @Override
    public void deleteInstanceRoleLarkMember(long refId) {
        UpdateWrapper<InstanceRoleLarkMember> uw = new UpdateWrapper<>();
        uw.lambda().eq(BaseEntity::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefId, refId)
                .set(InstanceRoleLarkMember::getDeleted, 1);
        instanceRoleLarkMemberMapper.update(uw);
    }

    @Override
    public List<InstanceRoleLarkMemberRes> findInstanceRoleLarkMemberByRefId(long refId) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefId, refId);
        List<InstanceRoleLarkMember> instanceRoleLarkMembers = instanceRoleLarkMemberMapper.selectList(qw);
        Map<Long, List<InstanceRoleLarkMember>> groupByInstanceRoleId = instanceRoleLarkMembers.stream()
                .collect(Collectors.groupingBy(InstanceRoleLarkMember::getInstanceRoleId));

        return groupByInstanceRoleId.values().stream()
                .map(larkMembers -> {
                    InstanceRoleLarkMemberRes instanceRoleLarkMemberRes = new InstanceRoleLarkMemberRes();
                    instanceRoleLarkMemberRes.setId(larkMembers.get(0).getId());
                    instanceRoleLarkMemberRes.setInstanceRoleId(larkMembers.get(0).getInstanceRoleId());
                    instanceRoleLarkMemberRes.setMemberIds(larkMembers.stream().map(InstanceRoleLarkMember::getMemberId).collect(Collectors.toSet()));
                    return instanceRoleLarkMemberRes;
                })
                .toList();
    }

}
