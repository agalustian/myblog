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
import ru.blog.dto.SearchPostsFilter;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
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

  private boolean searchPostsFilterTagIsNotEmpty(SearchPostsFilter searchPostsFilter) {
    return searchPostsFilter.tag() != null && !searchPostsFilter.tag().isEmpty();
  }

  private List<Object> generateSearchPreviewParameters(SearchPostsFilter searchPostsFilter) {
    List<Object> parameters = new ArrayList<>();

    if (searchPostsFilterTagIsNotEmpty(searchPostsFilter)) {
      parameters.add("%" + searchPostsFilter.tag() + "%");
    }

    return parameters;
  }

  private String generateSearchPreviewWhereFilter(SearchPostsFilter searchPostsFilter) {
    String whereFilter = "";

    if (searchPostsFilterTagIsNotEmpty(searchPostsFilter)) {
      // TODO use array of tags
      whereFilter += "tags like ?";
    }

    return whereFilter.isEmpty() ? "" : " where " + whereFilter;
  }

  @Override
  public Integer searchPostPreviewCount(SearchPostsFilter searchPostsFilter) {
    return jdbcTemplate.query(
        "select count(*) as count from posts " + generateSearchPreviewWhereFilter(searchPostsFilter),
        (rs, rowNumber) -> rs.getInt("count"),
        generateSearchPreviewParameters(searchPostsFilter).toArray()
    ).getFirst();
  }

  @Override
  public List<PostPreview> searchPostPreview(SearchPostsFilter searchPostsFilter, PageRequest pageRequest) {
    String selectQuery = "select posts.* from posts ";
    List<Object> parameters = generateSearchPreviewParameters(searchPostsFilter);

    parameters.add(pageRequest.getPageSize());
    parameters.add((pageRequest.getPageNumber() - 1) * pageRequest.getPageSize());

    return jdbcTemplate.query(
        selectQuery +
            generateSearchPreviewWhereFilter(searchPostsFilter) +
            """
                    order by created_at desc 
                    limit ? 
                    offset ?; 
                """,
        (rs, rowNumber) -> {
          return new PostPreview(
              rs.getString("id"),
              rs.getString("title"),
              rs.getString("text"),
              rs.getString("user_id"),
              mapTagsFromDb(rs.getString("tags")),
              rs.getString("created_at"),
              rs.getString("updated_at")
          );
        },
        parameters.toArray()
    );
  }

  private List<String> mapTagsFromDb(String tags) {
    return Arrays.stream(tags.split(" ")).toList();
  }

}