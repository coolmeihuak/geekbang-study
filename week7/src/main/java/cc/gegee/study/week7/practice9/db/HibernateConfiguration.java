package cc.gegee.study.week7.practice9.db;

import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.SchemaManagement;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * copy from org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaConfiguration </br>
 * used for default hibernate setting </br>
 * 初始化 hibernate 配置
 */
@Configuration
public class HibernateConfiguration {

    private final ObjectProvider<SchemaManagementProvider> providers;

    private final HibernateProperties hibernateProperties;

    private final JpaProperties properties;

    private final ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy;

    private final ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy;

    private final ConfigurableListableBeanFactory beanFactory;

    private final ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public HibernateConfiguration(ObjectProvider<SchemaManagementProvider> providers, HibernateProperties hibernateProperties, JpaProperties properties, ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy, ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy, ConfigurableListableBeanFactory beanFactory, ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        this.providers = providers;
        this.hibernateProperties = hibernateProperties;
        this.properties = properties;
        this.physicalNamingStrategy = physicalNamingStrategy;
        this.implicitNamingStrategy = implicitNamingStrategy;
        this.beanFactory = beanFactory;
        this.hibernatePropertiesCustomizers = hibernatePropertiesCustomizers;
    }

    /**
     * 获取配置文件信息
     */
    public Map<String, Object> getVendorProperties(DataSource dataSource) {
        List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
                physicalNamingStrategy.getIfAvailable(),
                implicitNamingStrategy.getIfAvailable(), beanFactory,
                this.hibernatePropertiesCustomizers.orderedStream()
                        .collect(Collectors.toList()));
        Supplier<String> defaultDdlMode = () -> new HibernateDefaultDdlAutoProvider(providers)
                .getDefaultDdlAuto(dataSource);
        Map<String, Object> vendorProperties = new LinkedHashMap<>(this.hibernateProperties.determineHibernateProperties(
                properties.getProperties(),
                new HibernateSettings().ddlAuto(defaultDdlMode)
                        .hibernatePropertiesCustomizers(
                                hibernatePropertiesCustomizers)));
        // DdlTransactionIsolatorJtaImpl could not locate TransactionManager to suspend any current transaction; base JtaPlatform impl
        vendorProperties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        // i can not find any reason to set this value, if any question happen, try to set it
//        vendorProperties.put("javax.persistence.transactionType", "JTA");
        return vendorProperties;
    }

    /**
     * 命名策略自动判断
     */
    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
            PhysicalNamingStrategy physicalNamingStrategy,
            ImplicitNamingStrategy implicitNamingStrategy,
            ConfigurableListableBeanFactory beanFactory,
            List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent(
                "org.hibernate.resource.beans.container.spi.BeanContainer",
                getClass().getClassLoader())) {
            customizers
                    .add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER,
                            new SpringBeanContainer(beanFactory)));
        }
        if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
            customizers.add(new NamingStrategiesHibernatePropertiesCustomizer(
                    physicalNamingStrategy, implicitNamingStrategy));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    /**
     * 自动进行建表操作
     */
    static class HibernateDefaultDdlAutoProvider implements SchemaManagementProvider {

        private final Iterable<SchemaManagementProvider> providers;

        HibernateDefaultDdlAutoProvider(Iterable<SchemaManagementProvider> providers) {
            this.providers = providers;
        }

        public String getDefaultDdlAuto(DataSource dataSource) {
            if (!EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
                return "none";
            }
            SchemaManagement schemaManagement = getSchemaManagement(dataSource);
            if (SchemaManagement.MANAGED.equals(schemaManagement)) {
                return "none";
            }
            return "create-drop";

        }

        @Override
        public SchemaManagement getSchemaManagement(DataSource dataSource) {
            return StreamSupport.stream(this.providers.spliterator(), false)
                    .map((provider) -> provider.getSchemaManagement(dataSource))
                    .filter(SchemaManagement.MANAGED::equals).findFirst()
                    .orElse(SchemaManagement.UNMANAGED);
        }

    }

    private static class NamingStrategiesHibernatePropertiesCustomizer
            implements HibernatePropertiesCustomizer {

        private final PhysicalNamingStrategy physicalNamingStrategy;

        private final ImplicitNamingStrategy implicitNamingStrategy;

        NamingStrategiesHibernatePropertiesCustomizer(PhysicalNamingStrategy physicalNamingStrategy, ImplicitNamingStrategy implicitNamingStrategy) {
            this.physicalNamingStrategy = physicalNamingStrategy;
            this.implicitNamingStrategy = implicitNamingStrategy;
        }

        /**
         * 数据库命名映射策略
         *
         * @param hibernateProperties the JPA vendor properties to customize
         */
        @Override
        public void customize(Map<String, Object> hibernateProperties) {
            if (this.physicalNamingStrategy != null) {
                hibernateProperties.put("hibernate.physical_naming_strategy", this.physicalNamingStrategy);
            }
            if (this.implicitNamingStrategy != null) {
                hibernateProperties.put("hibernate.implicit_naming_strategy", this.implicitNamingStrategy);
            }
        }
    }
}
