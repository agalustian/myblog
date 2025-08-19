package ru.blog.integration.repositories;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.blog.generators.PostCommentsGenerator;
import ru.blog.integration.testConfiguration.RepositoriesConfiguration;
import ru.blog.models.PostComment;
import ru.blog.services.ports.PostCommentsRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoriesConfiguration.class)
public class PostJdbcNativeRepositoryTests {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PostCommentsRepository postCommentsRepository;

  private PostComment postComment;

  @BeforeEach
  void setup() {
    jdbcTemplate.update("delete from post_comments");

    postComment = PostCommentsGenerator.generate().getFirst();

    postCommentsRepository.save(postComment.getPostId(), postComment.getUserId(), postComment.getComment());
  }

  @Test
  public void shouldFindPostComment() {
    var comment = postCommentsRepository.getComments(postComment.getPostId()).getFirst();

    Assertions.assertEquals(PostCommentsGenerator.generate().getFirst().getPostId(), comment.getPostId());
    Assertions.assertEquals(PostCommentsGenerator.generate().getFirst().getUserId(), comment.getUserId());
    Assertions.assertEquals(PostCommentsGenerator.generate().getFirst().getComment(), comment.getComment());
    Assertions.assertNotNull(comment.getId());
    Assertions.assertNotNull(comment.getCreatedAt());
    Assertions.assertNull(comment.getUpdatedAt());
  }

  @Test
  public void shouldSavePostComment() {
    var postComment = PostCommentsGenerator.generate().getFirst();
    postCommentsRepository.save("customPostId", postComment.getUserId(), postComment.getComment());

    var comment = postCommentsRepository.getComments("customPostId").getFirst();

    Assertions.assertEquals("customPostId", comment.getPostId());
  }

  @Test
  public void shouldUpdatePostComment() {
    var comment = postCommentsRepository.getComments(postComment.getPostId()).getFirst();
    postCommentsRepository.update(comment.getId(), comment.getUserId(), "new custom comment");
    var updatedComment = postCommentsRepository.getComments(postComment.getPostId()).getFirst();

    Assertions.assertEquals("new custom comment", updatedComment.getComment());
  }

  @Test
  public void shouldRemovePostComment() {
    var comment = postCommentsRepository.getComments(postComment.getPostId()).getFirst();
    postCommentsRepository.remove(comment.getId(), comment.getUserId());

    var commentsAfterRemoving = postCommentsRepository.getComments(postComment.getPostId());

    Assertions.assertEquals(0, commentsAfterRemoving.size());
  }

  @Test
  public void shouldFindPostCommentsCountForPosts() {
    var postComment = PostCommentsGenerator.generate().getFirst();
    postCommentsRepository.save(postComment.getPostId(), postComment.getUserId(), postComment.getComment());
    postCommentsRepository.save(postComment.getPostId(), postComment.getUserId(), postComment.getComment());

    var commentsCount = postCommentsRepository.findPostsCommentsCount(List.of(postComment.getPostId()));

    Assertions.assertEquals(3, commentsCount.get(postComment.getPostId()));
  }

}
