package com.example.enigma.ui.screens.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.enigma.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    text: String
) {
    SimpleInfoScreen(
        modifier = modifier,
        message = text,
        icon = {
            Icon(
                modifier = Modifier.size(120.dp),
                painter = painterResource(
                    id = R.drawable.ic_error
                ),
                contentDescription = stringResource(
                    id = R.string.something_went_wrong
                )
            )
        }
    )
}

@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier
) {
    ErrorScreen(
        modifier = modifier,
        text = stringResource(
            id = R.string.something_went_wrong
        )
    )
}

@Preview
@Composable
fun ErrorScreenPreview()
{
    GenericErrorScreen()
}
