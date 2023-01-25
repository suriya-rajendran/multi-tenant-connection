package multi.tenant.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "secondaryEntityManagerFactory", transactionManagerRef = "secondaryTransactionManager", basePackages = {
		"multi.tenant.repository.secondary" })
public class SecondaryDataSourceConfiguration {
 
	@Bean(name = "secondaryDataSourceProperties")
	@ConfigurationProperties("spring.datasource.secondary")
	public DataSourceProperties secondaryDataSourceProperties() {
		return new DataSourceProperties();
	}
 
	@Bean(name = "secondaryEntityManagerFactoryBuilderProperties")
	@ConfigurationProperties("spring.datasource.secondary")
	public EntityManagerFactoryBuilderProperties secondaryEntityManagerFactoryBuilderProperties() {
		return new EntityManagerFactoryBuilderProperties();
	}
 
	@Bean(name = "secondaryDataSource") 
	public DataSource secondaryDataSource(
			@Qualifier("secondaryDataSourceProperties") DataSourceProperties secondaryDataSourceProperties) {
		return secondaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
 
	@Bean(name = "secondaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
			EntityManagerFactoryBuilder secondaryEntityManagerFactoryBuilder,
			@Qualifier("secondaryDataSource") DataSource secondaryDataSource,
			@Qualifier("secondaryEntityManagerFactoryBuilderProperties") EntityManagerFactoryBuilderProperties secondaryEntityManagerFactoryBuilderProperties) {

		Map<String, String> secondaryJpaProperties = new HashMap<>();
		secondaryJpaProperties.put("hibernate.dialect", secondaryEntityManagerFactoryBuilderProperties.getDialect());
		secondaryJpaProperties.put("hibernate.hbm2ddl.auto", secondaryEntityManagerFactoryBuilderProperties.getDdl_auto());

		return secondaryEntityManagerFactoryBuilder.dataSource(secondaryDataSource)
				.packages("multi.tenant.datamodel.secondary").persistenceUnit("secondaryDataSource")
				.properties(secondaryJpaProperties).build();
	}
 
	@Bean(name = "secondaryTransactionManager")
	public PlatformTransactionManager secondaryTransactionManager(
			@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {

		return new JpaTransactionManager(secondaryEntityManagerFactory);
	}
}