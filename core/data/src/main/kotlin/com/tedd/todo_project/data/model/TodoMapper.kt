package com.tedd.todo_project.data.model

import com.tedd.todo_project.database.TodoEntity
import com.tedd.todo_project.domain.model.Todo

fun Todo.toEntity() = TodoEntity(
    id = id,
    work = work,
    isCompleted = isCompleted,
    addedTime = addedTime,
    completedTime = completedTime,
    position = position
)

fun TodoEntity.toDomain() = Todo(
    id = id,
    work = work,
    isCompleted = isCompleted,
    addedTime = addedTime,
    completedTime = completedTime,
    position = position
)
