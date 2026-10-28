package com.planner.persian.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.planner.persian.data.repository.PlannerRepository
import com.planner.persian.domain.model.Note
import com.planner.persian.domain.model.Priority
import com.planner.persian.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SearchFilter(val persianLabel: String) {
    ALL("همه"),
    PENDING("انجام نشده"),
    HIGH_PRIORITY("دارای اولویت بالا"),
    NOTES("یادداشت‌ها")
}

data class SearchUiState(
    val query: String = "",
    val activeFilter: SearchFilter = SearchFilter.ALL,
    val matchedTasks: List<Task> = emptyList(),
    val matchedNotes: List<Note> = emptyList()
)

class SearchViewModel(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _filter = MutableStateFlow(SearchFilter.ALL)

    private val debouncedQuery = _query
        .debounce(250)
        .distinctUntilChanged()

    val uiState: StateFlow<SearchUiState> = combine(
        _query,
        _filter,
        debouncedQuery.flatMapLatest { query ->
            combine(
                repository.searchTasks(query),
                repository.searchNotes(query)
            ) { tasks, notes -> Pair(tasks, notes) }
        }
    ) { query, filter, (tasks, notes) ->
        val filteredTasks = when (filter) {
            SearchFilter.ALL -> tasks
            SearchFilter.PENDING -> tasks.filter { !it.isCompleted }
            SearchFilter.HIGH_PRIORITY -> tasks.filter { it.priority == Priority.HIGH }
            SearchFilter.NOTES -> emptyList()
        }

        val filteredNotes = when (filter) {
            SearchFilter.ALL, SearchFilter.NOTES -> notes
            else -> emptyList()
        }

        SearchUiState(
            query = query,
            activeFilter = filter,
            matchedTasks = filteredTasks,
            matchedNotes = filteredNotes
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun onFilterSelected(filter: SearchFilter) {
        _filter.value = filter
    }

    fun onToggleTask(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setTaskCompletion(task.id, isCompleted)
        }
    }

    fun onDeleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    companion object {
        fun provideFactory(repository: PlannerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(repository) as T
                }
            }
    }
}
