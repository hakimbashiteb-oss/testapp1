package com.planner.persian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.planner.persian.domain.model.Priority
import com.planner.persian.ui.theme.PriorityHigh
import com.planner.persian.ui.theme.PriorityHighContainer
import com.planner.persian.ui.theme.PriorityHighText
import com.planner.persian.ui.theme.PriorityLow
import com.planner.persian.ui.theme.PriorityLowContainer
import com.planner.persian.ui.theme.PriorityLowText
import com.planner.persian.ui.theme.PriorityMedium
import com.planner.persian.ui.theme.PriorityMediumContainer
import com.planner.persian.ui.theme.PriorityMediumText

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (priority) {
        Priority.HIGH -> PriorityHighContainer to PriorityHighText
        Priority.MEDIUM -> PriorityMediumContainer to PriorityMediumText
        Priority.LOW -> PriorityLowContainer to PriorityLowText
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = priority.persianLabel,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
