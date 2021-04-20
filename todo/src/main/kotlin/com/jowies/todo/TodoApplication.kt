package com.jowies.todo

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class TodoApplication {

  @Bean
  fun run(todoListRepository: TodoListRepository, todoItemRepository: TodoItemRepository) =
      ApplicationRunner {
        todoListRepository.save(TodoList(name = "Hverdag", description = "Gjoremal i hverdagen"))
      }
}

fun main(args: Array<String>) {
  runApplication<TodoApplication>(*args)
}
