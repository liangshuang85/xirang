package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.model.dto.req.InstanceRoleLarkMemberReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleLarkMemberRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.InstanceRoleLarkMember;
import eco.ywhc.xr.core.mapper.InstanceRoleLarkMemberMapper;
import eco.ywhc.xr.core.mapper.InstanceRoleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstanceRoleLarkMemberManagerImpl implements InstanceRoleLarkMemberManager {

    private final InstanceRoleMapper instanceRoleMapper;

    private final InstanceRoleLarkMemberMapper instanceRoleLarkMemberMapper;

    @Override
    public void insertInstanceRoleLarkMember(Collection<InstanceRoleLarkMemberReq> reqs, long refId, InstanceRefType type) {
        List<InstanceRoleLarkMember> instanceRoleLarkMembers = reqs.stream()
                .filter(req -> CollectionUtils.isNotEmpty(req.getMemberIds()))
                .flatMap(req -> req.getMemberIds().stream()
                        .map(memberId -> {
                            InstanceRoleLarkMember instanceRoleLarkMember = new InstanceRoleLarkMember();
                            instanceRoleLarkMember.setInstanceRoleId(req.getInstanceRoleId());
                            instanceRoleLarkMember.setMemberId(memberId);
                            instanceRoleLarkMember.setMemberType("");
                            instanceRoleLarkMember.setRefType(type);
                            instanceRoleLarkMember.setRefId(refId);
                            return instanceRoleLarkMember;
                        })
                )
                .toList();
        if (instanceRoleLarkMembers.isEmpty()) {
            return;
        }
        instanceRoleLarkMemberMapper.bulkInsert(instanceRoleLarkMembers);
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

    @Override
    public List<String> getMemberIdsByRefId(long refId) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getRefId, refId);
        return instanceRoleLarkMemberMapper.selectList(qw).stream()
                .map(InstanceRoleLarkMember::getMemberId)
                .toList();
    }

    @Override
    public List<String> getMemberIdsByInstanceRoleIdAndRefId(long instanceRoleId, long refId) {
        QueryWrapper<InstanceRoleLarkMember> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRoleLarkMember::getDeleted, 0)
                .eq(InstanceRoleLarkMember::getInstanceRoleId, instanceRoleId)
                .eq(InstanceRoleLarkMember::getRefId, refId);
        return instanceRoleLarkMemberMapper.selectList(qw).stream()
                .map(InstanceRoleLarkMember::getMemberId)
                .toList();
    }

    @Override
    public InstanceRole findInstanceRoleById(long id) {
        return instanceRoleMapper.findEntityById(id);
    }

}
