package com.example.roomcompose.presentation.contacts_screen

import android.graphics.Bitmap
import com.example.roomcompose.data.Contacts

data class ContactState(
    val contacts: List<Contacts> = emptyList(),
    val firstname: String = "",
    val lastname: String = "",
    val phoneNumber: String = "",
    val isAddingContact: Boolean = false,
    val sortType: SortType = SortType.FIRST_NAME,
    val image: Bitmap? = null
)
