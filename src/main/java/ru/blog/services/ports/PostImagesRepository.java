package ru.blog.services.ports;

import java.io.InputStream;

public interface PostImagesRepository {
  void save(String id, InputStream image);

  void updateByPostId(String id, InputStream image);

  byte[] get(String id);
}
