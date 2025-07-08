package com.tedd.todo_project.feature.histories.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.number

fun LocalDateTime.toFormattedString(): String {
    return "$year/${month.number.pad()}/${day.pad()}"
}

fun Int.pad(size: Int = 2) = toString().padStart(size, '0')