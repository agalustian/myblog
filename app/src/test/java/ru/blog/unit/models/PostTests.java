package ru.blog.unit.models;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.blog.models.Post;

public class PostTests {
  class TestPost extends Post {
    public TestPost(String id, String title, String text, String userId, List<String> tags, String createdAt,
                       String updatedAt) {
      super(id, title, text, userId, tags, createdAt, updatedAt);
    }
  }

  @Test
  void shouldGetTagsAsText() {
    var expectedTagsText = "first second";

    var post = new TestPost(null, null, null, null, List.of("first", "second"), null, null);

    Assertions.assertEquals(expectedTagsText, post.getTagsAsText());
  }
}
