package com.shopping.mall.api.configure;

import com.shopping.mall.core.enums.BooleanValueCode;
import com.shopping.mall.core.enums.SortTypeCode;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.LocalDateTypeHandler;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <pre>
 * Description : MyBatis 설정
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class MybatisConfigure {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    private TypeHandler[] typeHandlers = new TypeHandler[]{
        new BooleanValueCode.TypeHandler(),
        new LocalDateTimeTypeHandler(),
        new LocalDateTypeHandler(),
        new SortTypeCode.TypeHandler(),
    };

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/**/*.xml"));
        sqlSessionFactory.setTypeHandlers(typeHandlers);
        sqlSessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);     // 컬럼명 맵핑시 "_" 제외 시키기
        return sqlSessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager dataTxManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
