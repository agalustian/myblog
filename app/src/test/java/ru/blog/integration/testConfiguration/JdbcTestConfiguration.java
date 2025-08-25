package ru.blog.integration.testConfiguration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class JdbcTestConfiguration {
  @Bean
  public DataSource dataSource() {
    var dataSource = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .build();

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("test-schema.sql"));
    populator.execute(dataSource);

    return dataSource;
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

}