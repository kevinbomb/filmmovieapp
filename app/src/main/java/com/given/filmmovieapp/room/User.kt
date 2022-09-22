package com.given.filmmovieapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val name: String,
    val email: String,
    val password: String
)