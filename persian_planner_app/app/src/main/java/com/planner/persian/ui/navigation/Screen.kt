package com.planner.persian.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Notes : Screen("notes")
    data object Search : Screen("search")
    data object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem(Screen.Dashboard.route, "امروز", Icons.Rounded.CalendarToday)
    data object Notes : BottomNavItem(Screen.Notes.route, "یادداشت‌ها", Icons.Rounded.Description)
    data object Search : BottomNavItem(Screen.Search.route, "جستجو", Icons.Rounded.Search)

    companion object {
        val items = listOf(Dashboard, Notes, Search)
    }
}
