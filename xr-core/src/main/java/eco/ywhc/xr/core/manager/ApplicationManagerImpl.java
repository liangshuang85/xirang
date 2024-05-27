package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.entity.Application;
import eco.ywhc.xr.core.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationManagerImpl implements ApplicationManager {

    private final ApplicationMapper applicationMapper;

    private final PermissionAssignmentManager permissionAssignmentManager;

    @Override
    public Application findEntityById(long id) {
        return applicationMapper.findEntityById(id);
    }

    @Override
    public Application mustFoundEntityById(long id) {
        return Optional.ofNullable(findEntityById(id)).orElseThrow(ResourceNotFoundException::new);
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

}
