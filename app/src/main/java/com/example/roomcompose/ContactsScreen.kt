package com.example.roomcompose

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.roomcompose.presentation.contacts_screen.ContactState
import com.example.roomcompose.presentation.contacts_screen.ContactsEvent
import com.example.roomcompose.presentation.contacts_screen.SortType

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(
    state: ContactState,
    onEvent: (ContactsEvent) -> Unit,
    context: Context
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ContactsEvent.ShowDialog) }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = ""
                )

            }
        },
      modifier = Modifier.fillMaxSize()
    ) {  _ ->

        if (state.isAddingContact){
            AddContactDialog(state = state, onEvent = onEvent, context = context)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(ContactsEvent.SortContacts(it))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = state.sortType == it,
                                onClick = { onEvent(ContactsEvent.SortContacts(it)) }
                            )
                            Text(text = it.name)

                        }
                    }
                }
            }
            items(state.contacts) { contact ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Card(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                        ,
                    ) {
                        AsyncImage(
                            model = if (contact.bitmap == null)  R.drawable.user else contact.bitmap,
                            contentDescription ="" ,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${contact.firstname} ${contact.lastname}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = contact.phoneNumber,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Thin
                        )

                    }
                    IconButton(onClick = { onEvent(ContactsEvent.DeleteContacts(contact)) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
                    }

                }
            }
        }
    }


}












