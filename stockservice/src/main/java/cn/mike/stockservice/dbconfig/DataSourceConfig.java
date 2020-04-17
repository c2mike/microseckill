package cn.mike.stockservice.dbconfig;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    @Autowired
    private DbPropConfig dbPropConfig;

    @Bean
    public DataSource DruidDataSource() throws SQLException {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dbPropConfig.getDriver());
        druidDataSource.setUrl(dbPropConfig.getUrl());
        druidDataSource.setUsername(dbPropConfig.getUsername());
        druidDataSource.setPassword(dbPropConfig.getPassword());
        druidDataSource.setMaxActive(dbPropConfig.getMaxActive());
        druidDataSource.setInitialSize(dbPropConfig.getInitialSize());
        druidDataSource.setMaxWait(dbPropConfig.getMaxWait());
        druidDataSource.setMinIdle(dbPropConfig.getMinIdle());
        druidDataSource.setValidationQuery(dbPropConfig.getValidationQuery());
        druidDataSource.setTestWhileIdle(dbPropConfig.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(dbPropConfig.isTestOnBorrow());
        druidDataSource.setTestOnReturn(dbPropConfig.isTestOnReturn());
        // druidDataSource.setTimeBetweenEvictionRunsMillis(dbPropConfig.getTimeBetweenEvictionRunsMillis());
        // druidDataSource.setMaxEvictableIdleTimeMillis(dbPropConfig.getMinEvictableIdleTimeMillis());
        druidDataSource.setPoolPreparedStatements(dbPropConfig.isPoolPreparedStatements());
        druidDataSource.setFilters(dbPropConfig.getFilters());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(dbPropConfig.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setUseGlobalDataSourceStat(dbPropConfig.isUseGlobalDataSourceStat());

        return  druidDataSource;
    }
}
