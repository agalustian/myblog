package ru.blog.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.models.Paging;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
import ru.blog.models.SearchPostsFilter;
import ru.blog.services.PostsService;

@Controller
@RequestMapping("/posts")
public class PostsController {
  PostsService postsService;

  PostsController(final PostsService postsService) {
    this.postsService = postsService;
  }

  @PostMapping()
  public String save(@RequestParam("text") String text, @RequestParam("title") String title,
                     @RequestParam("tags") String tags,
                     @RequestParam("image")
                     MultipartFile image)
      throws IOException {
    try {
      var imageStream = image.getInputStream();

      var postDetails =
          new PostDetails(null, title, text, "CURRENT_USER", Arrays.stream(tags.split(" ")).toList(), null, null);
      postsService.save(postDetails, imageStream);
    } catch (IOException e) {
      throw e;
    }

    return "redirect:/posts";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable(value = "id") String postId, @RequestParam("text") String text,
                       @RequestParam("title") String title, @RequestParam("tags") String tags,
                       @RequestParam("image")
                       MultipartFile image)
      throws IOException {
    try {
      var imageStream = image.getInputStream();

      var postDetails =
          new PostDetails(postId, title, text, "CURRENT_USER", Arrays.stream(tags.split(" ")).toList(), null, null);
      postsService.update(postDetails, image.isEmpty() ? null : imageStream);
    } catch (IOException e) {
      throw e;
    }

    return "redirect:/posts/" + postId;
  }

  @GetMapping("/add")
  public String getPostAddTemplate() {
    return "add-post";
  }

  @GetMapping("/{id}/edit")
  public String getPostForEditing(@PathVariable(name = "id") String postId, final Model model) {
    PostDetails postDetails = postsService.getPostDetailsById(postId);

    model.addAttribute("postDetails", postDetails);

    return "add-post";
  }

  @GetMapping("/{id}")
  public String getPostDetailsById(@PathVariable(name = "id") String postId, final Model model) {
    PostDetails postDetails = postsService.getPostDetailsById(postId);

    model.addAttribute("postDetails", postDetails);

    return "post";
  }

  @PostMapping("/{id}/delete")
  public String removePost(@PathVariable(name = "id") String postId) {
    postsService.remove(postId, "CURRENT_USER");

    return "redirect:/posts";
  }

  @GetMapping
  public String searchPostPreview(@RequestParam(value = "search", required = false) String searchByTag,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "1")
                                  Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "5")
                                  Integer pageSize,
                                  final Model model) {
    var pageRequest = PageRequest.of(pageNumber, pageSize);

    List<PostPreview> postPreviews = postsService.searchPostPreview(new SearchPostsFilter(searchByTag), pageRequest);

    if (postPreviews != null && !postPreviews.isEmpty() ) {
      model.addAttribute("posts", postPreviews);
      model.addAttribute("paging",
          new Paging(pageRequest.getPageNumber(), pageRequest.getPageSize(), postPreviews.getFirst().getTotalCount()));

    }

    return "posts";
  }

  @GetMapping(path = "/{id}/image")
  @ResponseBody
  public byte[] getPostImage(@PathVariable("id") String postId) {
    return postsService.getImage(postId);
  }

  // TODO get userID from authentication headers
  @GetMapping("/{id}/like")
  public String togglePostLike(@PathVariable("id") String postId, @RequestParam(value = "userId", required = false) String userId) {
    postsService.togglePostLike(postId, "CURRENT_USER");

    return "redirect:/posts/" + postId;
  }

}