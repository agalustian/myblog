package ru.blog.services;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.blog.models.PostComment;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
import ru.blog.dto.SearchPostsFilter;
import ru.blog.services.ports.PostCommentsRepository;
import ru.blog.services.ports.PostImagesRepository;
import ru.blog.services.ports.PostLikesRepository;
import ru.blog.services.ports.PostsRepository;

@Service
public class PostsService {

  private final PostsRepository postsRepository;

  private final PostImagesRepository imagesRepository;

  private final PostLikesRepository postLikesRepository;

  private final PostCommentsRepository postCommentsRepository;

  public PostsService(PostsRepository postsRepository, PostImagesRepository imagesRepository,
                      PostLikesRepository postLikesRepository, PostCommentsRepository postCommentsRepository) {
    this.postsRepository = postsRepository;
    this.imagesRepository = imagesRepository;
    this.postLikesRepository = postLikesRepository;
    this.postCommentsRepository = postCommentsRepository;
  }

  public List<PostPreview> searchPostPreview(SearchPostsFilter searchPostsFilter, PageRequest pageRequest) {
    Assert.notNull(searchPostsFilter, "Search posts filter is required for searching post previews");
    Assert.notNull(pageRequest, "Page request is required for searching post previews");

    List<PostPreview> postPreviews = postsRepository.searchPostPreview(searchPostsFilter, pageRequest);

    List<String> postPreviewIds = postPreviews.stream().map(PostPreview::getId).toList();

    Map<String, Integer> likes = postsRepository.findPostsLikesCount(postPreviewIds);
    Map<String, Integer> comments = postCommentsRepository.findPostsCommentsCount(postPreviewIds);

    for (PostPreview postPreview: postPreviews) {
      var likesCount = likes.getOrDefault(postPreview.getId(), 0);
      var commentsCount = comments.getOrDefault(postPreview.getId(), 0);

      postPreview.setLikesCount(likesCount);
      postPreview.setCommentsCount(commentsCount);
    }

    return postPreviews;
  }

  public PostDetails getPostDetailsById(final String postId) {
    Assert.notNull(postId, "Post id is required for getting posts details");

    PostDetails postDetails = postsRepository.getPostDetailsById(postId);
    List<PostComment> postComments = postCommentsRepository.getComments(postId);

    postDetails.setComments(postComments);

    return postDetails;
  }

  @Transactional
  public void save(PostDetails postDetails, InputStream image) {
    Assert.notNull(postDetails, "Post details is required for saving");
    Assert.notNull(image, "Image is required for saving");

    var id = postsRepository.save(postDetails);
    imagesRepository.save(id, image);
  }

  @Transactional
  public void update(PostDetails postDetails, InputStream image) {
    Assert.notNull(postDetails, "Post details is required for update");

    postsRepository.update(postDetails);
    if (image != null) {
      imagesRepository.updateByPostId(postDetails.getId(), image);
    }
  }

  public void remove(final String postId, String userId) {
    Assert.notNull(postId, "Post id is required for removing");
    Assert.notNull(userId, "User id is required for removing");

    postsRepository.remove(postId, userId);
  }

  public byte[] getImage(final String postId) {
    Assert.notNull(postId, "Post id is required for getting image");

    return imagesRepository.get(postId);
  }

  public void togglePostLike(final String postId, String userId) {
    Assert.notNull(postId, "Post id is required for toggle post like");
    Assert.notNull(userId, "User id is required for toggle post like");

    var likeExists = postLikesRepository.exists(postId, userId);

    if (likeExists) {
      postLikesRepository.remove(postId, userId);
      return;
    }

    postLikesRepository.set(postId, userId);
  }

}
