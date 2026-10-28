package com.planner.persian.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val categoryTag: String = "کاری",
    val dueTime: String = "", // e.g. "۱۴:۳۰"
    val dueDateEpochMillis: Long = System.currentTimeMillis(),
    val createdAtEpochMillis: Long = System.currentTimeMillis()
)
