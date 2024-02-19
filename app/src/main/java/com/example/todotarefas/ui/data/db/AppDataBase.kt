package com.example.todotarefas.ui.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todotarefas.ui.data.db.dao.taskDao
import com.example.todotarefas.ui.data.db.entity.TaskEntity

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun taskDao(): taskDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}