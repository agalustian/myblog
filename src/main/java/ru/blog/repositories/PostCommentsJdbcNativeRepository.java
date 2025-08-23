package ru.blog.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.models.PostComment;
import ru.blog.services.ports.PostCommentsRepository;

@Repository
public class PostCommentsJdbcNativeRepository implements PostCommentsRepository {

  private final JdbcTemplate jdbcTemplate;

  public PostCommentsJdbcNativeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void save(final String postId, String userId, String comment) {
    jdbcTemplate.update(
        """
              insert into post_comments(id, post_id, user_id, comment) 
              values(?, ?, ?, ?);
            """,
        UUID.randomUUID(),
        postId,
        userId,
        comment
    );
  }

  @Override
  public void update(final String commentId, String userId, String comment) {
    jdbcTemplate.update(
        """
              update post_comments 
              set comment = ?, updated_at = now()
              where id = ? and user_id = ?;
            """,
        comment,
        commentId,
        userId
    );
  }

  @Override
  public void remove(final String commentId, String userId) {
    jdbcTemplate.update(
        """
              delete from post_comments 
              where id = ? and user_id = ?;
            """,
        commentId,
        userId
    );
  }

  @Override
  public List<PostComment> getComments(String id) {
    return jdbcTemplate
        .query("select * from post_comments p where post_id = ? order by created_at desc;",
            (rs, rowNumber) -> new PostComment(
                rs.getString("id"),
                rs.getString("post_id"),
                rs.getString("user_id"),
                rs.getString("comment"),
                rs.getString("created_at"),
                rs.getString("updated_at")
            ),
            id);
  }

  @Override
  public Map<String, Integer> findPostsCommentsCount(final List<String> postIds) {
    var result = new HashMap<String, Integer>();

    try {
      jdbcTemplate.query(
          "select post_id, count(*) as comments_count from post_comments p where post_id = ANY(?) group by post_id;",
          (rs, rowNumber) -> {
            result.put(rs.getString("post_id"), rs.getInt("comments_count"));
            return result;
          },
          jdbcTemplate.getDataSource().getConnection().createArrayOf("string", postIds.toArray())
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return result;
  }

}