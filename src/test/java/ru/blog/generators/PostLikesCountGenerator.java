package ru.blog.generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostLikesCountGenerator {

  public static Map<String, Integer> generate(final List<String> postIds) {
    var likesCount = new HashMap<java.lang.String, java.lang.Integer>();
    likesCount.put(postIds.getFirst(), 5);
    likesCount.put(postIds.getLast(), 7);

    return likesCount;
  }

}
