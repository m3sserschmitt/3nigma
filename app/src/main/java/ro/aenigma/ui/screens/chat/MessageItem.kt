package ro.aenigma.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ro.aenigma.R
import ro.aenigma.data.database.MessageEntity
import ro.aenigma.ui.screens.common.selectable
import ro.aenigma.util.PrettyDateFormatter

@Composable
fun MessageItem(
    isSelectionMode: Boolean,
    isSelected: Boolean,
    isSent: Boolean,
    onItemSelected: (MessageEntity) -> Unit,
    onItemDeselected: (MessageEntity) -> Unit,
    onClick: () -> Unit,
    message: MessageEntity
) {
    val paddingStart = if (message.incoming) 8.dp else 50.dp
    val paddingEnd = if (message.incoming) 50.dp else 8.dp
    val contentColor = if (message.incoming)
        MaterialTheme.colorScheme.onSecondaryContainer
    else
        MaterialTheme.colorScheme.onPrimaryContainer
    val containerColor = if (message.incoming)
        MaterialTheme.colorScheme.secondaryContainer
    else
        MaterialTheme.colorScheme.primaryContainer
    Box(
        modifier = Modifier.fillMaxWidth().padding(paddingStart, 8.dp, paddingEnd, 0.dp),
        contentAlignment = if (message.incoming) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        Surface(
            modifier = Modifier
                .selectable(
                    item = message,
                    isSelectionMode = isSelectionMode,
                    isSelected = isSelected,
                    onItemSelected = onItemSelected,
                    onItemDeselected = onItemDeselected,
                    onClick = onClick
                ),
            color = containerColor,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelectionMode) {
                    if (isSelected) {
                        Icon(
                            modifier = Modifier.alpha(.5f),
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = stringResource(R.string.message_selection),
                            tint = contentColor,
                        )
                    } else {
                        Icon(
                            modifier = Modifier.alpha(.5f),
                            painter = painterResource(id = R.drawable.ic_radio_button_unchecked),
                            contentDescription = stringResource(R.string.message_selection),
                            tint = contentColor,
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(8.dp).width(IntrinsicSize.Max)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = message.text,
                        textAlign = if (message.incoming) TextAlign.Start else TextAlign.End,
                        color = contentColor,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            textAlign = TextAlign.End,
                            text = PrettyDateFormatter.getTime(message.date),
                            color = contentColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (!message.incoming && !isSent) {
                            Icon(
                                modifier = Modifier.size(12.dp).alpha(.5f),
                                imageVector = Icons.Outlined.Done,
                                contentDescription = stringResource(R.string.message_sent),
                                tint = contentColor
                            )
                        } else if (!message.incoming) {
                            Icon(
                                modifier = Modifier.size(16.dp).alpha(.5f),
                                painter = painterResource(R.drawable.ic_timer),
                                contentDescription = stringResource(R.string.message_sent),
                                tint = contentColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MessageItemPreview()
{
    MessageItem(
        isSelectionMode = true,
        isSelected = false,
        isSent = true,
        message = MessageEntity(
            "123-123-123-123",
            "Hello",
            incoming = true,
            sent = true
        ),
        onItemDeselected = {},
        onClick = {},
        onItemSelected = {}
    )
}

@Composable
@Preview
fun MessageItemSelectedPreview()
{
    MessageItem(
        isSelectionMode = true,
        isSelected = true,
        isSent = true,
        message = MessageEntity(
            "123-123-123-123",
            "Hello",
            incoming = true,
            sent = false
        ),
        onItemDeselected = {},
        onClick = {},
        onItemSelected = {}
    )
}

@Composable
@Preview
fun MessageItemPending()
{
    MessageItem(
        isSelectionMode = false,
        isSelected = false,
        isSent = false,
        message = MessageEntity(
            "123-123-123-123",
            "Hello",
            incoming = false,
            sent = false
        ),
        onItemDeselected = {},
        onClick = {},
        onItemSelected = {}
    )
}

@Composable
@Preview
fun MessageItemSent()
{
    MessageItem(
        isSelectionMode = false,
        isSelected = false,
        isSent = true,
        message = MessageEntity(
            "123-123-123-123",
            "Hello",
            incoming = false,
            sent = true
        ),
        onItemDeselected = {},
        onClick = {},
        onItemSelected = {}
    )
}
