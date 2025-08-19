package ru.blog.generators;

import java.util.List;
import ru.blog.models.PostPreview;

public class PostPreviewsGenerator {
  public static List<PostPreview> generate() {
    var postPreview = new PostPreview("test-id", "title", "text", "userId", List.of("test"), null, null);
    var anotherPostPreview = new PostPreview("test-id2", "title", "text", "userId2", List.of("test"), null, null);

    return List.of(postPreview, anotherPostPreview);
  }
}
