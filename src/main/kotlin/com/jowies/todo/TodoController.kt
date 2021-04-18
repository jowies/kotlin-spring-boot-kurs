package com.jowies.todo

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
class TodoController {

  @GetMapping("/")
  fun blog(): String {
    return "blog"
  }

}