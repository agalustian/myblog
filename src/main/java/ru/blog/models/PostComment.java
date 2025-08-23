package ru.blog.models;

public class PostComment {
  private final String id;

  private final String postId;

  private final String userId;

  private final String comment;

  private final String createdAt;

  private final String updatedAt;

  public PostComment(String id, String postId, String userId, String comment, String createdAt, String updatedAt) {
    this.id = id;
    this.postId = postId;
    this.userId = userId;
    this.comment = comment;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public String getId() {
    return id;
  }

  public String getPostId() {
    return postId;
  }

  public String getUserId() {
    return userId;
  }

  public String getComment() {
    return comment;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

}
