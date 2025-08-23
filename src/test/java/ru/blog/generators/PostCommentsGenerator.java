package ru.blog.generators;

import java.util.List;
import ru.blog.models.PostComment;

public class PostCommentsGenerator {
  public static List<PostComment> generate() {
    var postComment = new PostComment("commentId1", "test-id", "userId1", "text1", null, null);
    var anotherPostComment = new PostComment("commentId2", "test-id2", "userId2", "text2", null, null);

    return List.of(postComment, anotherPostComment);
  }
}
