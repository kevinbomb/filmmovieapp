package com.given.filmmovieapp.room.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val tanggalLahir: String,
    val noTelepon: String
)