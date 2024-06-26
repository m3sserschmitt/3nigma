package com.example.enigma.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.enigma.R

@Composable
fun RenameContactDialog(
    visible: Boolean,
    onNewContactNameChanged: (String) -> Boolean,
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    if(visible) {
        EditContactDialog(
            onContactNameChanged = onNewContactNameChanged,
            title = stringResource(
                id = R.string.rename_contact
            ),
            body = stringResource(
                id = R.string.enter_new_contact_name
            ),
            onConfirmClicked = onConfirmClicked,
            onDismissClicked = onDismiss,
        )
    }
}

@Preview
@Composable
fun RenameContactDialogPreview()
{
    RenameContactDialog(
        visible = true,
        onNewContactNameChanged = { true },
        onConfirmClicked = {},
        onDismiss = {}
    )
}
