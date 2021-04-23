package com.jowies.example.todo

import org.springframework.stereotype.Service

@Service
class TodoListService(
    var todoListRepository: TodoListRepository,
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

  fun updateList(id: Long, updatedList: TodoList): TodoList? {
    return todoListRepository.findById(id).orElse(null)?.let {
      it.name = updatedList.name
      it.description = updatedList.description
      return todoListRepository.save(it)
    }
}
