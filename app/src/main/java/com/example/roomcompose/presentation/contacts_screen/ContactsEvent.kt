package com.example.roomcompose.presentation.contacts_screen

import android.graphics.Bitmap
import com.example.roomcompose.data.Contacts

sealed interface ContactsEvent {
    object SaveContacts : ContactsEvent
    data class SetFirstName(val firstname: String): ContactsEvent
    data class SetLastName(val lastname: String): ContactsEvent
    data class SetPhoneNumber(val phoneNumber: String): ContactsEvent
    object ShowDialog: ContactsEvent
    object HideDialog: ContactsEvent
    data class SortContacts(val sortType: SortType): ContactsEvent
    data class DeleteContacts(val contacts: Contacts): ContactsEvent
    data class PickImage(val bmp: Bitmap?): ContactsEvent

}