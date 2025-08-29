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
import ru.blog.dto.Paging;
import ru.blog.models.PostDetails;
import ru.blog.models.PostPreview;
import ru.blog.dto.SearchPostsFilter;
import ru.blog.services.PostsService;

@Controller
@RequestMapping("/posts")
public class PostsController {
  PostsService postsService;

  PostsController(final PostsService postsService) {
    this.postsService = postsService;
  }

  @PostMapping()
  public String save(@RequestParam("userId") String userId,
                     @RequestParam("text") String text,
                     @RequestParam("title") String title,
                     @RequestParam("tags") String tags,
                     @RequestParam("image")
                     MultipartFile image)
      throws IOException {
    try {
      var imageStream = image.getInputStream();

      var postDetails =
          new PostDetails(null, title, text, userId, Arrays.stream(tags.split(" ")).toList(), null, null);
      postsService.save(postDetails, imageStream);
    } catch (IOException e) {
      throw e;
    }

    return "redirect:/posts";
  }

  @PostMapping("/{id}")
  public String update(@RequestParam("userId") String userId,
                       @PathVariable(value = "id") String postId,
                       @RequestParam("text") String text,
                       @RequestParam("title") String title,
                       @RequestParam("tags") String tags,
                       @RequestParam("image")
                       MultipartFile image)
      throws IOException {
    try {
      var imageStream = image.getInputStream();

      var postDetails =
          new PostDetails(postId, title, text, userId, Arrays.stream(tags.split(" ")).toList(), null, null);
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
  public String removePost(@RequestParam("userId") String userId, @PathVariable(name = "id") String postId) {
    postsService.remove(postId, userId);

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
    var searchFilter = new SearchPostsFilter(searchByTag);

    List<PostPreview> postPreviews = postsService.searchPostPreview(searchFilter, pageRequest);

    if (postPreviews != null && !postPreviews.isEmpty()) {
      Integer totalCount = postsService.searchPostPreviewCount(searchFilter);

      model.addAttribute("posts", postPreviews);
      model.addAttribute("paging",
          new Paging(pageRequest.getPageNumber(), pageRequest.getPageSize(), totalCount));
    }

    return "posts";
  }

  @GetMapping(path = "/{id}/image")
  @ResponseBody
  public byte[] getPostImage(@PathVariable("id") String postId) {
    return postsService.getImage(postId);
  }

  // TODO get userID from authentication headers
  @PostMapping("/{id}/like")
  public String togglePostLike(@RequestParam("userId") String userId, @PathVariable("id") String postId) {
    postsService.togglePostLike(postId, userId);

    return "redirect:/posts/" + postId;
  }

}
