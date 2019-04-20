package com.kulygin.musiccloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Slf4j
@Configuration
@ConfigurationProperties("spring.datasource")
public class DataSourceInit {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return createDataSource();
    }

    private SimpleDriverDataSource createDataSource() {
        SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();

        if (driverClassName.equals("com.mysql.jdbc.Driver")) {
            simpleDriverDataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        } else if (driverClassName.equals("org.postgresql.Driver")) {
            simpleDriverDataSource.setDriverClass(org.postgresql.Driver.class);
        } else {
            throw new RuntimeException("Unknown database driver");
        }

        simpleDriverDataSource.setUsername(username);
        simpleDriverDataSource.setPassword(password);

        simpleDriverDataSource.setUrl(url);

        return simpleDriverDataSource;
    }
}
