package ru.blog.unit.models;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.blog.models.PostPreview;

public class PostPreviewTests {
  @Test
  void shouldPrepareText() {
    var text = """
        test
        testestsets
        asdsadasd
        test""";


    var expectedText = """
        test
        testestsets
        asdsadasd
        """;

    var postPreview = new PostPreview(null, null, text, null, List.of("first", "second"), null, null);

    Assertions.assertEquals(expectedText, postPreview.getText());
  }
}
