package com.example.roomcompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Contacts::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ContactsDatabase: RoomDatabase() {

    abstract val contactsDao: ContactsDao
}