package ru.blog.integration.repositories;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.blog.generators.PostDetailsGenerator;
import ru.blog.integration.testConfiguration.RepositoriesConfiguration;
import ru.blog.models.PostDetails;
import ru.blog.dto.SearchPostsFilter;
import ru.blog.services.ports.PostLikesRepository;
import ru.blog.services.ports.PostsRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoriesConfiguration.class)
public class PostsJdbcNativeRepositoryTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private PostLikesRepository postLikesRepository;

  @BeforeEach
  void setup() {
    jdbcTemplate.update("delete from posts");
  }

  @Test
  public void shouldSavePostDetails() {
    var postId = postsRepository.save(PostDetailsGenerator.generate());

    Assertions.assertNotNull(postId);
  }

  @Test
  public void shouldGetPostDetailsById() {
    var generatedPostDetails = PostDetailsGenerator.generate();
    var postId = postsRepository.save(generatedPostDetails);
    var postDetails = postsRepository.getPostDetailsById(postId);

    Assertions.assertEquals(postId, postDetails.getId());
    Assertions.assertEquals(generatedPostDetails.getUserId(), postDetails.getUserId());
    Assertions.assertEquals(generatedPostDetails.getText(), postDetails.getText());
    Assertions.assertEquals(generatedPostDetails.getTags(), postDetails.getTags());
    Assertions.assertEquals(generatedPostDetails.getTitle(), postDetails.getTitle());
    Assertions.assertEquals(generatedPostDetails.getLikesCount(), postDetails.getLikesCount());

    Assertions.assertNotNull(postDetails.getId());
    Assertions.assertNotNull(postDetails.getCreatedAt());

    Assertions.assertNull(postDetails.getUpdatedAt());
  }

  @Test
  public void shouldFindPostDetailsWithLikesCount() {
    var postDetails = PostDetailsGenerator.generate();

    var postId = postsRepository.save(postDetails);
    postLikesRepository.set(postId, postDetails.getUserId());
    postLikesRepository.set(postId, "another-user");

    var foundPostDetails = postsRepository.getPostDetailsById(postId);

    Assertions.assertEquals(2, foundPostDetails.getLikesCount());
  }

  @Test
  public void shouldFindPostsLikesCount() {
    var postDetails = PostDetailsGenerator.generate();

    var postId = postsRepository.save(postDetails);
    var anotherPostId = postsRepository.save(postDetails);

    postLikesRepository.set(postId, postDetails.getUserId());
    postLikesRepository.set(postId, "another-user");
    postLikesRepository.set(anotherPostId, postDetails.getUserId());

    var postsLikesCount = postsRepository.findPostsLikesCount(List.of(postId, anotherPostId));

    Assertions.assertEquals(2, postsLikesCount.get(postId));
    Assertions.assertEquals(1, postsLikesCount.get(anotherPostId));
  }

  @Test
  public void shouldUpdatePostDetailsById() {
    var generatedPostDetails = PostDetailsGenerator.generate();
    var postId = postsRepository.save(generatedPostDetails);
    var postDetailsUpdate = new PostDetails(
        postId,
        "update-title",
        "updated-text",
        generatedPostDetails.getUserId(),
        List.of("test", "another-tag"),
        null,
        null
    );

    postsRepository.update(postDetailsUpdate);

    var updatedPostDetails = postsRepository.getPostDetailsById(postId);

    Assertions.assertEquals(postId, updatedPostDetails.getId());
    Assertions.assertEquals(generatedPostDetails.getUserId(), generatedPostDetails.getUserId());
    Assertions.assertEquals(generatedPostDetails.getLikesCount(), generatedPostDetails.getLikesCount());
    Assertions.assertEquals(postDetailsUpdate.getText(), updatedPostDetails.getText());
    Assertions.assertEquals(postDetailsUpdate.getTags(), updatedPostDetails.getTags());
    Assertions.assertEquals(postDetailsUpdate.getTitle(), updatedPostDetails.getTitle());

    Assertions.assertNotNull(updatedPostDetails.getId());
    Assertions.assertNotNull(updatedPostDetails.getCreatedAt());
    Assertions.assertNotNull(updatedPostDetails.getUpdatedAt());
  }

  @Test
  public void shouldRemovePostDetails() {
    var generatedPostDetails = PostDetailsGenerator.generate();
    var postId = postsRepository.save(generatedPostDetails);
    var postDetails = postsRepository.getPostDetailsById(postId);
    postsRepository.remove(postDetails.getId(), postDetails.getUserId());
    var postDetailsAfterRemoving = postsRepository.getPostDetailsById(postId);

    Assertions.assertNull(postDetailsAfterRemoving);
  }

  @Test
  public void shouldFindPostPreviews() {
    var generatedPostDetails = PostDetailsGenerator.generate();
    postsRepository.save(generatedPostDetails);
    postsRepository.save(generatedPostDetails);

    var postPreviews = postsRepository.searchPostPreview(new SearchPostsFilter(""), PageRequest.of(1, 2));

    Assertions.assertNotNull(postPreviews);
    Assertions.assertEquals(2, postPreviews.size());
  }

  @Test
  public void shouldFindPostPreviewsByTag() {
    var postDetails = PostDetailsGenerator.generate();
    var anotherPostDetails = PostDetailsGenerator.generate("test-tag");
    postsRepository.save(postDetails);
    postsRepository.save(anotherPostDetails);

    var postPreviews = postsRepository.searchPostPreview(new SearchPostsFilter("test-tag"), PageRequest.of(1, 2));

    Assertions.assertNotNull(postPreviews);
    Assertions.assertEquals(1, postPreviews.size());
  }

}
