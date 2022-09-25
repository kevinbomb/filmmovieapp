package com.given.filmmovieapp.room.upcoming

import androidx.room.*

@Dao
interface UpcomingDao {
    @Insert
    suspend fun addUpcoming(upcoming: Upcoming)
    @Update
    suspend fun updateUpcoming(upcoming: Upcoming)
    @Delete
    suspend fun deleteUpcoming(upcoming: Upcoming)
    @Query("SELECT * FROM upcoming")
    suspend fun getUpcoming() : List<Upcoming>
    @Query("SELECT * FROM upcoming WHERE num =:nomor")
    suspend fun getUserId(nomor: Int) : List<Upcoming>
}