package org.bmsource.dwh.common.multitenancy.impl;

import org.bmsource.dwh.common.multitenancy.EnableMultitenancy;
import org.bmsource.dwh.common.multitenancy.impl.concurrent.ContextAwarePoolExecutor;
import org.bmsource.dwh.common.portal.Tenant;
import org.bmsource.dwh.common.portal.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultitenancyConfiguration implements WebMvcConfigurer {

    @Autowired
    TenantContextInterceptor tenantInterceptor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSourceProperties properties;

    @Autowired
    private TenantRepository repository;

//    @Autowired
//    JpaVendorAdapter jpaVendorAdapter;
//
//    @Autowired
//    JpaProperties props;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantInterceptor);
    }

    @Bean
    public MappedInterceptor repositoryRestResourcesInterceptor() {
        return new MappedInterceptor(new String[]{"/**"}, tenantInterceptor);
    }

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();
        for (Tenant tenant : repository.findAll()) {
            resolvedDataSources.put(tenant.getSchemaName(), createDatasource(tenant.getSchemaName()));
        }

        MultitenantDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(createDatasource(null));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    private DataSource createDatasource(String tenantSchema) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create(this.getClass().getClassLoader())
            .driverClassName(properties.getDriverClassName())
            .url(resolveTenantConnectionUrl(tenantSchema))
            .username(properties.getUsername())
            .password(properties.getPassword());
        if (properties.getType() != null) {
            dataSourceBuilder.type(properties.getType());
        }
        return dataSourceBuilder.build();
    }

//    @Primary
//    @Bean(name = "multitenantEntityManagerFactory")
//    public EntityManagerFactory entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource());
//        emf.setJpaVendorAdapter(jpaVendorAdapter);
//        emf.setPackagesToScan(resolvePackageName());
//        emf.setPersistenceUnitName("default");
//        emf.getJpaPropertyMap().putAll(props.getProperties());
//        emf.afterPropertiesSet();
//        return emf.getObject();
//    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        return new ContextAwarePoolExecutor();
    }

    private String resolvePackageName() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(EnableMultitenancy.class);
        if (beans.size() == 0) {
            throw new RuntimeException("No configuration annotated with " + EnableMultitenancy.class.getName());
        }
        return beans.values().iterator().next().getClass().getPackage().getName();
    }

    private String resolveTenantConnectionUrl(String tenantSchema) {
        String join = properties.getUrl().contains("?") ? "&" : "?";
        return tenantSchema != null ?
            properties.getUrl() + join + "currentSchema=" + tenantSchema :
            properties.getUrl();
    }
}
