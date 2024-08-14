package com.sevenhallo.multitenant.config.multitenant;

import com.sevenhallo.multitenant.config.TenantContext;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Configuration
public class HibernateConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private DataSource dataSource;

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Qualifier("tenantConnectionProvider") MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
            CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl
    ) {
        //Properties related to MultiTenancyStrategy Schema
        Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertiesMap.put("hibernate.multiTenancy", "SCHEMA");
        jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

        //Properties related to MultiTenancyStrategy Database
        //jpaPropertiesMap.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        //jpaPropertiesMap.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        //jpaPropertiesMap.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

        jpaPropertiesMap.put(Environment.FORMAT_SQL, true);
        jpaPropertiesMap.put(Environment.SHOW_SQL, true);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sevenhallo.multitenant.*");
        em.setJpaVendorAdapter(this.jpaVendorAdapter());
        em.setJpaPropertyMap(jpaPropertiesMap);
        return em;
    }

    @Bean
    @Scope(
            value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public JdbcTemplate jdbcTemplate() {
        // Wrap original DataSource
        DataSource tenantAwareDataSource = new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                Connection connection = dataSource.getConnection();
                connection.unwrap(Connection.class).setSchema(TenantContext.getCurrentTenant());
                return connection;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                Connection connection = dataSource.getConnection(username, password);
                connection.unwrap(Connection.class).setSchema(TenantContext.getCurrentTenant());
                return connection;
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return dataSource.unwrap(iface);
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return dataSource.isWrapperFor(iface);
            }

            public Statement createStatement() throws SQLException {
                return dataSource.getConnection().createStatement();
            }

            // Implement other DataSource methods by delegating to the original DataSource
            // ...
        };

        return new JdbcTemplate(tenantAwareDataSource);
    }
}