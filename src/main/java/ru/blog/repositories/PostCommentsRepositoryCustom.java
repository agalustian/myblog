package ru.blog.repositories;

import java.util.List;
import java.util.Map;

public interface PostCommentsRepositoryCustom {
  Map<String, Integer> findPostsCommentsCount(final List<String> postIds);

  void patchComment(final String commentId, String userId, String comment);
}
