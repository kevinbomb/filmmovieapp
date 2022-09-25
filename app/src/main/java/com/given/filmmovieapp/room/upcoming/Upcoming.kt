package com.given.filmmovieapp.room.upcoming

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Upcoming (
    @PrimaryKey(autoGenerate = true)
    val num: Int,
    val judul: String,
    val direktur: String,
    val tanggal: String,
    val sinopsis: String

)