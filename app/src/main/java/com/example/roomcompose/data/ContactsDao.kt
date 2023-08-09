package com.example.roomcompose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {


    @Upsert
    suspend fun insertContacts(contacts: Contacts)

    @Delete
    suspend fun deleteContacts(contacts: Contacts)


    @Query("SELECT * FROM contacts ORDER BY firstname ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Contacts>>

    @Query("SELECT * FROM contacts ORDER BY lastname ASC")
    fun getContactsOrderedByLastName(): Flow<List<Contacts>>

    @Query("SELECT * FROM contacts ORDER BY phoneNumber ASC")
    fun getContactsOrderedByPhoneNumber(): Flow<List<Contacts>>









}