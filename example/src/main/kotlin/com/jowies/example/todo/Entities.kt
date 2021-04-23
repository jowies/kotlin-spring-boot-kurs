package com.jowies.example.todo

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class TodoList(
        var name: String,
        var description: String,
        var created: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null
)
