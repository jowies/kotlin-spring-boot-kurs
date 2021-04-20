package com.jowies.todo

import org.springframework.stereotype.Service

@Service
class TodoListService(
    var todoListRepository: TodoListRepository,
    var todoItemRepository: TodoItemRepository
) {

  fun getAllLists(): List<TodoList> {
    return todoListRepository.findAll().toList()
  }

  fun getList(id: Long): TodoList? {
    return todoListRepository.findById(id).orElse(null)
  }

  fun addList(todoList: TodoList): TodoList {
    return todoListRepository.save(todoList)
  }

  fun removeList(id: Long) {
    return todoListRepository.deleteById(id)
  }

  fun updateList(id: Long, newTodoList: TodoList): TodoList {
    return todoListRepository.findById(id).orElse(null)?.let {
      it.name = newTodoList.name
      it.description = newTodoList.description
      return todoListRepository.save(it)
    }
        ?: let {
          newTodoList.id = id
          return todoListRepository.save(newTodoList)
        }
  }
}
