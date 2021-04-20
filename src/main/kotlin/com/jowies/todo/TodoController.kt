package com.jowies.todo

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/list")
class TodoController(val todoService: TodoListService) {

  @GetMapping
  fun getAllLists(): List<TodoList> {
    print("1")
    return todoService.getAllLists()
  }

  @GetMapping("/{id}")
  fun getListById(@PathVariable id: Long): TodoList {
    print("1")
    return todoService.getList(id)
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This todo list does not exist")
  }

  @PostMapping
  fun addList(@RequestBody todoList: TodoList): TodoList {
    return todoService.addList(todoList)
  }

  @DeleteMapping("/{id}")
  fun deletePost(@PathVariable id: Long) {
    return todoService.removeList(id)
  }

  @PutMapping("/{id}")
  fun deletePost(@PathVariable id: Long, @RequestBody todoList: TodoList): TodoList {
    return todoService.updateList(id, todoList)
  }
}
