package com.planner.persian.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.planner.persian.domain.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val tag: String,
    val colorHex: String,
    val isPinned: Boolean,
    val lastModifiedEpochMillis: Long
) {
    fun toDomain(): Note = Note(
        id = id,
        title = title,
        content = content,
        tag = tag,
        colorHex = colorHex,
        isPinned = isPinned,
        lastModifiedEpochMillis = lastModifiedEpochMillis
    )

    companion object {
        fun fromDomain(note: Note): NoteEntity = NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            tag = note.tag,
            colorHex = note.colorHex,
            isPinned = note.isPinned,
            lastModifiedEpochMillis = note.lastModifiedEpochMillis
        )
    }
}
