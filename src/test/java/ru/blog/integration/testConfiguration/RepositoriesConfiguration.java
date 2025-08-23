package ru.blog.integration.testConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.blog.repositories.PostCommentsJdbcNativeRepository;
import ru.blog.repositories.PostImagesJdbcNativeRepository;
import ru.blog.repositories.PostsJdbcNativeRepository;
import ru.blog.repositories.PostsLikesJdbcNativeRepository;
import ru.blog.services.ports.PostCommentsRepository;
import ru.blog.services.ports.PostImagesRepository;
import ru.blog.services.ports.PostLikesRepository;
import ru.blog.services.ports.PostsRepository;

@Configuration
@Import(JdbcTestConfiguration.class)
@Profile("!controller-test")
public class RepositoriesConfiguration {

  @Bean
  public PostCommentsRepository postCommentsRepository(JdbcTemplate jdbcTemplate) {
    return new PostCommentsJdbcNativeRepository(jdbcTemplate);
  }

  @Bean
  public PostLikesRepository postLikesRepository(JdbcTemplate jdbcTemplate) {
    return new PostsLikesJdbcNativeRepository(jdbcTemplate);
  }

  @Bean
  public PostImagesRepository postImagesRepository(JdbcTemplate jdbcTemplate) {
    return new PostImagesJdbcNativeRepository(jdbcTemplate);
  }
  @Bean
  public PostsRepository postsRepository(JdbcTemplate jdbcTemplate) {
    return new PostsJdbcNativeRepository(jdbcTemplate);
  }
}
