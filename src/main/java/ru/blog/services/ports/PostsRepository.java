package ru.blog.services.ports;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
import ru.blog.dto.SearchPostsFilter;

public interface PostsRepository {
  String save(PostDetails postDetails);

  void update(PostDetails postDetails);

  PostDetails getPostDetailsById(String id);

  List<PostPreview> searchPostPreview(SearchPostsFilter searchPostsFilter, PageRequest pageRequest);

  Integer searchPostPreviewCount(SearchPostsFilter searchPostsFilter);

  void remove(String postId, String userId);

  Map<String, Integer> findPostsLikesCount(List<String> postIds);
}
