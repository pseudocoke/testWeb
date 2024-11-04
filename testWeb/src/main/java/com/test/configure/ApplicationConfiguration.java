package com.test.configure;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mass.db.mapper.BasicMapper;
import com.mass.db.service.BasicQueryService;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = { "com.test" }, excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = { Controller.class }),
		@Filter(type = FilterType.ANNOTATION, value = { Configuration.class }) })
@EnableWebMvc
public class ApplicationConfiguration {

	// JNDI 정보를 확인하기 위한 environment 처리
	@Autowired
	Environment environment;

	// Java Mail Sender 설정
	// https://offbyone.tistory.com/167
	@Bean(name = "mailSender")
	public JavaMailSenderImpl getMailSender() {
		JavaMailSenderImpl resultObj = new JavaMailSenderImpl();

		resultObj.setHost("smtp.gmail.com");
		resultObj.setPort(587);
		resultObj.setUsername("itvillage.dev@gmail.com");
		resultObj.setPassword("jfteiueivgrewavr");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", true);
		resultObj.setJavaMailProperties(javaMailProperties);

		return resultObj;
	}
	
	//	HikariConnectionPool에 관련된 Connection Pool 생성
	@Bean(name = "dataSource")
	public DataSource initializeDataSource() {
				
		HikariDataSource resultDataSource = new HikariDataSource();
		
		String serverType = environment.getProperty("server.type");	//	JNDI의 서버타입을 이용하여 분기할 수 있다. 현재는 미사용
		if(StringUtils.isEmpty(serverType)) serverType = "PRD";

		if("DEV".equalsIgnoreCase(serverType) || "STG".equalsIgnoreCase(serverType)) {
			//	jdbc:postgresql://192.168.0.80:5432/iorder
			//	org.postgresql.Driver
			resultDataSource.setDriverClassName("org.postgresql.Driver");
			resultDataSource.setJdbcUrl("jdbc:postgresql://192.168.0.80:5432/iorder");
			resultDataSource.setUsername("iorder");
			resultDataSource.setPassword("iorder0516!@");
			resultDataSource.setPoolName("devPool");
			return resultDataSource;
		} else {
			resultDataSource.setDriverClassName("org.postgresql.Driver");
			resultDataSource.setJdbcUrl("jdbc:postgresql://192.168.0.80:5432/iorder");
			resultDataSource.setUsername("iorder");
			resultDataSource.setPassword("iorder0516!@");
			resultDataSource.setPoolName("prdPool");
			
		    //	resultDataSource.setMaximumPoolSize(5);    
			//	resultDataSource.setConnectionTestQuery("SELECT 1");    
			
			return resultDataSource;			
		}
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean getSqlSessionFactoryBean() {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

		sessionFactoryBean.setDataSource(initializeDataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] mapperLocations;
		try {
			mapperLocations = resolver.getResources("classpath*:query/**/**/*_postgresql.xml");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		sessionFactoryBean.setMapperLocations(mapperLocations);

		return sessionFactoryBean;
	}
	
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() throws Exception {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(initializeDataSource());
        return tm;
    }
		
	@Bean(name = "sqlSession")
	public SqlSessionTemplate getSqlSessionTemplate()
	{
		SqlSessionFactory sessionFactory;
		try {
			sessionFactory = getSqlSessionFactoryBean().getObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		if(sessionFactory == null) return null;	//	session factory도 얻을 수 없다면 초기화 불가
		
		SqlSessionTemplate resultObj = new SqlSessionTemplate(sessionFactory);
		return resultObj;
	}	
	
	//	BasicQueryService 연결
	@Bean(name = "basicMapper") 
	public BasicMapper getBasicMapper() {
		BasicMapper bm = new BasicMapper();
		bm.setSqlSessionTemplate(getSqlSessionTemplate());
		bm.setShowSQL(true); 	//	쿼리를 보여주자.
		return bm;
	}
	
	/*
	 * basicQueryService의 로그를 뿌려주기 위해서는 logback.xml 설정이 필수
	 * <logger name="com.mass.db" level="info"/> 로그레벨에 맞춰서 info 레벨은 반드시 보여주도록 하자.
	 * mybatis를 타기 때문에 다른 query 툴을 사용할 경우 이중으로 나올 수 있으므로, 
	 * 그 때는 위의 showSQL을 끄도록 한다.
	 */
	
	@Bean(name = "basicQueryService") 
	public BasicQueryService getBasicQueryService() {
		BasicQueryService bqs = new BasicQueryService();
		bqs.setBasicMapper(getBasicMapper());
		return bqs;
	}
		
}
