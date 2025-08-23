package ru.blog.unit.models;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.blog.models.PostDetails;

public class PostDetailsTest {

  @Test
  void shouldPrepareText() {
    var text = """
        test
        testestsets
        asdsadasd""";

    var expectedText = """
        test
        
        testestsets

        asdsadasd""";

    var postDetails = new PostDetails(null, null, text, null, List.of("first", "second"), null, null);

    Assertions.assertEquals(expectedText, postDetails.getText());
  }

}
