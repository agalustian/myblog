package ru.blog.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("controller-test")
public class PostCommentsControllerTests {
  private final MockMvc mockMvc;

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  PostCommentsControllerTests(final MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
  }

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("DELETE FROM posts");
    jdbcTemplate.execute("DELETE FROM post_comments");
    jdbcTemplate.execute("""
        insert into posts (id, title, text, user_id, tags)
                values (
                    '726f8caf-366a-4f2d-a5a4-b7ebd4310e9c',
                        'Awesome post 1',
                        'BUILD SUCCESSFUL in 659ms
                   3 actionable tasks: 2 executed, 1 up-to-date
                   Configuration cache entry reused
                   14:51:56: Execution finished',
                        'Иванов',
                        'awesome first')
        """);
    jdbcTemplate.execute("""
        insert into post_comments (id, post_id, user_id, comment)
        values ('726f8caf-366a-4f2d-a5a4-b7ebd4390e9c',
                '726f8caf-366a-4f2d-a5a4-b7ebd4310e9c',
                'Иванов',
                'Awesome great comment!')
        """);

  }

  @Test
  void shouldSavePostComment() throws Exception {
    mockMvc.perform(post("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/comments").param("text", "test-text"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

  @Test
  void shouldEditPostComment() throws Exception {
    mockMvc.perform(
            post("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/comments/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/edit").param(
                "text", "new-test-text"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

  @Test
  void shouldDeletePostComment() throws Exception {
    mockMvc.perform(
            post("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/comments/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

}

