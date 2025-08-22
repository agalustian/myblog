package ru.blog.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
import ru.blog.dto.SearchPostsFilter;
import ru.blog.services.ports.PostsRepository;

@Repository
public class PostsJdbcNativeRepository implements PostsRepository {

  private final JdbcTemplate jdbcTemplate;

  public PostsJdbcNativeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public String save(PostDetails postDetails) {
    var postId = UUID.randomUUID().toString();

    jdbcTemplate.update(
        """
              insert into posts(id, title, text, user_id, tags) 
              values(?, ?, ?, ?, ?);
            """,
        postId,
        postDetails.getTitle(),
        postDetails.getText(),
        postDetails.getUserId(),
        postDetails.getTagsAsText()
    );

    return postId;
  }

  @Override
  public void update(PostDetails postDetails) {
    jdbcTemplate.update(
        """
              update posts 
              set title = ?, text = ?, tags = ?, updated_at = now()
              where id = ?;
            """,
        postDetails.getTitle(),
        postDetails.getText(),
        postDetails.getTagsAsText(),
        postDetails.getId()
    );
  }

  @Override
  public void remove(final String postId, String userId) {
    jdbcTemplate.update(
        """
              delete from posts 
              where id = ? and user_id = ?;
            """,
        postId,
        userId
    );
  }

  @Override
  public PostDetails getPostDetailsById(final String postId) {
    var postsDetails = jdbcTemplate
        .query(
            "select p.*, (select count(*) from post_likes where post_id = ?) as likes_count from posts p where id = ?;",
            (rs, rowNumber) -> {
              var postDetails = new PostDetails(
                  rs.getString("id"),
                  rs.getString("title"),
                  rs.getString("text"),
                  rs.getString("user_id"),
                  mapTagsFromDb(rs.getString("tags")),
                  rs.getString("created_at"),
                  rs.getString("updated_at")
              );
              postDetails.setLikesCount(rs.getInt("likes_count"));

              return postDetails;
            },
            postId,
            postId);

    if (postsDetails.isEmpty()) {
      return null;
    }

    return postsDetails.getFirst();
  }

  @Override
  public Map<String, Integer> findPostsLikesCount(final List<String> postIds) {
    var result = new HashMap<String, Integer>();

    try {
      jdbcTemplate.query(
          "select post_id, count(*) as likes_count from post_likes p where post_id = ANY(?) group by post_id;",
          (rs, rowNumber) -> {
            result.put(rs.getString("post_id"), rs.getInt("likes_count"));
            return result;
          },
          jdbcTemplate.getDataSource().getConnection().createArrayOf("string", postIds.toArray())
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  @Override
  public List<PostPreview> searchPostPreview(SearchPostsFilter searchPostsFilter, PageRequest pageRequest) {
    String selectQuery = "select posts.*, COUNT(*) OVER () AS total_rows from posts ";
    String whereFilter = "";
    List<Object> parameters = new ArrayList<>();

    if (searchPostsFilter.tag() != null && !searchPostsFilter.tag().isEmpty()) {
      // TODO use array of tags
      whereFilter += "tags like ?";
      parameters.add("%" + searchPostsFilter.tag() + "%");
    }

    parameters.add(pageRequest.getPageSize());
    parameters.add((pageRequest.getPageNumber() - 1) * pageRequest.getPageSize());

    return jdbcTemplate.query(
        selectQuery +
            (whereFilter.isEmpty() ? "" : " where " + whereFilter) +
            """
                    order by created_at desc 
                    limit ? 
                    offset ?; 
                """,
        (rs, rowNumber) -> {
          var postPreview = new PostPreview(
              rs.getString("id"),
              rs.getString("title"),
              rs.getString("text"),
              rs.getString("user_id"),
              mapTagsFromDb(rs.getString("tags")),
              rs.getString("created_at"),
              rs.getString("updated_at")
          );

          postPreview.setTotalCount(rs.getInt("total_rows"));

          return postPreview;
        },
        parameters.toArray()
    );
  }

  private List<String> mapTagsFromDb(String tags) {
    return Arrays.stream(tags.split(" ")).toList();
  }

}