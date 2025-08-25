package ru.blog.generators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostCommentsCountGenerator {

  public static Map<String, Integer> generate(final List<String> postIds) {
    var commentsCount = new HashMap<String, Integer>();
    commentsCount.put(postIds.getFirst(), 3);
    commentsCount.put(postIds.getLast(), 4);

    return commentsCount;
  }

}
