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
fun TextInputDialog(
    title: String,
    body: String,
    placeholderText: String,
    onTextChanged: (String) -> Boolean,
    onConfirmClicked: () -> Unit,
    onDismissClicked: () -> Unit
) {
    var isValidationError by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            dismissOnClickOutside = false
        )
    ) {
        DialogContentTemplate(
            content = {
                TextInput(
                    text = text,
                    placeholderText = placeholderText,
                    isError = isValidationError,
                    onTextChanged = {
                        newText -> isValidationError = !onTextChanged(newText)
                        text = newText
                    }
                )
            },
            title = title,
            body = body,
            dismissible = true,
            onPositiveButtonClicked = {
                isValidationError = isValidationError || text.isEmpty()
                if(!isValidationError)
                {
                    onConfirmClicked()
                    text = ""
                }
            },
            onNegativeButtonClicked =  {
                onDismissClicked()
                text = ""
            }
        )
    }
}

@Composable
fun DialogContentTemplate(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    content: @Composable () -> Unit,
    dismissible: Boolean = true,
    positiveButtonVisible: Boolean = true,
    negativeButtonText: String = stringResource(id = R.string.dismiss),
    positiveButtonText: String = stringResource(id = R.string.confirm),
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicked: () -> Unit
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
            if(body.isNotEmpty()) {
                HorizontalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.background)
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = body,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(thickness = 4.dp, color = MaterialTheme.colorScheme.background)
            }
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
                            onNegativeButtonClicked()
                        }
                    ) {
                        Text(
                            text = negativeButtonText
                        )
                    }
                }
                if(positiveButtonVisible) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            onPositiveButtonClicked()
                        }
                    ) {
                        Text(
                            text = positiveButtonText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextInput(
    text: String,
    placeholderText: String,
    isError: Boolean,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        isError = isError,
        onValueChange = {
            newValue -> onTextChanged(newValue)
        },
        label = {
            Text(
                text = placeholderText
            )
        },
        singleLine = true
    )
}

@Composable
@Preview
fun TextInputDialogPreview()
{
    TextInputDialog(
        title = stringResource(
            id = R.string.contact_details_successfully_retrieved
        ),
        body = stringResource(
            id = R.string.save_contact_message
        ),
        onTextChanged = { true },
        onConfirmClicked = { },
        onDismissClicked = { },
        placeholderText = ""
    )
}