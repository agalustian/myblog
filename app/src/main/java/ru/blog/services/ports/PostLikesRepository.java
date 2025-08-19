package ru.blog.services.ports;

public interface PostLikesRepository {
  void set(String postId, String userId);

  boolean exists(String postId, String userId);

  void remove(String postId, String userId);
}
