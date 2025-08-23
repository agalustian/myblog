package ru.blog.unit.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import ru.blog.generators.PostCommentsCountGenerator;
import ru.blog.generators.PostCommentsGenerator;
import ru.blog.generators.PostDetailsGenerator;
import ru.blog.generators.PostLikesCountGenerator;
import ru.blog.generators.PostPreviewsGenerator;
import ru.blog.dto.SearchPostsFilter;
import ru.blog.services.PostsService;
import ru.blog.services.ports.PostCommentsRepository;
import ru.blog.services.ports.PostImagesRepository;
import ru.blog.services.ports.PostLikesRepository;
import ru.blog.services.ports.PostsRepository;

public class PostServiceTests {
  private static final byte[] IMAGE = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};

  private final PostsRepository postsRepository = Mockito.mock(PostsRepository.class);

  private final PostImagesRepository imagesRepository = Mockito.mock(PostImagesRepository.class);

  private final PostLikesRepository postLikesRepository = Mockito.mock(PostLikesRepository.class);

  private final PostCommentsRepository postCommentsRepository = Mockito.mock(PostCommentsRepository.class);

  private final PostsService postsService =
      new PostsService(postsRepository, imagesRepository, postLikesRepository, postCommentsRepository);

  @Nested
  class SavePostTests {

    @Test
    void shouldSavePost() {
      var expectedPostId = "postId";
      var postDetails = PostDetailsGenerator.generate();
      var imageStream = new ByteArrayInputStream(IMAGE);
      Mockito.when(postsRepository.save(postDetails)).thenReturn(expectedPostId);

      postsService.save(postDetails, imageStream);

      verify(postsRepository, times(1)).save(postDetails);
      verify(imagesRepository, times(1)).save(expectedPostId, imageStream);
    }

    @Test
    void shouldThrowErrorOnSavePostWithoutPostDetails() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.save(null, new ByteArrayInputStream(IMAGE));
      });
    }

    @Test
    void shouldThrowErrorOnSavePostWithoutImage() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.save(PostDetailsGenerator.generate(), null);
      });
    }

  }

  @Nested
  class UpdatePostTests {

    @Test
    void shouldUpdatePost() {
      var postDetails = PostDetailsGenerator.generate();
      var imageStream = new ByteArrayInputStream(IMAGE);

      postsService.update(postDetails, imageStream);

      verify(postsRepository, times(1)).update(postDetails);
      verify(imagesRepository, times(1)).updateByPostId(postDetails.getId(), imageStream);
    }

    @Test
    void shouldUpdatePostWithoutImageUpdate() {
      var postDetails = PostDetailsGenerator.generate();

      postsService.update(postDetails, null);

      verify(postsRepository, times(1)).update(postDetails);
      verify(imagesRepository, times(0)).updateByPostId(postDetails.getId(), new ByteArrayInputStream(IMAGE));
    }

    @Test
    void shouldThrowErrorOnUpdatePostWithoutPostDetails() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.update(null, new ByteArrayInputStream(IMAGE));
      });
    }

  }

  @Nested
  class RemovePostTests {

    @Test
    void shouldRemovePost() {
      var postDetails = PostDetailsGenerator.generate();

      postsService.remove(postDetails.getId(), postDetails.getUserId());

      verify(postsRepository, times(1)).remove(postDetails.getId(), postDetails.getUserId());
    }

    @Test
    void shouldThrowErrorOnRemovePostWithoutPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.remove(null, "userId");
      });
    }

    @Test
    void shouldThrowErrorOnRemovePostWithoutUserId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.remove("postId", null);
      });
    }

  }

  @Nested
  class GetPostImageTests {

    @Test
    void shouldGetPostImage() {
      var postDetails = PostDetailsGenerator.generate();

      postsService.getImage(postDetails.getId());

      verify(imagesRepository, times(1)).get(postDetails.getId());
    }

    @Test
    void shouldThrowErrorOnRemovePostWithoutPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.getImage(null);
      });
    }

  }

  @Nested
  class TogglePostLikeTests {

    private static final String POST_ID = "postId";
    private static final String USER_ID = "userId";

    @Test
    void shouldSetPostLike() {
      Mockito.when(postLikesRepository.exists(POST_ID, USER_ID)).thenReturn(false);

      postsService.togglePostLike(POST_ID, USER_ID);

      verify(postLikesRepository, times(1)).exists(POST_ID, USER_ID);
      verify(postLikesRepository, times(1)).set(POST_ID, USER_ID);
    }

    @Test
    void shouldRemovePostLike() {
      Mockito.when(postLikesRepository.exists(POST_ID, USER_ID)).thenReturn(true);

      postsService.togglePostLike(POST_ID, USER_ID);

      verify(postLikesRepository, times(1)).exists(POST_ID, USER_ID);
      verify(postLikesRepository, times(1)).remove(POST_ID, USER_ID);
    }

    @Test
    void shouldThrowErrorOnTogglePostLikeWithoutPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.togglePostLike(null, USER_ID);
      });
    }

    @Test
    void shouldThrowErrorOnTogglePostLikeWithoutUserId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.togglePostLike(POST_ID, null);
      });
    }

  }

  @Nested
  class GetPostDetailsTests {

    @Test
      // TODO fix Assertions after .equals will be implemented in PostDetails class
    void shouldGetPostDetails() {
      var postDetails = PostDetailsGenerator.generate();
      var postComments = PostCommentsGenerator.generate();

      Mockito.when(postsRepository.getPostDetailsById(postDetails.getId())).thenReturn(postDetails);
      Mockito.when(postCommentsRepository.getPostCommentsByPostId(postDetails.getId())).thenReturn(postComments);

      var foundPostDetails = postsService.getPostDetailsById(postDetails.getId());

      verify(postsRepository, times(1)).getPostDetailsById(postDetails.getId());
      verify(postCommentsRepository, times(1)).getPostCommentsByPostId(postDetails.getId());

      Assertions.assertEquals(postDetails.getId(), foundPostDetails.getId());
      Assertions.assertEquals(postDetails.getTitle(), foundPostDetails.getTitle());
      Assertions.assertEquals(postComments.size(), foundPostDetails.getComments().size());
      Assertions.assertEquals(postComments.getFirst().getId(), foundPostDetails.getComments().getFirst().getId());
      Assertions.assertEquals(postComments.getLast().getId(), foundPostDetails.getComments().getLast().getId());
    }

    @Test
    void shouldGetPostDetailsWithoutComments() {
      var postDetails = PostDetailsGenerator.generate();

      Mockito.when(postsRepository.getPostDetailsById(postDetails.getId())).thenReturn(postDetails);
      Mockito.when(postCommentsRepository.getPostCommentsByPostId(postDetails.getId())).thenReturn(new ArrayList<>());

      var foundPostDetails = postsService.getPostDetailsById(postDetails.getId());

      Assertions.assertEquals(postDetails.getId(), foundPostDetails.getId());
      Assertions.assertEquals(postDetails.getTitle(), foundPostDetails.getTitle());
      Assertions.assertEquals(0, foundPostDetails.getComments().size());
    }

    @Test
    void shouldThrowErrorOnGettingPostDetailsWithoutPostId() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.getPostDetailsById(null);
      });
    }

  }

  @Nested
  class SearchPostPreviewTests {

    @Test
      // TODO fix Assertions after .equals will be implemented in PostDetails class
    void shouldGetPostPreviews() {
      var postPreviews = PostPreviewsGenerator.generate();
      var searchPostPreviewFilters = new SearchPostsFilter("");
      var pageRequest = PageRequest.of(1, 10);
      var postIds = List.of(postPreviews.getFirst().getId(), postPreviews.getLast().getId());
      var commentsCount = PostCommentsCountGenerator.generate(postIds);
      var likesCount = PostLikesCountGenerator.generate(postIds);

      Mockito.when(postsRepository.searchPostPreview(searchPostPreviewFilters, pageRequest)).thenReturn(postPreviews);
      Mockito.when(postsRepository.findPostsLikesCount(postIds)).thenReturn(likesCount);
      Mockito.when(postCommentsRepository.findPostsCommentsCount(postIds)).thenReturn(commentsCount);

      var foundPostPreviews = postsService.searchPostPreview(searchPostPreviewFilters, pageRequest);

      verify(postsRepository, times(1)).searchPostPreview(searchPostPreviewFilters, pageRequest);
      verify(postsRepository, times(1)).findPostsLikesCount(postIds);
      verify(postCommentsRepository, times(1)).findPostsCommentsCount(postIds);

      Assertions.assertEquals(postPreviews.size(), foundPostPreviews.size());
      Assertions.assertEquals(postPreviews.getFirst().getId(), foundPostPreviews.getFirst().getId());

      Assertions.assertEquals(commentsCount.get(postIds.getFirst()), foundPostPreviews.getFirst().getCommentsCount());
      Assertions.assertEquals(likesCount.get(postIds.getFirst()), foundPostPreviews.getFirst().getLikesCount());
      Assertions.assertEquals(commentsCount.get(postIds.getLast()), foundPostPreviews.getLast().getCommentsCount());
      Assertions.assertEquals(likesCount.get(postIds.getLast()), foundPostPreviews.getLast().getLikesCount());
    }

    @Test
    void shouldGetPostPreviewsWithoutLikesAndComments() {
      var postPreviews = PostPreviewsGenerator.generate();
      var searchPostPreviewFilters = new SearchPostsFilter("");
      var pageRequest = PageRequest.of(1, 10);
      var postIds = List.of(postPreviews.getFirst().getId(), postPreviews.getLast().getId());

      Mockito.when(postsRepository.searchPostPreview(searchPostPreviewFilters, pageRequest)).thenReturn(postPreviews);
      Mockito.when(postsRepository.findPostsLikesCount(postIds)).thenReturn(new HashMap<>());
      Mockito.when(postCommentsRepository.findPostsCommentsCount(postIds)).thenReturn(new HashMap<>());

      var foundPostPreviews = postsService.searchPostPreview(searchPostPreviewFilters, pageRequest);

      Assertions.assertEquals(0, foundPostPreviews.getFirst().getCommentsCount());
      Assertions.assertEquals(0, foundPostPreviews.getFirst().getLikesCount());
      Assertions.assertEquals(0, foundPostPreviews.getLast().getCommentsCount());
      Assertions.assertEquals(0, foundPostPreviews.getLast().getLikesCount());
    }

    @Test
    void shouldThrowErrorOnGettingPostDetailsWithoutSearchPostFilter() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.searchPostPreview(null, PageRequest.of(1, 10));
      });
    }

    @Test
    void shouldThrowErrorOnGettingPostDetailsWithoutPageRequest() {
      Assertions.assertThrows(IllegalArgumentException.class, () -> {
        postsService.searchPostPreview(new SearchPostsFilter(""), null);
      });
    }

  }

}
