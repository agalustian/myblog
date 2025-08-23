package ru.blog.services.ports;

import java.util.List;
import java.util.Map;
import ru.blog.models.PostComment;

public interface PostCommentsRepository {
  void save(String postId, String userId, String comment);

  void update(String commentId, String userId, String comment);

  List<PostComment> getComments(String postId);

  void remove(String postId, String userId);

  Map<String, Integer> findPostsCommentsCount(final List<String> postId);
}
