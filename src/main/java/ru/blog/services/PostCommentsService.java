package ru.blog.services;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.blog.services.ports.PostCommentsRepository;

@Service
public class PostCommentsService {
  private final PostCommentsRepository postCommentsRepository;

  public PostCommentsService(final PostCommentsRepository postCommentsRepository) {
    this.postCommentsRepository = postCommentsRepository;
  }

  public void save(final String postId, String userId, String comment) {
    Assert.notNull(postId, "Post id is required for comment creation");
    Assert.notNull(userId, "User id is required for comment creation");
    Assert.notNull(comment, "Comment is required for creation");

    postCommentsRepository.save(postId, userId, comment);
  }

  public void update(final String commentId, String userId, String comment) {
    Assert.notNull(commentId, "Comment id is required for comment update");
    Assert.notNull(userId, "User id is required for comment update");
    Assert.notNull(comment, "Comment is required for update");

    postCommentsRepository.update(commentId, userId, comment);
  }

  public void remove(final String commentId, String userId) {
    Assert.notNull(commentId, "Comment id is required for comment removing");
    Assert.notNull(userId, "User id is required for comment removing");

    postCommentsRepository.remove(commentId, userId);
  }

}
