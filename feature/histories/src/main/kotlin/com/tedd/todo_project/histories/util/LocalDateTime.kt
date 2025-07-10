package com.tedd.todo_project.histories.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number

fun LocalDateTime.toFormattedString(): String {
    return "$year/${month.number.pad()}/${day.pad()}"
}

fun Int.pad(size: Int = 2) = toString().padStart(size, '0')