package eco.ywhc.xr.core.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public interface CustomBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param entityList 实体对象集合
     * @return 插入成功的数量
     */
    int bulkInsert(List<T> entityList);

    /**
     * 根据ID查找Entity
     *
     * @param id Entity ID
     */
    default T findEntityById(final Serializable id) {
        QueryWrapper<T> qw = Wrappers.query();
        qw.eq("deleted", 0).eq("id", id);
        return selectOne(qw);
    }

    /**
     * 根据ID列表查找Entity列表
     *
     * @param ids Entity ID列表
     */
    default List<T> findAllByIds(final Collection<? extends Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        QueryWrapper<T> qw = Wrappers.query();
        qw.eq("deleted", 0).in("id", ids);
        return selectList(qw);
    }

    /**
     * 根据ID删除Entity
     *
     * @param id      Entity ID
     * @param logical 是否为逻辑删除
     */
    default int deleteEntityById(final Serializable id, boolean logical) {
        T entity = findEntityById(id);
        if (entity == null) {
            return 0;
        }
        if (!logical) {
            return deleteById(id);
        }
        UpdateWrapper<T> uw = Wrappers.update();
        uw.set("deleted", true).eq("id", id);
        return update(null, uw);
    }

    /**
     * 根据ID删除Entity
     *
     * @param id Entity ID
     */
    default int deleteEntityById(final Serializable id) {
        return deleteEntityById(id, false);
    }

    /**
     * 根据ID逻辑删除Entity
     *
     * @param id Entity ID
     */
    default int logicDeleteEntityById(final Serializable id) {
        return deleteEntityById(id, true);
    }

}
