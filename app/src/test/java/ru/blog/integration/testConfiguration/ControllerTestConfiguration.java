package ru.blog.integration.testConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import ru.blog.services.PostsService;
import ru.blog.services.ports.PostCommentsRepository;
import ru.blog.services.ports.PostImagesRepository;
import ru.blog.services.ports.PostLikesRepository;
import ru.blog.services.ports.PostsRepository;


@Import(JdbcTestConfiguration.class)
public class ControllerTestConfiguration {

  @Bean
  public PostsService postsService(PostsRepository postsRepository, PostImagesRepository imagesRepository,
                                   PostLikesRepository postLikesRepository,
                                   PostCommentsRepository postCommentsRepository) {
    return new PostsService(postsRepository, imagesRepository, postLikesRepository, postCommentsRepository);
  }
}
