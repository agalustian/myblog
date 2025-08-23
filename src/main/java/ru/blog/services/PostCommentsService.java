package ru.blog.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.blog.models.PostComment;
import ru.blog.services.ports.PostCommentsRepository;

@Service
public class PostCommentsService {
  private final PostCommentsRepository postCommentsRepository;

  public PostCommentsService(final PostCommentsRepository postCommentsRepository) {
    this.postCommentsRepository = postCommentsRepository;
  }

  public void save(final PostComment postComment) {
    Assert.notNull(postComment, "PostComment is required for comment creation");

    postCommentsRepository.save(postComment);
  }

  @Transactional
  public void update(final String commentId, String userId, String comment) {
    Assert.notNull(commentId, "Comment id is required for comment update");
    Assert.notNull(userId, "User id is required for comment update");
    Assert.notNull(comment, "Comment is required for update");

    postCommentsRepository.patchComment(commentId, userId, comment);
  }

  @Transactional
  public void remove(final String commentId, String userId) {
    Assert.notNull(commentId, "Comment id is required for comment removing");
    Assert.notNull(userId, "User id is required for comment removing");

    postCommentsRepository.removePostCommentByIdAndUserId(commentId, userId);
  }

}
