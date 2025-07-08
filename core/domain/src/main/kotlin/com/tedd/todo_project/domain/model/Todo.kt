package com.tedd.todo_project.domain.model

import kotlinx.datetime.LocalDateTime

data class Todo(
    val id: Long = 0,
    val work: String,
    val isCompleted: Boolean,
    val addedTime: LocalDateTime,
    val completedTime: LocalDateTime?
)