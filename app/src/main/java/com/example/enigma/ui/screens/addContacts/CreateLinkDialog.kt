package com.example.enigma.ui.screens.addContacts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.enigma.R
import com.example.enigma.models.CreatedSharedData
import com.example.enigma.ui.screens.common.DialogContentTemplate
import com.example.enigma.ui.screens.common.IndeterminateCircularIndicator
import com.example.enigma.util.DatabaseRequestState
import com.example.enigma.util.PrettyDateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLinkDialog(
    sharedData: DatabaseRequestState<CreatedSharedData>,
    onConfirmButtonClick: () -> Unit
) {
    if(sharedData !is DatabaseRequestState.Idle) {
        val title = when (sharedData)
        {
            is DatabaseRequestState.Success -> stringResource(
                id = R.string.link_successfully_created
            )
            is DatabaseRequestState.Loading -> stringResource(
                id = R.string.loading
            )
            is DatabaseRequestState.Error -> stringResource(
                id = R.string.failure
            )
            else -> ""
        }
        val body = when(sharedData)
        {
            is DatabaseRequestState.Success -> stringResource(
                id = R.string.copy_link_or_share_to_other_apps
            )
            is DatabaseRequestState.Loading -> ""
            is DatabaseRequestState.Error -> stringResource(
                id = R.string.link_could_not_be_created
            )
            else -> ""
        }

        val link = if(sharedData is DatabaseRequestState.Success)
            sharedData.data.resourceUrl ?: "Link not available"
        else
            "Link not available"
        val context = LocalContext.current

        BasicAlertDialog(
            onDismissRequest = { },
            content = {
                DialogContentTemplate(
                    title = title,
                    body = body,
                    content = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ){
                            when(sharedData)
                            {
                                is DatabaseRequestState.Success -> {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        val validUntil = PrettyDateFormatter.prettyTimeFormat(
                                            sharedData.data.validUntil
                                        )
                                        if(validUntil != null)
                                        {
                                            Text(
                                                text = stringResource(
                                                    id = R.string.link_valid_for)
                                                    .format(validUntil),
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        Text(
                                            text = link,
                                            maxLines = 5,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    try {
                                                        val intent = Intent(Intent.ACTION_SEND)
                                                        intent.type = "text/plain"
                                                        intent.putExtra(Intent.EXTRA_TEXT, link)
                                                        context.startActivity(
                                                            Intent.createChooser(
                                                                intent,
                                                                "Share link via"
                                                            )
                                                        )
                                                    }
                                                    catch (_: Exception)
                                                    {
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to share link.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Share,
                                                    contentDescription = stringResource(
                                                        id = R.string.share
                                                    )
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    try {
                                                        val clipboard = context.getSystemService(
                                                            Context.CLIPBOARD_SERVICE
                                                        ) as ClipboardManager
                                                        val data = ClipData.newPlainText("", link)
                                                        clipboard.setPrimaryClip(data)
                                                    }
                                                    catch (_: Exception)
                                                    {
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to copy link to clipboard.",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    painter = painterResource(
                                                        id = R.drawable.ic_copy
                                                    ),
                                                    contentDescription = stringResource(
                                                        id = R.string.copy
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                                is DatabaseRequestState.Loading -> IndeterminateCircularIndicator(
                                    visible = true,
                                    text = stringResource(
                                        id = R.string.loading
                                    )
                                )
                                is DatabaseRequestState.Error -> {
                                    Icon(
                                        modifier = Modifier.size(64.dp),
                                        painter = painterResource(
                                            id = R.drawable.ic_error
                                        ),
                                        contentDescription = stringResource(
                                            id = R.string.error
                                        )
                                    )
                                }
                                else -> { }
                            }
                        }
                    },
                    onPositiveButtonClicked = onConfirmButtonClick,
                    onNegativeButtonClicked = { },
                    dismissible = false,
                    positiveButtonText = stringResource(
                        id = R.string.ok
                    )
                )
            },
            properties = DialogProperties(
                dismissOnClickOutside = false
            )
        )
    }
}

@Composable
fun CreateLinkDialog(
    visible: Boolean,
    sharedData: DatabaseRequestState<CreatedSharedData>,
    onConfirmButtonClick: () -> Unit
) {
    if(visible)
    {
        CreateLinkDialog(
            sharedData = sharedData,
            onConfirmButtonClick = onConfirmButtonClick
        )
    }
}

@Preview
@Composable
fun CreateLinkDialogPreview()
{
    CreateLinkDialog(
        sharedData = DatabaseRequestState.Success(
            CreatedSharedData(
                "123-123-123-123",
                resourceUrl = "https://example.com/Share?Tag=123-123-123-123",
                "2024-06-22 17:43:04.2399895+03:00"
            )
        ),
        onConfirmButtonClick = { }
    )
}
