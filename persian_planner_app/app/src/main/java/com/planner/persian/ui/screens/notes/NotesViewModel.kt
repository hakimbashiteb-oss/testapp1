package com.planner.persian.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.planner.persian.data.repository.PlannerRepository
import com.planner.persian.domain.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val selectedTagFilter: String? = null,
    val isLoading: Boolean = false
)

class NotesViewModel(
    private val repository: PlannerRepository
) : ViewModel() {

    private val _selectedTag = MutableStateFlow<String?>(null)

    val uiState: StateFlow<NotesUiState> = combine(
        repository.allNotes,
        _selectedTag
    ) { notes, tag ->
        val filtered = if (tag.isNullOrBlank()) {
            notes
        } else {
            notes.filter { it.tag == tag }
        }
        NotesUiState(
            notes = filtered,
            selectedTagFilter = tag,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState(isLoading = true)
    )

    fun onSelectTag(tag: String?) {
        _selectedTag.value = tag
    }

    fun onSaveNote(
        id: Long,
        title: String,
        content: String,
        tag: String,
        colorHex: String,
        isPinned: Boolean
    ) {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            val note = Note(
                id = id,
                title = title.trim(),
                content = content.trim(),
                tag = tag.trim(),
                colorHex = colorHex,
                isPinned = isPinned,
                lastModifiedEpochMillis = System.currentTimeMillis()
            )
            repository.insertOrUpdateNote(note)
        }
    }

    fun onDeleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNoteById(noteId)
        }
    }

    fun getNoteFlow(noteId: Long) = repository.getNoteById(noteId)

    companion object {
        fun provideFactory(repository: PlannerRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(repository) as T
                }
            }
    }
}
