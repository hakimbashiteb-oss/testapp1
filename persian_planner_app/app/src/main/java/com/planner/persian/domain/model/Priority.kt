package com.planner.persian.domain.model

enum class Priority(
    val persianLabel: String,
    val level: Int
) {
    HIGH(persianLabel = "فوری و مهم", level = 3),
    MEDIUM(persianLabel = "متوسط", level = 2),
    LOW(persianLabel = "پایین", level = 1);

    companion object {
        fun fromLevel(level: Int): Priority {
            return entries.find { it.level == level } ?: MEDIUM
        }
    }
}
