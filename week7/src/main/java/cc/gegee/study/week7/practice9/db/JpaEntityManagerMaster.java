package cc.gegee.study.week7.practice9.db;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

import javax.persistence.EntityManager;
import java.util.Objects;

import static cc.gegee.study.week7.practice9.Application.BASE_PACKAGE;
import static cc.gegee.study.week7.practice9.db.JpaEntityManagerMaster.ENTITY_MANAGER_FACTORY_BEAN;

/**
 * 自定义 JPA 的 EntityManager
 */
@Configuration
@EnableJpaRepositories(basePackages = {BASE_PACKAGE}, entityManagerFactoryRef = ENTITY_MANAGER_FACTORY_BEAN)
public class JpaEntityManagerMaster {

    public final static String ENTITY_MANAGER_FACTORY_BEAN = "masterEntityManagerFactoryBean";

    private final RoutingDataSource dataSource;

    private final EntityManagerFactoryBuilder builder;

    private final HibernateConfiguration hibernateConfiguration;

    public JpaEntityManagerMaster(RoutingDataSource dataSource, EntityManagerFactoryBuilder builder, HibernateConfiguration hibernateConfiguration) {
        this.dataSource = dataSource;
        this.builder = builder;
        this.hibernateConfiguration = hibernateConfiguration;
    }

    @Bean
//    @Primary
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactoryBean() {
        return builder
                .dataSource(dataSource)
                .properties(hibernateConfiguration.getVendorProperties(dataSource))
                .packages(BASE_PACKAGE)
                .persistenceUnit("persistenceUnitMaster")
//                .jta(true)
                .build();
    }

    @Bean
//    @Primary
    public EntityManager masterEntityManager() {
        return SharedEntityManagerCreator.createSharedEntityManager(Objects.requireNonNull(masterEntityManagerFactoryBean().getObject()));
    }
}
