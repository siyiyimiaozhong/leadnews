package com.siyi.common.mysql.core;

import com.siyi.utils.common.Base64Utils;
import com.siyi.utils.common.DESUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 自动化配置核心数据库的连接配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "mysql.core")
@PropertySource("classpath:mysql-core-jdbc.properties")
@MapperScan(basePackages = "com.siyi.model.mappers", sqlSessionFactoryRef = "mysqlCoreSqlSessionFactory")
public class MysqlCoreConfig {
    private String jdbcUrl;
    private String jdbcUserName;
    private String jdbcPassword;
    private String jdbcDriver;
    private String rootMapper;//mapper文件在classpath下存放的根路径
    private String aliasesPackage;//别名包
    private String helperDialect = "mysql";// 分页语言
    private Boolean helperReasonable = false;//分页合理化
    private Boolean supportMethodsArguments = false;//自动根据上面 params 配置的字段中取值
    private String params;//pageNum,pageSize,count,pageSizeZero,reasonable，不配置映射的用默认值， 默认值为pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero

    /**
     * 这是最快的数据库连接池
     *
     * @return
     */
    @Bean
    public DataSource mysqlCoreDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(this.getJdbcUserName());
        hikariDataSource.setPassword(this.getRealPassword());
        hikariDataSource.setJdbcUrl(this.getJdbcUrl());
        //最大连接数
        hikariDataSource.setMaximumPoolSize(50);
        //最小连接数
        hikariDataSource.setMinimumIdle(5);
        hikariDataSource.setDriverClassName(this.getJdbcDriver());
        return hikariDataSource;
    }

    /**
     * 这是Mybatis的Session
     *
     * @return
     * @throws IOException
     */
    @Bean
    public SqlSessionFactoryBean mysqlCoreSqlSessionFactory(@Qualifier("mysqlCoreDataSource") DataSource mysqlCoreDataSource) throws IOException {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        //设置数据源
        sessionFactory.setDataSource(mysqlCoreDataSource);
        //别名
        sessionFactory.setTypeAliasesPackage(this.getAliasesPackage());
        //mapper文件存储的位置
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources(this.getMapperFilePath()));
        //开启自动驼峰标识
        org.apache.ibatis.session.Configuration mybatisConf = new org.apache.ibatis.session.Configuration();
        mybatisConf.setMapUnderscoreToCamelCase(true);
        sessionFactory.setConfiguration(mybatisConf);
        return sessionFactory;
    }

    /**
     * 密码反转，简单示意密码在配置文件中的加密处理
     *
     * @return
     */
    public String getRealPassword() {
        return new String(DESUtils.hexStringToByte(DESUtils.decode(new String(Base64Utils.decode(this.getJdbcPassword())))));
    }

    /**
     * 拼接Mapper.xml文件的存放路径
     *
     * @return
     */
    public String getMapperFilePath() {
        return new StringBuffer().append("classpath:").append(this.getRootMapper()).append("/**/*.xml").toString();
    }
}
