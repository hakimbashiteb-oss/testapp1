package com.planner.persian.data.repository

import com.planner.persian.data.local.dao.NoteDao
import com.planner.persian.data.local.dao.TaskDao
import com.planner.persian.data.local.entity.NoteEntity
import com.planner.persian.data.local.entity.TaskEntity
import com.planner.persian.domain.model.Note
import com.planner.persian.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlannerRepository(
    private val taskDao: TaskDao,
    private val noteDao: NoteDao
) {
    // ---------------- TASK OPERATIONS ---------------- //

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks().map { list ->
        list.map { it.toDomain() }
    }

    val pendingTasks: Flow<List<Task>> = taskDao.getPendingTasks().map { list ->
        list.map { it.toDomain() }
    }

    val completedTasks: Flow<List<Task>> = taskDao.getCompletedTasks().map { list ->
        list.map { it.toDomain() }
    }

    fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { list -> list.map { it.toDomain() } }

    suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(TaskEntity.fromDomain(task))
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(TaskEntity.fromDomain(task))
    }

    suspend fun setTaskCompletion(taskId: Long, isCompleted: Boolean) {
        taskDao.updateTaskCompletion(taskId, isCompleted)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(TaskEntity.fromDomain(task))
    }

    suspend fun deleteTaskById(taskId: Long) {
        taskDao.deleteTaskById(taskId)
    }

    // ---------------- NOTE OPERATIONS ---------------- //

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes().map { list ->
        list.map { it.toDomain() }
    }

    fun getNoteById(id: Long): Flow<Note?> =
        noteDao.getNoteById(id).map { it?.toDomain() }

    fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query).map { list -> list.map { it.toDomain() } }

    suspend fun insertOrUpdateNote(note: Note): Long {
        return if (note.id == 0L) {
            noteDao.insertNote(NoteEntity.fromDomain(note))
        } else {
            noteDao.updateNote(NoteEntity.fromDomain(note))
            note.id
        }
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(NoteEntity.fromDomain(note))
    }

    suspend fun deleteNoteById(noteId: Long) {
        noteDao.deleteNoteById(noteId)
    }
}
