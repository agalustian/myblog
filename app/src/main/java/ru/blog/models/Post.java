package ru.blog.models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Post {

  private final String id;

  private final String title;

  private final String text;

  private final String createdAt;

  private final String updatedAt;

  private final String userId;

  private final List<String> tags;

  private Integer likesCount;

  protected Post(String id, String title, String text, String userId, List<String> tags, String createdAt,
                 String updatedAt) {
    this.id = id;
    this.title = title;
    this.text = prepareText(text);
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.userId = userId;
    this.tags = tags;
  }

  protected String prepareText(String text) {
    return text;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public String getUserId() {
    return userId;
  }

  public List<String> getTags() {
    return tags;
  }

  public Integer getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(int likesCount) {
    this.likesCount = likesCount;
  }

  public String getTagsAsText() {
    return tags.stream().collect(Collectors.joining(" "));
  }

  public List<String> getTextParts() {
    return Arrays.stream(getText().split("\n\n")).toList();
  }

}
