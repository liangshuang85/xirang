package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.core.mapper.ProjectInformationMapper;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectManagerImpl implements ProjectManager {

    private final ProjectMapper projectMapper;

    private final ProjectInformationMapper projectInformationMapper;

    @Override
    public Project findEntityById(@NonNull Long id) {
        return projectMapper.findEntityById(id);
    }

    @Override
    public ProjectInformation getProjectInformationByProjectId(long id) {
        QueryWrapper<ProjectInformation> qw = new QueryWrapper<>();
        qw.lambda().eq(ProjectInformation::getDeleted, 0)
                .eq(ProjectInformation::getProjectId, id);
        return projectInformationMapper.selectOne(qw);
    }

}
