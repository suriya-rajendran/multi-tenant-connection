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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManagerFactory", transactionManagerRef = "primaryTransactionManager", basePackages = {
		"multi.tenant.repository.primary" })
public class PrimaryDataSourceConfiguration {

	@Primary
	@Bean(name = "primaryDataSourceProperties")
	@ConfigurationProperties("spring.datasource.primary")
	public DataSourceProperties primaryDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "primaryEntityManagerFactoryBuilderProperties")
	@ConfigurationProperties("spring.datasource.primary")
	public EntityManagerFactoryBuilderProperties primaryEntityManagerFactoryBuilderProperties() {
		return new EntityManagerFactoryBuilderProperties();
	}

	@Primary
	@Bean(name = "primaryDataSource") 
	public DataSource primaryDataSource(
			@Qualifier("primaryDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
		return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean(name = "primaryEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
			EntityManagerFactoryBuilder primaryEntityManagerFactoryBuilder,
			@Qualifier("primaryDataSource") DataSource primaryDataSource,
			@Qualifier("primaryEntityManagerFactoryBuilderProperties") EntityManagerFactoryBuilderProperties primaryEntityManagerFactoryBuilderProperties) {

		Map<String, String> primaryJpaProperties = new HashMap<>();
		primaryJpaProperties.put("hibernate.dialect", primaryEntityManagerFactoryBuilderProperties.getDialect());
		primaryJpaProperties.put("hibernate.hbm2ddl.auto", primaryEntityManagerFactoryBuilderProperties.getDdl_auto());

		return primaryEntityManagerFactoryBuilder.dataSource(primaryDataSource)
				.packages("multi.tenant.datamodel.primary").persistenceUnit("primaryDataSource")
				.properties(primaryJpaProperties).build();
	}

	@Primary
	@Bean(name = "primaryTransactionManager")
	public PlatformTransactionManager primaryTransactionManager(
			@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {

		return new JpaTransactionManager(primaryEntityManagerFactory);
	}
}