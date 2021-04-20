package com.jowies.todo

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class TodoList(
        var name: String,
        var description: String,
        var created: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null
)

@Entity
class TodoItem(
        var decription: String,
        var created: LocalDateTime = LocalDateTime.now(),
        var completed: LocalDateTime? = null,
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn("list_id") var todoList: TodoList,
        @Id @GeneratedValue var id: Long? = null
)
