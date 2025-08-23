package ru.blog.integration.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.blog.repositories.PostsLikesJdbcNativeRepository;
import ru.blog.services.ports.PostLikesRepository;

@ActiveProfiles("test")
@DataJdbcTest
@Import(PostsLikesJdbcNativeRepository.class)
public class PostLikesJdbcNativeRepositoryTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PostLikesRepository postLikesRepository;

  private static String POST_ID = "postId";

  private static String USER_ID = "userId";

  @BeforeEach
  void setup() {
    jdbcTemplate.update("delete from post_likes");

    postLikesRepository.set(POST_ID, USER_ID);
  }

  @Test
  public void shouldCheckLikeExistence() {
    var likeExists = postLikesRepository.exists(POST_ID, USER_ID);

    Assertions.assertTrue(likeExists);
  }

  @Test
  public void shouldSetPostLike() {
    postLikesRepository.set(POST_ID, "custom user id");
    var likeExists = postLikesRepository.exists(POST_ID, "custom user id");

    Assertions.assertTrue(likeExists);
  }

  @Test
  public void shouldRemovePostLike() {
    postLikesRepository.remove(POST_ID, USER_ID);

    var likeExists = postLikesRepository.exists(POST_ID, "custom user id");

    Assertions.assertFalse(likeExists);
  }

}
