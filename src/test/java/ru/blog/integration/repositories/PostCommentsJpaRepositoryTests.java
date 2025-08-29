package ru.blog.integration.repositories;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.blog.generators.PostCommentsGenerator;
import ru.blog.models.PostComment;
import ru.blog.services.ports.PostCommentsRepository;

@ActiveProfiles("test")
@DataJpaTest
public class PostCommentsJpaRepositoryTests {

  @Autowired
  private PostCommentsRepository postCommentsRepository;
  @Autowired
  private EntityManager entityManager;

  private PostComment postComment;

  @BeforeEach
  void setup() {
    postCommentsRepository.deleteAll();

    postComment = PostCommentsGenerator.generate().getFirst();

    postCommentsRepository.save(
        new PostComment(UUID.randomUUID().toString(), postComment.getPostId(), postComment.getUserId(),
            postComment.getComment(), null, null));
  }

  @Test
  public void shouldFindPostCommentsCountForPosts() {
    postCommentsRepository.save(
        new PostComment(UUID.randomUUID().toString(), postComment.getPostId(), postComment.getUserId(),
            postComment.getComment(), null, null));
    postCommentsRepository.save(
        new PostComment(UUID.randomUUID().toString(), postComment.getPostId(), postComment.getUserId(),
            postComment.getComment(), null, null));

    var commentsCount = postCommentsRepository.findPostsCommentsCount(List.of(postComment.getPostId()));

    Assertions.assertEquals(3, commentsCount.get(postComment.getPostId()));
  }

}
