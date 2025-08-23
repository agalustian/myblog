package ru.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.blog.models.PostComment;
import ru.blog.services.PostCommentsService;

@Controller
@RequestMapping("/posts/{id}/comments")
public class PostCommentsController {

  private static final String REDIRECT_PATH_TO_POST = "redirect:/posts/";

  private final PostCommentsService postCommentsService;

  PostCommentsController(final PostCommentsService postCommentsService) {
    this.postCommentsService = postCommentsService;
  }

  @PostMapping
  String save(@PathVariable("id") String postId, @RequestParam("text") String comment) {
    postCommentsService.save(PostComment.from(postId, "CURRENT_USER", comment));

    return  REDIRECT_PATH_TO_POST+postId;
  }

  @PostMapping("{comment_id}/edit")
  String update(@PathVariable("id") String postId, @RequestParam("text") String comment) {
    postCommentsService.update(postId, "CURRENT_USER", comment);

    return REDIRECT_PATH_TO_POST+postId;
  }

  @PostMapping("{comment_id}/delete")
  String remove(@PathVariable("id") String postId, @PathVariable("comment_id") String commentId) {
    postCommentsService.remove(commentId, "CURRENT_USER");

    return REDIRECT_PATH_TO_POST+postId;
  }

}
