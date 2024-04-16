package eco.ywhc.xr.core.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

/**
 * Mybatis Plus自动填充功能
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MybatisPlusFillHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createdAt", OffsetDateTime.class, OffsetDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updatedAt", OffsetDateTime.class, OffsetDateTime.now());
    }

    /**
     * 严格模式填充策略，默认策略为有值不覆盖，如果提供的值为null也不填充。改为无条件覆盖。
     */
    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        Object obj = fieldVal.get();
        if (obj != null) {
            metaObject.setValue(fieldName, obj);
        }
        return this;
    }

}
