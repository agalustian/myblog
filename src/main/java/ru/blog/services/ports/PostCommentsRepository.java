package ru.blog.services.ports;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.blog.models.PostComment;
import ru.blog.repositories.PostCommentsRepositoryCustom;

public interface PostCommentsRepository extends PostCommentsRepositoryCustom, JpaRepository<PostComment, String> {
  void removePostCommentByIdAndUserId(String id, String userId);

  List<PostComment> getPostCommentsByPostId(String postId);
}
