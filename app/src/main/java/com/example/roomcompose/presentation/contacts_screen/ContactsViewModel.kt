package com.example.roomcompose.presentation.contacts_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomcompose.data.Contacts
import com.example.roomcompose.data.ContactsDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactsViewModel(
   private val contactsDao: ContactsDao
) : ViewModel() {


    private val _state = MutableStateFlow(ContactState())

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.FIRST_NAME -> contactsDao.getContactsOrderedByFirstName()
                SortType.LAST_NAME -> contactsDao.getContactsOrderedByLastName()
                SortType.PHONE_NUMBER -> contactsDao.getContactsOrderedByPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())


    fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.DeleteContacts -> {
                viewModelScope.launch {
                    contactsDao.deleteContacts(event.contacts)
                }
            }

            ContactsEvent.HideDialog -> {
                _state.update { it.copy(isAddingContact = false) }
            }

            ContactsEvent.SaveContacts -> {

                val firstname = state.value.firstname
                val lastname = state.value.lastname
                val phoneNumber = state.value.phoneNumber
                val image = state.value.image

                if (firstname.isBlank() || lastname.isBlank() || phoneNumber.isBlank()){
                    return
                }
                val contacts = Contacts(
                    firstname = firstname,
                    lastname = lastname,
                    phoneNumber = phoneNumber,
                    bitmap = image

                )
                viewModelScope.launch {
                    contactsDao.insertContacts(contacts)
                }

                _state.update { it.copy(
                    isAddingContact = false,
                    firstname = "",
                    lastname = "",
                    phoneNumber = "",
                    image = null
                ) }

            }

            is ContactsEvent.SetFirstName -> {
                _state.update { it.copy(firstname = event.firstname) }
            }

            is ContactsEvent.SetLastName -> {
                _state.update { it.copy(lastname = event.lastname) }

            }

            is ContactsEvent.SetPhoneNumber -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }

            }

            ContactsEvent.ShowDialog -> {
                _state.update { it.copy(isAddingContact = true) }
            }

            is ContactsEvent.SortContacts -> {
                _sortType.value = event.sortType
            }

            is ContactsEvent.PickImage -> {
                _state.update { it.copy(image = event.bmp) }
            }
        }
    }


}