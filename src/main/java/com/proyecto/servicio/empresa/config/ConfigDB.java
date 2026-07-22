package com.proyecto.servicio.empresa.config;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import lombok.extern.slf4j.Slf4j;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "datasource.ucp")
@EnableJpaRepositories(entityManagerFactoryRef = "sfEntityManagerFactory", transactionManagerRef = "sfTransactionManager",
        basePackages = {"com.proyecto.servicio.empresa.repositorys.sf"})
public class ConfigDB {

    @Autowired
    private Environment env;

    /**
     * se define el acceso al data source y se retorna
     * se genera la instancianove
     *
     * @return data source
     */
    @Bean(name = "sfDataSource")
    public DataSource sfDataSource() {
        log.info("inicia pool de ef");
        PoolDataSource pds = null;
        try {
            pds = PoolDataSourceFactory.getPoolDataSource();
            pds.setConnectionFactoryClassName(env.getProperty("spring.datasource.driver.class"));
            pds.setURL(env.getProperty("spring.datasource.url"));
            pds.setUser(env.getProperty("spring.datasource.username"));
            pds.setPassword(env.getProperty("spring.datasource.password"));
            pds.setMinPoolSize(1);
            pds.setInitialPoolSize(5);
            pds.setMaxPoolSize(10);
            pds.setValidateConnectionOnBorrow(true);
            pds.setConnectionPoolName("ef");
            pds.setConnectionWaitTimeout(40);
            pds.setSQLForValidateConnection("SELECT 1");
        } catch (Exception ea) {
            log.error("Error BD EF: " + ea.getMessage());
        }
        return pds;
    }

    /**
     * unidad transaccional, se define de donde se van a tomar las entidades
     *
     * @return entiti
     */
    @Primary
    @DependsOn("erpFlyway")
    @Bean(name = "sfEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(sfDataSource());
        em.setPackagesToScan("com.proyecto.servicio.empresa.entity.sf");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.query.timeout", 60000);
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    /**
     * se genera la instancia
     *
     * @return instancia
     */

    @Bean("sfTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("sfEntityManagerFactory") LocalContainerEntityManagerFactoryBean topicsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(topicsEntityManagerFactory.getObject()));
    }
}
