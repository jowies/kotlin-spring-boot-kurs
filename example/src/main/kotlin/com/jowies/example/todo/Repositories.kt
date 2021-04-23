package com.jowies.example.todo

import org.springframework.data.repository.CrudRepository

interface TodoListRepository : CrudRepository<TodoList, Long> {}
