package eco.ywhc.xr.core.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import eco.ywhc.xr.core.config.property.IdGeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"eco.ywhc.xr.core.mapper"})
@RequiredArgsConstructor
public class MybatisPlusConfig {

    private final IdGeneratorProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public IdentifierGenerator identifierGenerator() {
        Integer workerId = properties.getWorkerId();
        Integer datacenterId = properties.getDatacenterId();
        if (workerId != null && datacenterId != null) {
            return new DefaultIdentifierGenerator(workerId, datacenterId);
        } else {
            return new DefaultIdentifierGenerator(1, 1);
        }
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    public CustomSqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }

}
