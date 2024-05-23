package com.mhss.app.mybrain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mhss.app.mybrain.data.local.converters.DBConverters
import com.mhss.app.mybrain.data.local.dao.*
import com.mhss.app.mybrain.domain.model.alarm.Alarm
import com.mhss.app.mybrain.domain.model.bookmarks.Bookmark
import com.mhss.app.mybrain.domain.model.diary.DiaryEntry
import com.mhss.app.mybrain.domain.model.notes.Note
import com.mhss.app.mybrain.domain.model.notes.NoteFolder
import com.mhss.app.mybrain.domain.model.tasks.Task

@Database(
    entities = [Note::class, Task::class, DiaryEntry::class, Bookmark::class, Alarm::class, NoteFolder::class],
    version = 4
)
@TypeConverters(DBConverters::class)
abstract class MyBrainDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun diaryDao(): DiaryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        const val DATABASE_NAME = "by_brain_db"
    }
}