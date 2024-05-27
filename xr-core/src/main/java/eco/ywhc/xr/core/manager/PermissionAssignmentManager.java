package eco.ywhc.xr.core.manager;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 权限分配
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionAssignmentManager {

    /**
     * 授予主体权限
     *
     * @param id              主体ID
     * @param permissionCodes 权限编码集合。此处不校验{@code permissionCodes}的合法性
     */
    void grantPermissions(long id, Collection<String> permissionCodes);

    /**
     * 撤销主体的授权
     *
     * @param id              主体ID
     * @param permissionCodes 权限编码集合
     */
    void revokePermissions(long id, Collection<String> permissionCodes);

    /**
     * 列出指定主体当前被授予的全部权限的编码
     *
     * @param id 主体ID
     */
    Set<String> listAllPermissionCodesBySubjectId(long id);

    /**
     * 列出列表中所有主体当前被授予的全部权限的编码
     *
     * @param ids 主体ID列表
     */
    Set<String> listAllPermissionCodesBySubjectIds(Collection<Long> ids);

    /**
     * 列出被授予了指定权限的全部主体的ID
     *
     * @param permissionCode 权限编码
     */
    List<Long> listAllSubjectIdsByPermissionCode(String permissionCode);

}
