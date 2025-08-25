package ru.blog.generators;

import java.util.List;
import ru.blog.models.PostDetails;

public class PostDetailsGenerator {
  public static PostDetails generate() {
    var postDetails = new PostDetails("test-id", "title", "text", "userId", List.of("test"), null, null);

    postDetails.setLikesCount(0);

    return postDetails;
  }

  public static PostDetails generate(final String tag) {
    var postDetails = new PostDetails("test-id", "title", "text", "userId", List.of(tag), null, null);

    postDetails.setLikesCount(0);

    return postDetails;
  }
}
