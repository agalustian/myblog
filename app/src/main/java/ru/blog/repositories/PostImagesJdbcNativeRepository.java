package ru.blog.repositories;

import java.io.InputStream;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.services.ports.PostImagesRepository;

@Repository
public class PostImagesJdbcNativeRepository implements PostImagesRepository {

  private final JdbcTemplate jdbcTemplate;

  public PostImagesJdbcNativeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void save(String id, InputStream image) {
    jdbcTemplate.update(
        """
              insert into post_images(id, image) 
              values(?, ?);
            """,
        id,
        image
    );
  }

  @Override
  public void updateByPostId(String id, InputStream image) {
    jdbcTemplate.update(
        """
              update post_images
                  set image = ?
              where id = ?;
            """,
        image,
        id
    );
  }

  @Override
  public byte[] get(String id) {
    var images = jdbcTemplate.query(
        "select * from post_images where id = ?;",
        (rs, rowNum) -> rs.getBytes("image"),
        id
    );

    return images.getFirst();
  }

}
