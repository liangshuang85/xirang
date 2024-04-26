package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.AttachmentConverter;
import eco.ywhc.xr.common.model.UploadedFile;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.entity.Attachment;
import eco.ywhc.xr.core.mapper.AttachmentMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentManagerImpl implements AttachmentManager {

    private final AttachmentConverter attachmentConverter;

    private final AttachmentMapper attachmentMapper;

    @Override
    public Long createOne(Attachment attachment) {
        attachmentMapper.insert(attachment);
        return attachment.getId();
    }

    @Override
    public int updateOne(Attachment attachment) {
        if (attachment == null || attachment.getId() == null) {
            throw new InternalErrorException("更新附件失败");
        }
        return attachmentMapper.updateById(attachment);
    }

    @Override
    public void update(long id, @Nullable FileOwnerType ownerType, long ownerId) {
        Attachment attachment = findEntityById(id);
        if (attachment == null || attachment.getId() == null) {
            throw new InternalErrorException("更新附件失败");
        }
        attachment.setOwnerType(ownerType);
        attachment.setOwnerId(ownerId);
        updateOne(attachment);
    }

    @Override
    public void update(Collection<Long> ids, @Nullable FileOwnerType ownerType, long ownerId) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        ids.forEach(attachmentId -> update(attachmentId, ownerType, ownerId));
    }

    @Override
    public void compareAndUpdate(long ownerId, Collection<Long> newIds, FileOwnerType ownerType) {
        // 当前附件ID列表
        List<Long> currentIds = findManyEntitiesByOwnerId(ownerId, ownerType).stream().map(Attachment::getId).toList();
        // 待删除附件ID列表
        Collection<Long> pendingDeleteIds = CollectionUtils.removeAll(currentIds, newIds);
        if (CollectionUtils.isNotEmpty(pendingDeleteIds)) {
            deleteByIds(pendingDeleteIds);
        }
        // 待关联附件ID列表
        Collection<Long> pendingUpdateIds = CollectionUtils.removeAll(newIds, currentIds);
        if (CollectionUtils.isNotEmpty(pendingUpdateIds)) {
            update(pendingUpdateIds, ownerType, ownerId);
        }
    }

    @Override
    public Attachment findEntityById(long id) {
        return attachmentMapper.findEntityById(id);
    }

    @Override
    public AttachmentResponse findOne(long id) {
        var entity = findEntityById(id);
        if (entity == null) {
            throw new ResourceNotFoundException();
        }
        return attachmentConverter.toResponse(entity);
    }

    @Override
    public List<AttachmentResponse> findMany(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.lambda().eq(Attachment::getDeleted, 0).in(Attachment::getId, ids);
        return attachmentMapper.selectList(qw).stream().map(attachmentConverter::toResponse).toList();
    }

    @Override
    public Map<Long, List<AttachmentResponse>> findOneToManyByOwnerIds(Collection<Long> ownerIds) {
        if (CollectionUtils.isEmpty(ownerIds)) {
            return Collections.emptyMap();
        }
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.lambda().eq(Attachment::getDeleted, 0).in(Attachment::getOwnerId, ownerIds).orderByDesc(Attachment::getId);
        var rst = attachmentMapper.selectList(qw).stream().map(attachmentConverter::toResponse)
                .collect(Collectors.groupingBy(AttachmentResponse::getOwnerId));
        rst.values().forEach(list -> list.sort(Comparator.comparingLong(AttachmentResponse::getId).reversed()));
        return rst;
    }

    @Override
    public Map<Long, AttachmentResponse> findOneToOneByOwnerIds(Collection<Long> ownerIds) {
        Map<Long, List<AttachmentResponse>> data = findOneToManyByOwnerIds(ownerIds);
        return data.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
    }

    @Override
    public List<Attachment> findAllEntitiesByOwnerId(long ownerId) {
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.lambda().eq(Attachment::getDeleted, 0)
                .eq(Attachment::getOwnerId, ownerId)
                .orderByDesc(Attachment::getId);
        return attachmentMapper.selectList(qw);
    }

    @Override
    public List<Attachment> findManyEntitiesByOwnerId(long ownerId, FileOwnerType ownerType) {
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.lambda().eq(Attachment::getDeleted, 0)
                .eq(Attachment::getOwnerId, ownerId)
                .eq(ownerType != null, Attachment::getOwnerType, ownerType)
                .orderByDesc(Attachment::getId);
        return attachmentMapper.selectList(qw);
    }

    @Override
    public List<AttachmentResponse> findManyByOwnerId(long ownerId, @Nullable FileOwnerType ownerType) {
        return findManyEntitiesByOwnerId(ownerId, ownerType).stream()
                .map(attachmentConverter::toResponse)
                .toList();
    }

    @Override
    public AttachmentResponse findOneByOwnerId(@NonNull long ownerId, @Nullable FileOwnerType ownerType) {
        List<AttachmentResponse> attachments = findManyByOwnerId(ownerId, ownerType);
        if (CollectionUtils.isEmpty(attachments)) {
            return null;
        }
        return attachments.get(0);
    }

    @Override
    public Attachment findEntityByChecksum(String checksum) {
        if (StringUtils.isEmpty(checksum)) {
            return null;
        }
        QueryWrapper<Attachment> qw = new QueryWrapper<>();
        qw.lambda().eq(Attachment::getDeleted, 0)
                .eq(Attachment::getChecksum, checksum)
                .orderByDesc(Attachment::getCreatedAt);
        return attachmentMapper.selectList(qw).stream().findFirst().orElse(null);
    }

    @Override
    public Attachment copy(Attachment attachment) {
        return Attachment.builder().fileName(attachment.getFileName())
                .filePath(attachment.getFilePath()).fileSize(attachment.getFileSize())
                .fileUrl(attachment.getFileUrl()).fileType(attachment.getFileType())
                .mimeType(attachment.getMimeType()).checksum(attachment.getChecksum())
                .build();
    }

    @Override
    public Attachment save(UploadedFile uploadedFile) {
        Attachment attachment = attachmentConverter.fromUploadedFile(uploadedFile);
        createOne(attachment);
        return attachment;
    }

    @Override
    public void deleteById(long id) {
        attachmentMapper.logicDeleteEntityById(id);
        // TODO: 删除对应的文件
    }

    @Override
    public void deleteByIds(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        UpdateWrapper<Attachment> uw = new UpdateWrapper<>();
        uw.lambda().eq(Attachment::getDeleted, 0)
                .in(Attachment::getId, ids)
                .set(Attachment::getDeleted, 1);
        attachmentMapper.update(null, uw);
        // TODO: 删除对应的文件
    }

    @Override
    public void deleteByOwnerId(long ownerId) {
        UpdateWrapper<Attachment> uw = new UpdateWrapper<>();
        uw.lambda().eq(Attachment::getDeleted, 0)
                .eq(Attachment::getOwnerId, ownerId)
                .set(Attachment::getDeleted, 1);
        attachmentMapper.update(null, uw);
        // TODO: 删除对应的文件
    }

    @Override
    public void deleteByOwnerIds(Collection<Long> ownerIds) {
        if (CollectionUtils.isEmpty(ownerIds)) {
            return;
        }
        UpdateWrapper<Attachment> uw = new UpdateWrapper<>();
        uw.lambda().eq(Attachment::getDeleted, 0)
                .in(Attachment::getOwnerId, ownerIds)
                .set(Attachment::getDeleted, 1);
        attachmentMapper.update(null, uw);
        // TODO: 删除对应的文件
    }

    @Override
    public boolean contains(long id) {
        return findEntityById(id) != null;
    }

    @Override
    public boolean containsAll(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        var currentIds = attachmentMapper.findAllByIds(ids).stream()
                .map(Attachment::getId)
                .collect(Collectors.toSet());
        return currentIds.equals(Set.copyOf(ids));
    }

}
