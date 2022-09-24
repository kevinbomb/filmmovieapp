package com.given.filmmovieapp.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: User)
    @Update
    suspend fun updateUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
    @Query("SELECT * FROM user")
    suspend fun getUsers() : List<User>
    @Query("SELECT * FROM user WHERE id =:user_id")
    suspend fun getUserId(user_id: Int) : List<User>
    @Query("SELECT * FROM user WHERE username =:user_name")
    suspend fun getUserName(user_name: String) : List<User>
}