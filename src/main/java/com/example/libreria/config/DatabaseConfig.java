package com.example.libreria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    /**
     * Configura y proporciona una instancia de {@link DataSource} para
     * conectarse a la base de datos PostgreSQL.
     *
     * @return una instancia configurada de {@link DataSource}.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/mi_base_de_datos");
        dataSource.setUsername("usuario");
        dataSource.setPassword("contrasena");
        return dataSource;
    }

    /**
     * Configura y proporciona una instancia de {@link JdbcTemplate} para
     * realizar operaciones de acceso a datos.
     *
     * @param dataSource la instancia de {@link DataSource} que se usará para la
     * configuración de {@link JdbcTemplate}.
     * @return una instancia configurada de {@link JdbcTemplate}.
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
