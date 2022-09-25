package com.given.filmmovieapp.room.upcoming

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Upcoming::class],
    version = 1
)
abstract class UpcomingDB: RoomDatabase() {
    abstract fun upcomingDao() : UpcomingDao
    companion object {
        @Volatile private var instance : UpcomingDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UpcomingDB::class.java,
                "upcoming12345.db"
            ).build()
    }
}