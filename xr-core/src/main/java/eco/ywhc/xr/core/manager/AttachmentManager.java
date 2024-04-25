package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.model.UploadedFile;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.entity.Attachment;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 附件
 */
@Transactional(rollbackFor = {Exception.class})
public interface AttachmentManager {

    /**
     * 创建一个附件
     */
    Long createOne(Attachment attachment);

    /**
     * 更新一个附件
     */
    int updateOne(Attachment attachment);

    /**
     * 更新附件的属主类型和属主ID
     */
    void update(long id, FileOwnerType ownerType, long ownerId);

    /**
     * 更新列表中附件的属主类型和属主ID
     */
    void update(Collection<Long> ids, FileOwnerType ownerType, long ownerId);

    /**
     * 指定ID查找附件
     *
     * @param id 附件ID
     */
    Attachment findEntityById(long id);

    /**
     * 指定ID查找附件
     *
     * @param id 附件ID
     */
    AttachmentResponse findOne(long id);

    /**
     * 指定ID查找附件
     *
     * @param ids 附件ID列表
     */
    List<AttachmentResponse> findMany(Collection<Long> ids);

    /**
     * 指定属主ID列表查找每个属主的附件列表
     *
     * @param ownerIds 属主ID列表
     */
    Map<Long, List<AttachmentResponse>> findOneToManyByOwnerIds(Collection<Long> ownerIds);

    /**
     * 指定属主ID列表查找每个属主的唯一附件
     *
     * @param ownerIds 属主ID列表
     */
    Map<Long, AttachmentResponse> findOneToOneByOwnerIds(Collection<Long> ownerIds);

    /**
     * 指定属主ID查找其附件列表
     *
     * @param ownerId   属主ID
     * @param ownerType 文件属主类型
     */
    List<AttachmentResponse> findManyByOwnerId(long ownerId, @NonNull FileOwnerType ownerType);

    /**
     * 指定属主ID查找其唯一附件
     *
     * @param ownerId   属主ID
     * @param ownerType 文件属主类型
     */
    AttachmentResponse findOneByOwnerId(long ownerId, @NonNull FileOwnerType ownerType);

    /**
     * 通过Checksum查找附件
     *
     * @param checksum 附件校验和
     */
    Attachment findEntityByChecksum(String checksum);

    /**
     * 复制一个附件的信息，但是不包含id、ownerType和ownerId
     *
     * @param attachment 附件
     */
    Attachment copy(Attachment attachment);

    /**
     * 保存一个附件
     *
     * @param uploadedFile 上传的文件
     */
    Attachment save(UploadedFile uploadedFile);

    /**
     * 删除指定附件
     *
     * @param id 附件ID
     */
    void deleteById(long id);

    /**
     * 删除指定附件
     *
     * @param ids 附件ID列表
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 删除指定属主的全部附件
     *
     * @param ownerId 属主ID
     */
    void deleteByOwnerId(long ownerId);

    /**
     * 删除指定属主的全部附件
     *
     * @param ownerIds 属主ID列表
     */
    void deleteByOwnerIds(Collection<Long> ownerIds);

    /**
     * 检查指定附件是否存在
     *
     * @param id 附件ID
     */
    boolean contains(long id);

    /**
     * 检查ID列表中的附件是否都存在
     *
     * @param ids 附件ID列表
     */
    boolean containsAll(Collection<Long> ids);

}
