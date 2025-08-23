package ru.blog.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource(locations = "classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("controller-test")
public class PostsControllerTests {

  private final JdbcTemplate jdbcTemplate;

  private final MockMvc mockMvc;

  @Autowired
  PostsControllerTests(final MockMvc mockMvc, JdbcTemplate jdbcTemplate) {
    this.mockMvc = mockMvc;
    this.jdbcTemplate = jdbcTemplate;
  }

  @BeforeEach
  void setUp() {
    jdbcTemplate.execute("DELETE FROM posts");
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

  }

  @Test
  void shouldReturnPostDetails() throws Exception {
    mockMvc.perform(get("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"))
        .andExpect(status().isOk())
        .andExpect(view().name("post"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("postDetails"));
  }

  @Test
  void shouldReturnPostPreviews() throws Exception {
    mockMvc.perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(view().name("posts"))
        .andExpect(model().attributeExists("posts"))
        .andExpect(model().attributeExists("paging"));
  }

  @Test
  void shouldReturnAddPost() throws Exception {
    mockMvc.perform(get("/posts/add"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(view().name("add-post"));
  }

  @Test
  void shouldGetPostForEdit() throws Exception {
    mockMvc.perform(get("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/edit"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(view().name("add-post"))
        .andExpect(model().attributeExists("postDetails"));
  }

  @Test
  void shouldEditPost() throws Exception {
    byte[] image = {108};
    mockMvc.perform(multipart("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c")
            .file(new MockMultipartFile("image", image))
            .param("text", "test1")
            .param("title", "test2")
            .param("tags", "test3")
            .param("image", "test-image")
            .contentType("multipart/form-data")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

  @Test
  void shouldSavePost() throws Exception {
    byte[] image = {108};
    mockMvc.perform(multipart("/posts")
            .file(new MockMultipartFile("image", image))
            .param("text", "test1")
            .param("title", "test2")
            .param("tags", "test3")
            .param("image", "test-image")
            .contentType("multipart/form-data")
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts"));
  }

  @Test
  void shouldDeletePost() throws Exception {
    mockMvc.perform(post("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts"));
  }

  @Test
  void shouldToggleLike() throws Exception {
    mockMvc.perform(get("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/like?userId=CURRENT_USER"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

  @Test
  void shouldSetLikeAndRemoveLikeForSpecifiedPost() throws Exception {
    mockMvc.perform(get("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/like?userId=CURRENT_USER"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));

    mockMvc.perform(get("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c/like?userId=CURRENT_USER"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/posts/726f8caf-366a-4f2d-a5a4-b7ebd4310e9c"));
  }

}

