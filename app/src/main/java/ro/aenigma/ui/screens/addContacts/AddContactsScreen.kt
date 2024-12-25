package ro.aenigma.ui.screens.addContacts

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ro.aenigma.R
import ro.aenigma.models.CreatedSharedData
import ro.aenigma.models.SharedData
import ro.aenigma.ui.navigation.Screens
import ro.aenigma.ui.screens.common.StandardAppBar
import ro.aenigma.util.DatabaseRequestState
import ro.aenigma.util.QrCodeGenerator
import ro.aenigma.util.QrCodeScannerState
import ro.aenigma.viewmodels.MainViewModel

@Composable
fun AddContactsScreen(
    profileToShare: String,
    navigateToContactsScreen: () -> Unit,
    mainViewModel: MainViewModel
) {
    var scannerState by remember { mutableStateOf(QrCodeScannerState.SHARE_CODE) }
    val qrCode by mainViewModel.qrCode.collectAsState()
    val sharedDataCreate by mainViewModel.sharedDataCreateResult.collectAsState()
    val qrCodeLabel by mainViewModel.qrCodeLabel.collectAsState()
    val sharedDataGet by mainViewModel.sharedDataRequest.collectAsState()
    val floatingButtonVisible = profileToShare == Screens.ADD_CONTACT_SCREEN_SHARE_MY_CODE_ARG_VALUE
    val context = LocalContext.current

    LaunchedEffect(key1 = true)
    {
        mainViewModel.generateCode(profileToShare)
    }

    AddContactsScreen(
        scannerState = scannerState,
        qrCode = qrCode,
        sharedDataCreate = sharedDataCreate,
        sharedDataGet = sharedDataGet,
        qrCodeLabel = qrCodeLabel,
        floatingButtonVisible = floatingButtonVisible,
        onScannerStateChanged = {
            newState -> scannerState = newState
        },
        onQrCodeFound = {
            scannedData -> if(mainViewModel.setScannedContactDetails(scannedData))
            scannerState = QrCodeScannerState.SAVE
        },
        onSaveContact = {
            scannerState = QrCodeScannerState.SHARE_CODE
            mainViewModel.saveContactChanges()
            Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_SHORT).show()
            navigateToContactsScreen()
        },
        onSaveContactDismissed = {
            mainViewModel.cleanupContactChanges()
            scannerState = QrCodeScannerState.SHARE_CODE
        },
        onNewContactNameChanged = {
            newContactName -> mainViewModel.setNewContactName(newContactName)
        },
        onCreateLinkClicked = {
            mainViewModel.createContactShareLink()
        },
        onGetLink = {
            url -> mainViewModel.openContactSharedData(url)
        },
        onSharedDataConfirm = {
            mainViewModel.cleanupContactChanges()
        },
        navigateToContactsScreen = navigateToContactsScreen
    )
}

@Composable
fun AddContactsScreen(
    scannerState: QrCodeScannerState,
    qrCode: DatabaseRequestState<Bitmap>,
    sharedDataCreate: DatabaseRequestState<CreatedSharedData>,
    sharedDataGet: DatabaseRequestState<SharedData>,
    qrCodeLabel: String,
    floatingButtonVisible: Boolean,
    onScannerStateChanged: (QrCodeScannerState) -> Unit,
    onQrCodeFound: (String) -> Unit,
    onSaveContact: () -> Unit,
    onSaveContactDismissed: () -> Unit,
    onNewContactNameChanged: (String) -> Boolean,
    onCreateLinkClicked: () -> Unit,
    onGetLink: (String) -> Unit,
    onSharedDataConfirm: () -> Unit,
    navigateToContactsScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            StandardAppBar(
                title = if (scannerState == QrCodeScannerState.SCAN_CODE)
                    stringResource(R.string.scan_qr_code)
                else
                    stringResource(R.string.add_contacts),
                navigateBack = navigateToContactsScreen,
                transparent = scannerState == QrCodeScannerState.SCAN_CODE
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
                sharedDataCreate = sharedDataCreate,
                sharedDataGet = sharedDataGet,
                qrCodeLabel = qrCodeLabel,
                onSaveContact = onSaveContact,
                onSaveContactDismissed = onSaveContactDismissed,
                onQrCodeFound = onQrCodeFound,
                onNewContactNameChanged = onNewContactNameChanged,
                onCreateLinkClicked = onCreateLinkClicked,
                onGetLink = onGetLink,
                onSharedDataConfirm = onSharedDataConfirm
            )
        },
        floatingActionButton = {
            QrScannerFab(
                scannerState = scannerState,
                visible = floatingButtonVisible,
                onClick = {
                    if (scannerState == QrCodeScannerState.SHARE_CODE) {
                        onScannerStateChanged(QrCodeScannerState.SCAN_CODE)
                    } else if (scannerState == QrCodeScannerState.SCAN_CODE) {
                        onScannerStateChanged(QrCodeScannerState.SHARE_CODE)
                    }
                }
            )
        }
    )
}

@Composable
fun QrScannerFab(
    visible: Boolean,
    scannerState: QrCodeScannerState,
    onClick: () -> Unit
) {
    if(visible) {
        FloatingActionButton(
            onClick = {
                onClick()
            }
        ) {
            Icon(
                painter = if (scannerState == QrCodeScannerState.SHARE_CODE)
                    painterResource(id = R.drawable.ic_qr_scanner)
                else
                    painterResource(id = R.drawable.ic_qr_code),
                contentDescription = ""
            )
        }
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
            sharedDataCreate = DatabaseRequestState.Idle,
            sharedDataGet = DatabaseRequestState.Idle,
            qrCodeLabel = "John",
            floatingButtonVisible = true,
            onNewContactNameChanged = { true },
            onQrCodeFound = { },
            onSaveContact = { },
            onScannerStateChanged = { },
            onSaveContactDismissed = { },
            navigateToContactsScreen = { },
            onCreateLinkClicked = { },
            onGetLink = { },
            onSharedDataConfirm = { }
        )
    }
}