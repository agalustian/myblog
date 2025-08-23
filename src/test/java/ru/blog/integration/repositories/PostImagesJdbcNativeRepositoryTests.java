package ru.blog.integration.repositories;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.blog.integration.testConfiguration.RepositoriesConfiguration;
import ru.blog.services.ports.PostImagesRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoriesConfiguration.class)
public class PostImagesJdbcNativeRepositoryTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PostImagesRepository postImagesRepository;

  private static String POST_ID = "postId";

  private static byte[] IMAGE = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};

  @BeforeEach
  void setup() {
    jdbcTemplate.update("delete from post_images");

    postImagesRepository.save(POST_ID, new ByteArrayInputStream(IMAGE));
  }

  @Test
  public void shouldGetImage() {
    var image = postImagesRepository.get(POST_ID);

    Assertions.assertEquals("Hello World", new String(image, StandardCharsets.UTF_8));
  }

  @Test
  public void shouldSaveImage() {
    postImagesRepository.save("customPostId", new ByteArrayInputStream(IMAGE));
    var image = postImagesRepository.get("customPostId");

    Assertions.assertEquals("Hello World", new String(image, StandardCharsets.UTF_8));
  }

  @Test
  public void shouldUpdateImage() {
    byte[] helloImage = { 72, 101, 108, 108, 111 };
    postImagesRepository.updateByPostId(POST_ID, new ByteArrayInputStream(helloImage));
    var image = postImagesRepository.get(POST_ID);

    Assertions.assertEquals("Hello", new String(image, StandardCharsets.UTF_8));
  }

}
