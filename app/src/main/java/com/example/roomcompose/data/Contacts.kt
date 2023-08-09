package com.example.roomcompose.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contacts(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    var bitmap: Bitmap?
)
