package com.example.enigma.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.enigma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactDialog(
    contactName: String,
    title: String,
    body: String,
    dismissible: Boolean,
    onContactNameChanged: (String) -> Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit
) {
    var isContactNameValidationError by remember { mutableStateOf(false) }

    BasicAlertDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        properties = DialogProperties(
            dismissOnClickOutside = dismissible
        )
    ) {
        DialogContentTemplate(
            content = {
                ContactNameInput(
                    contactName = contactName,
                    isError = isContactNameValidationError,
                    onContactNameChanged = {
                        newValue -> isContactNameValidationError = !onContactNameChanged(newValue)
                    }
                )
            },
            title = title,
            body = body,
            dismissible = dismissible,
            onConfirmClicked = {
                isContactNameValidationError = isContactNameValidationError || contactName.isEmpty()
                if(!isContactNameValidationError)
                {
                    onConfirmClicked()
                }
            },
            onDismissClicked =  { onDismissClicked() }
        )
    }
}

@Composable
fun DialogContentTemplate(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    content: @Composable () -> Unit,
    dismissible: Boolean,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight
            )
            HorizontalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.background)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = body,
                textAlign = TextAlign.Center,
            )
            HorizontalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.background)
            content()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if(dismissible)
                {
                    OutlinedButton(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            onDismissClicked()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.dismiss)
                        )
                    }
                }
                Button(
                    modifier = Modifier.padding(4.dp),
                    onClick = {
                        onConfirmClicked()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }
            }
        }
    }
}

@Composable
fun ContactNameInput(
    contactName: String,
    isError: Boolean,
    onContactNameChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contactName,
        isError = isError,
        onValueChange = {
            newValue -> onContactNameChanged(newValue)
        },
        label = {
            Text(
                text = stringResource(id = R.string.contact_name)
            )
        },
        maxLines = 1
    )
}

@Composable
@Preview
fun SaveContactDialogPreview()
{
    EditContactDialog(
        contactName = "",
        title = stringResource(
            id = R.string.qr_code_scanned_successfully
        ),
        body = stringResource(
            id = R.string.save_contact_message
        ),
        dismissible = false,
        onContactNameChanged = { true },
        onDismissRequest = { },
        onConfirmClicked = { },
        onDismissClicked = { }
    )
}
