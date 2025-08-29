package ru.blog.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "post_comments")
public class PostComment {

  @Id
  private String id;

  @Column(name = "post_id", nullable = false, updatable = false)
  private String postId;

  @Column(name = "user_id", nullable = false, updatable = false)
  private String userId;

  @Column(name = "comment", nullable = false)
  private String comment;

  @Column(name = "created_at", updatable = false)
  private String createdAt;

  @Column(name = "updated_at", updatable = false)
  private String updatedAt;

  public PostComment(String id, String postId, String userId, String comment, String createdAt, String updatedAt) {
    this.id = id;
    this.postId = postId;
    this.userId = userId;
    this.comment = comment;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public PostComment(String id, String postId, String userId, String comment) {
    this.id = id;
    this.postId = postId;
    this.userId = userId;
    this.comment = comment;
  }

  protected PostComment() {
  }

  public static PostComment from(String postId, String userId, String comment) {
    return new PostComment(UUID.randomUUID().toString(), postId, userId, comment);
  }

  public static PostComment from(PostComment postComment, String newComment) {
    return new PostComment(postComment.getId(), postComment.getPostId(), postComment.getUserId(), newComment);
  }

  private void setId(String id) {
    this.id = id;
  }

  private void setPostId(String postId) {
    this.postId = postId;
  }

  private void setUserId(String userId) {
    this.userId = userId;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  private void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  private void setUpdatedAt(String updatedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostComment that = (PostComment) o;
    return Objects.equals(id, that.id) && Objects.equals(postId, that.postId) &&
        Objects.equals(userId, that.userId) && Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, postId, userId, comment);
  }

}
