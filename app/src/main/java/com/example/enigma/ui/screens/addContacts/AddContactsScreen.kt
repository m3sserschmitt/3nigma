package com.example.enigma.ui.screens.addContacts

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.enigma.R
import com.example.enigma.util.DatabaseRequestState
import com.example.enigma.util.QrCodeGenerator
import com.example.enigma.util.QrCodeScannerState
import com.example.enigma.viewmodels.MainViewModel

@Composable
fun AddContactsScreen(
    navigateToContactsScreen: () -> Unit,
    mainViewModel: MainViewModel
) {
    LaunchedEffect(key1 = true)
    {
        mainViewModel.reset()
        mainViewModel.generateCode()
    }

    var scannerState by remember { mutableStateOf(QrCodeScannerState.SHARE_CODE) }
    val qrCode by mainViewModel.contactQrCode.collectAsState()

    AddContactsScreen(
        scannerState = scannerState,
        qrCode = qrCode,
        onScannerStateChanged = {
            newState -> scannerState = newState
        },
        onQrCodeFound = {
            scannedData -> if(mainViewModel.setScannedContactDetails(scannedData))
            scannerState = QrCodeScannerState.SAVE
        },
        onSaveContact = {
            scannerState = QrCodeScannerState.SHARE_CODE
            mainViewModel.saveNewContact()
        },
        onSaveContactDismissed = {
            mainViewModel.resetNewContactDetails()
            scannerState = QrCodeScannerState.SHARE_CODE
        },
        onNewContactNameChanged = {
            newContactName -> mainViewModel.updateNewContactName(newContactName)
        },
        navigateToContactsScreen = navigateToContactsScreen
    )
}

@Composable
fun AddContactsScreen(
    scannerState: QrCodeScannerState,
    qrCode: DatabaseRequestState<Bitmap>,
    onScannerStateChanged: (QrCodeScannerState) -> Unit,
    onQrCodeFound: (String) -> Unit,
    onSaveContact: () -> Unit,
    onSaveContactDismissed: () -> Unit,
    onNewContactNameChanged: (String) -> Boolean,
    navigateToContactsScreen: () -> Unit
) {
    Scaffold (
        topBar = {
            AddContactsAppBar(
                navigateToContactsScreen = navigateToContactsScreen
            )
        },
        content = { paddingValues ->
            AddContactsContent(
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding()
                    ),
                scannerState = scannerState,
                qrCode = qrCode,
                onSaveContact = onSaveContact,
                onSaveContactDismissed = onSaveContactDismissed,
                onQrCodeFound = onQrCodeFound,
                onNewContactNameChanged = onNewContactNameChanged
            )
        },
        floatingActionButton = {
            QrScannerFab(
                scannerState = scannerState,
                onClick = {
                    if(scannerState == QrCodeScannerState.SHARE_CODE)
                    {
                        onScannerStateChanged(QrCodeScannerState.SCAN_CODE)
                    }
                    else if(scannerState == QrCodeScannerState.SCAN_CODE)
                    {
                        onScannerStateChanged(QrCodeScannerState.SHARE_CODE)
                    }
                }
            )
        }
    )
}

@Composable
fun QrScannerFab(
    scannerState: QrCodeScannerState,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onClick()
        }
    ) {
        Icon(
            painter = if(scannerState == QrCodeScannerState.SHARE_CODE)
                painterResource(id = R.drawable.ic_qr_scanner)
            else
                painterResource(id = R.drawable.ic_qr_code),
            contentDescription = ""
        )
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@Preview
@Composable
fun AddContactsScreenPreview()
{
    val bitmap = QrCodeGenerator(400, 400).encodeAsBitmap("Hello world!")
    if(bitmap != null) {
        AddContactsScreen(
            scannerState = QrCodeScannerState.SHARE_CODE,
            qrCode = DatabaseRequestState.Success(bitmap),
            onNewContactNameChanged = { true },
            onQrCodeFound = { },
            onSaveContact = { },
            onScannerStateChanged = { },
            onSaveContactDismissed = { },
            navigateToContactsScreen = { }
        )
    }
}
