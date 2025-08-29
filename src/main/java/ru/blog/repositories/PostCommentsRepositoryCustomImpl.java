package ru.blog.repositories;

import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class PostCommentsRepositoryCustomImpl implements PostCommentsRepositoryCustom {
  private final EntityManager entityManager;

  public PostCommentsRepositoryCustomImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Map<String, Integer> findPostsCommentsCount(final List<String> postIds) {
    var query = entityManager.createQuery("""
            select postId, count(*) as comments_count
            from PostComment p
            where postId IN (:postIds)
            group by postId
        """);

    query.setParameter("postIds", postIds);

    var queryResultList = (List<Object[]>) query.getResultList();
    var postCommentCounts = new HashMap<String, Integer>();

    for (Object[] row : queryResultList) {
      postCommentCounts.put((String) row[0], ((Number) row[1]).intValue());
    }

    return postCommentCounts;
  }
}
