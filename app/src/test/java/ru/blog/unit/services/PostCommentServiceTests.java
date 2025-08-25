package ru.blog.unit.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.blog.services.PostCommentsService;
import ru.blog.services.ports.PostCommentsRepository;

public class PostCommentServiceTests {
  private final PostCommentsRepository postCommentsRepository = Mockito.mock(PostCommentsRepository.class);

  private final PostCommentsService postCommentsService = new PostCommentsService(postCommentsRepository);

  private static final String COMMENT_ID = "commentId";
  private static final String POST_ID = "postId";
  private static final String USER_ID = "userId";

  @Nested
  class SaveCommentTests {

    @Test
    void shouldSavePostComment() {
      postCommentsService.save(POST_ID, USER_ID, "testComment");

      verify(postCommentsRepository, times(1)).save(POST_ID, USER_ID, "testComment");
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.save(null, USER_ID, "testComment");
      });
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyUserId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.save(POST_ID, null, "testComment");
      });
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyComment() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.save(POST_ID, USER_ID, null);
      });
    }

  }

  @Nested
  class UpdateCommentTests {

    @Test
    void shouldSavePostComment() {
      postCommentsService.update(COMMENT_ID, USER_ID, "testComment");

      verify(postCommentsRepository, times(1)).update(COMMENT_ID, USER_ID, "testComment");
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.update(null, USER_ID, "testComment");
      });
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyUserId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.update(COMMENT_ID, null, "testComment");
      });
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyComment() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.update(COMMENT_ID, USER_ID, null);
      });
    }

  }

  @Nested
  class RemoveCommentTests {

    @Test
    void shouldSavePostComment() {
      postCommentsService.remove(COMMENT_ID, USER_ID);

      verify(postCommentsRepository, times(1)).remove(COMMENT_ID, USER_ID);
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.remove(null, USER_ID);
      });
    }

    @Test
    void shouldThrowAssertionErrorOnEmptyUserId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postCommentsService.remove(COMMENT_ID, null);
      });
    }

  }

}
