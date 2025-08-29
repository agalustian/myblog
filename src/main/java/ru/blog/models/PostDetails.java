package ru.blog.models;

import java.util.Arrays;
import java.util.List;

public class PostDetails extends Post {

  private List<PostComment> comments;

  public PostDetails(String id, String title, String text, String userId, List<String> tags, String createdAt, String updatedAt) {
    super(id, title, text, userId, tags, createdAt, updatedAt);
  }

  protected String prepareText(String text) {
    return text.replace("\n", "\n\n");
  }

  public List<PostComment> getComments() {
    return comments;
  }

  public void setComments(List<PostComment> comments) {
    this.comments = comments;
  }
}
