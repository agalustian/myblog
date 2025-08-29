package ru.blog.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class PostPreview extends Post {

  private Integer commentsCount;

  public PostPreview(String id, String title, String text, String userId, List<String> tags, String createdAt, String updatedAt) {
    super(id, title, text, userId, tags, createdAt, updatedAt);
  }

  protected String prepareText(final String text) {
    try {
      BufferedReader reader = new BufferedReader(new StringReader(text));
      StringWriter sw = new StringWriter();
      PrintWriter writer = new PrintWriter(sw);

      for (int i = 0; i < 3; i++) {
        var line = reader.readLine();

        if (line != null) {
          writer.println(line);
        }
      }

      reader.close();
      writer.close();

      return sw.toString();
    } catch (IOException e) {
      return "";
    }
  }

  public void setCommentsCount(int commentsCount) {
    this.commentsCount = commentsCount;
  }

  public Integer getCommentsCount() {
    return commentsCount;
  }

}
