package com.kulygin.musiccloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceInit {
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return createDataSource();
    }

    private SimpleDriverDataSource createDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
        simpleDriverDataSource.setDriverClass(org.h2.Driver.class);
        simpleDriverDataSource.setUsername("kulyginvv");
        simpleDriverDataSource.setPassword("123456");

        simpleDriverDataSource.setUrl("jdbc:h2:~/.music-cloud/db/mcData;DB_CLOSE_ON_EXIT=FALSE;IFEXISTS=FALSE;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;");

        return simpleDriverDataSource;
    }
}
