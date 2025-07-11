package com.tedd.todo_project.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val work: String,
    val isCompleted: Boolean,
    val addedTime: LocalDateTime,
    val completedTime: LocalDateTime?,
    val position: Int = 0
)
