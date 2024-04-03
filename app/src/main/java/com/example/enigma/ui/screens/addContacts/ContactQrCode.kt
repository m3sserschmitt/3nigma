package com.example.enigma.ui.screens.addContacts

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.enigma.R

@Composable
fun ContactQrCode(
    modifier: Modifier = Modifier,
    qrCode: Bitmap
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(horizontal = 28.dp),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            text = stringResource(
                id = R.string.qr_code_caption
            )
        )
        HorizontalDivider(
            color = MaterialTheme.colorScheme.background,
            thickness = 32.dp
        )
        Image(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(48.dp))
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth,

            bitmap = qrCode.asImageBitmap(),
            contentDescription = stringResource(
                id = R.string.contact_qr_code
            )
        )
    }
}

