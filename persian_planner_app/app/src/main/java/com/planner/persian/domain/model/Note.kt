package com.planner.persian.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val tag: String = "ایده",
    val colorHex: String = "#FFF9C4", // Pastel yellow default
    val isPinned: Boolean = false,
    val lastModifiedEpochMillis: Long = System.currentTimeMillis()
)
