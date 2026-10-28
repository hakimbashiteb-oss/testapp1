package com.planner.persian.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planner.persian.domain.model.Priority
import com.planner.persian.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val priorityLevel: Int,
    val categoryTag: String,
    val dueTime: String,
    val dueDateEpochMillis: Long,
    val createdAtEpochMillis: Long
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = Priority.fromLevel(priorityLevel),
        categoryTag = categoryTag,
        dueTime = dueTime,
        dueDateEpochMillis = dueDateEpochMillis,
        createdAtEpochMillis = createdAtEpochMillis
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            isCompleted = task.isCompleted,
            priorityLevel = task.priority.level,
            categoryTag = task.categoryTag,
            dueTime = task.dueTime,
            dueDateEpochMillis = task.dueDateEpochMillis,
            createdAtEpochMillis = task.createdAtEpochMillis
        )
    }
}
