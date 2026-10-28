package com.planner.persian

import android.app.Application
import com.planner.persian.data.local.AppDatabase
import com.planner.persian.data.repository.PlannerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PersianPlannerApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy {
        AppDatabase.getDatabase(this, applicationScope)
    }

    val repository by lazy {
        PlannerRepository(
            taskDao = database.taskDao(),
            noteDao = database.noteDao()
        )
    }
}
