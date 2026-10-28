package com.planner.persian.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.planner.persian.data.repository.PlannerRepository
import com.planner.persian.domain.model.Priority
import com.planner.persian.domain.model.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val pendingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val totalCount: Int = 0,
    val completedCount: Int = 0,
    val isLoading: Boolean = false
)

class DashboardViewModel(
    private val repository: PlannerRepository
) : ViewModel() {

    private var recentlyDeletedTask: Task? = null

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.pendingTasks,
        repository.completedTasks
    ) { pending, completed ->
        val total = pending.size + completed.size
        DashboardUiState(
            pendingTasks = pending,
            completedTasks = completed,
            totalCount = total,
            completedCount = completed.size,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun onToggleTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setTaskCompletion(task.id, isCompleted)
        }
    }

    fun onDeleteTask(task: Task) {
        recentlyDeletedTask = task
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun onUndoDelete() {
        recentlyDeletedTask?.let { task ->
            viewModelScope.launch {
                repository.insertTask(task)
                recentlyDeletedTask = null
            }
        }
    }

    fun onAddNewTask(
        title: String,
        description: String,
        priority: Priority,
        categoryTag: String,
        dueTime: String
    ) {
        if (title.isBlank()) return
        viewModelScope.launch {
            val newTask = Task(
                title = title.trim(),
                description = description.trim(),
                priority = priority,
                categoryTag = categoryTag.trim(),
                dueTime = dueTime.trim(),
                dueDateEpochMillis = System.currentTimeMillis()
            )
            repository.insertTask(newTask)
        }
    }

    companion object {
        fun provideFactory(repository: PlannerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DashboardViewModel(repository) as T
                }
            }
    }
}
