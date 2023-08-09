@file:Suppress("DEPRECATION")

package com.example.roomcompose


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.roomcompose.presentation.contacts_screen.ContactState
import com.example.roomcompose.presentation.contacts_screen.ContactsEvent

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialog(
    state: ContactState,
    onEvent: (ContactsEvent) -> Unit,
    modifier: Modifier = Modifier,
    context: Context
) {

    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->

            if (result.resultCode == RESULT_OK){
                val intent = result.data
                val imageUri = intent?.data
                selectedImage = imageUri
                val source = ImageDecoder.createSource(context.contentResolver, selectedImage!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                onEvent(ContactsEvent.PickImage(bitmap))
            }

        }
    )

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(ContactsEvent.HideDialog)
        },
        title = { Text(text = "Add contact") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AsyncImage(
                    model = if (selectedImage == null)  R.drawable.user else selectedImage,
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            val intent = Intent(ACTION_GET_CONTENT)
                            intent.type = "image/*"
                            selectImage.launch(intent)
                        }
                        .align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = state.firstname,
                    onValueChange = {
                        onEvent(ContactsEvent.SetFirstName(it))
                    },
                    label = {
                        Text(text = "First name")
                    }
                )

                OutlinedTextField(
                    value = state.lastname,
                    onValueChange = {
                        onEvent(ContactsEvent.SetLastName(it))
                    },
                    label = {
                        Text(text = "Last name")
                    }
                )
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        onEvent(ContactsEvent.SetPhoneNumber(it))
                    },
                    label = {
                        Text(text = "Phone number")
                    }
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(ContactsEvent.SaveContacts)
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}

