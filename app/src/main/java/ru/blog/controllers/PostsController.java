package ru.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostsController {

    PostsController() {
    }

    @GetMapping
    public String posts() {
        return "posts";
    }

}