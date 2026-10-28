package com.planner.persian.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.planner.persian.data.local.dao.NoteDao
import com.planner.persian.data.local.dao.TaskDao
import com.planner.persian.data.local.entity.NoteEntity
import com.planner.persian.data.local.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TaskEntity::class, NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "persian_planner_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialPersianData(database.taskDao(), database.noteDao())
                    }
                }
            }
        }

        private suspend fun populateInitialPersianData(taskDao: TaskDao, noteDao: NoteDao) {
            val now = System.currentTimeMillis()

            // Pre-populate realistic Persian planner tasks
            taskDao.insertTask(
                TaskEntity(
                    title = "بررسی و طراحی معماری اپلیکیشن",
                    description = "پیاده‌سازی Clean Architecture و کامپوز در اندروید",
                    isCompleted = true,
                    priorityLevel = 3, // High
                    categoryTag = "کاری",
                    dueTime = "۰۹:۳۰",
                    dueDateEpochMillis = now,
                    createdAtEpochMillis = now - 3600000
                )
            )
            taskDao.insertTask(
                TaskEntity(
                    title = "جلسه هماهنگی تیم برنامه‌ریزی روزانه",
                    description = "بررسی اهداف و شاخص‌های کلیدی فصل پاییز",
                    isCompleted = false,
                    priorityLevel = 3, // High
                    categoryTag = "کاری",
                    dueTime = "۱۱:۰۰",
                    dueDateEpochMillis = now,
                    createdAtEpochMillis = now
                )
            )
            taskDao.insertTask(
                TaskEntity(
                    title = "ورزش و پیاده‌روی عصرگاهی",
                    description = "۴۵ دقیقه تمرین هوازی در پارک ملت",
                    isCompleted = false,
                    priorityLevel = 2, // Medium
                    categoryTag = "شخصی",
                    dueTime = "۱۸:۳۰",
                    dueDateEpochMillis = now,
                    createdAtEpochMillis = now
                )
            )
            taskDao.insertTask(
                TaskEntity(
                    title = "مطالعه ۲۰ صفحه از کتاب تفکر سریع و کند",
                    description = "یادداشت‌برداری از فصل سوگیری‌های شناختی",
                    isCompleted = false,
                    priorityLevel = 1, // Low
                    categoryTag = "مطالعه",
                    dueTime = "۲۱:۱۵",
                    dueDateEpochMillis = now,
                    createdAtEpochMillis = now
                )
            )

            // Pre-populate sample Persian notes with pastel colors
            noteDao.insertNote(
                NoteEntity(
                    title = "ایده‌های محصول جدید",
                    content = "اضافه کردن ویجت تقویم خورشیدی به هوم‌اسکرین اندروید با انیمیشن‌های مینیمال و ویجت‌های تعاملی Glance.",
                    tag = "ایده",
                    colorHex = "#FFF9C4", // Warm Pastel Yellow
                    isPinned = true,
                    lastModifiedEpochMillis = now
                )
            )
            noteDao.insertNote(
                NoteEntity(
                    title = "لیست خرید مایحتاج هفتگی",
                    content = "۱. قهوه ترک و اسپرسو\n۲. زیتون پرورده و پنیر تبریز\n۳. نان سنگک کنجدی\n۴. میوه‌های تازه پاییزی (انار و نارنگی)",
                    tag = "شخصی",
                    colorHex = "#E1F5FE", // Soft Pastel Blue
                    isPinned = false,
                    lastModifiedEpochMillis = now - 1800000
                )
            )
            noteDao.insertNote(
                NoteEntity(
                    title = "نکات معماری Compose در اندروید",
                    content = "همیشه وضعیت‌ها (State) را بالا ببرید (State Hoisting) و رویدادها (Events) را به صورت لامبدا به سمت بالا هدایت کنید.",
                    tag = "مطالعه",
                    colorHex = "#E8F5E9", // Soft Pastel Green
                    isPinned = false,
                    lastModifiedEpochMillis = now - 7200000
                )
            )
        }
    }
}
