package com.school.config;

import com.school.util.AppConstant;
import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.jdbc.CfJdbcEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;
    @Bean
    @Primary
    public DataSource getDataSource() {

        try {
            System.out.println("Going into POSTGREE CJDBC ENV DATASOURCE METHOD");
            CfJdbcEnv cfJdbcEnv = new CfJdbcEnv();
            CfCredentials cfCredentials = cfJdbcEnv.findCredentialsByLabel("postgresql-db");
            System.err.println("Got POSTGRE desc connection" + cfCredentials.getMap());
            Map<String, Object> map = cfCredentials.getMap();
            String url = "jdbc:postgresql://"+ (String) map.get("hostname")+":"+(String) map.get("port")+"/"+(String) map.get("dbname");
            String user = (String) map.get("username");
            String password = (String) map.get("password");

            System.err.println("Got Postgree connection url " + url);
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName(AppConstant.driverName);
            dataSourceBuilder.url(url);
            dataSourceBuilder.username(user);
            dataSourceBuilder.password(password);
            return dataSourceBuilder.build();


        }catch(Exception e) {
            System.err.println("Catch Block");
            System.out.println("Username:"+env.getProperty("spring.datasource.username"));
            DriverManagerDataSource dataSource=new DriverManagerDataSource();
            dataSource.setUrl(env.getProperty("spring.datasource.url"));
            dataSource.setDriverClassName(AppConstant.driverName);
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
            return dataSource;
        }

    }
}
