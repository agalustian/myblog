package ru.blog.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.services.ports.PostLikesRepository;

@Repository
public class PostsLikesJdbcNativeRepository implements PostLikesRepository {

  private final JdbcTemplate jdbcTemplate;

  public PostsLikesJdbcNativeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void set(final String postId, String userId) {
    jdbcTemplate.update(
        """
              insert into post_likes(post_id, user_id) 
              values(?, ?);
            """,
        postId,
        userId
    );
  }

  @Override
  public boolean exists(final String postId, String userId) {
    var result = jdbcTemplate.query(
        """
              select * from post_likes
              where post_id = ? and user_id = ?
              limit 1;
            """,
        (rs, rowNum) -> true,
        postId,
        userId
    );

    return !result.isEmpty() && result.getFirst() != null && result.getFirst() == Boolean.TRUE;
  }

  @Override
  public void remove(final String postId, String userId) {
    jdbcTemplate.update(
        """
              delete from post_likes 
              where post_id = ? and user_id = ?;
            """,
        postId,
        userId
    );
  }

}