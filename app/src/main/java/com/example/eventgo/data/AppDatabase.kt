package com.example.eventgo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventgo.EventDao

/**
 * The main database for the application. This database stores Event entities.
 */
@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Provides access to the DAO for Event entities.
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Retrieves the singleton instance of the database.
        // If the database does not already exist, it is created.
        fun getDatabase(context: Context): AppDatabase {
            // If INSTANCE is null, then synchronized block is used to ensure only one instance is created
            return INSTANCE ?: synchronized(this) {
                // Create a new instance of the database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "event_database"
                ).build()
                INSTANCE = instance // Set the instance to the newly created database
                instance // Return the instance
            }
        }
    }
}
