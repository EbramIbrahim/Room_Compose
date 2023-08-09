package com.example.roomcompose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.roomcompose.data.ContactsDatabase
import com.example.roomcompose.presentation.contacts_screen.ContactsViewModel
import com.example.roomcompose.ui.theme.RoomComposeTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactsDatabase::class.java,
            "contacts"
        ).build()

    }

    private val viewModel by viewModels<ContactsViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactsViewModel(database.contactsDao) as T
                }
            }
        }
    )


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current
            RoomComposeTheme {
                val state by viewModel.state.collectAsState()

                Contacts(state = state, onEvent = viewModel::onEvent, context = context)

            }
        }
    }
}

