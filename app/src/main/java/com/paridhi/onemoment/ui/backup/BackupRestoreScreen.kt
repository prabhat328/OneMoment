package com.paridhi.onemoment.ui.backup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.security.SecureRandom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreScreen(
    viewModel: BackupRestoreViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val backupMetadata by viewModel.backupMetadata.collectAsState()
    val selectedRestoreUri by viewModel.selectedRestoreUri.collectAsState()

    var showBackupDialog by remember { mutableStateOf(false) }
    var showRestoreDialog by remember { mutableStateOf(false) }
    var selectedBackupUri by remember { mutableStateOf<Uri?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val createBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        if (uri != null) {
            selectedBackupUri = uri
            showBackupDialog = true
        }
    }

    val openBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.verifyBackupFile(uri)
        }
    }

    LaunchedEffect(selectedRestoreUri, uiState) {
        if (selectedRestoreUri != null && uiState is BackupRestoreState.Idle) {
            showRestoreDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 32.dp, bottom = 100.dp)
    ) {
        Text(
            text = "Backup & Restore",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Text(
            text = "Keep your memories safe",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        BackupSection(title = "Backup") {
            BackupOptionItem(
                icon = Icons.Default.CloudUpload,
                title = "Cloud Backup",
                subtitle = "Save securely to Google Drive. Restores automatically on any device.",
                label = "Coming soon",
                enabled = false,
                onClick = { }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            BackupOptionItem(
                icon = Icons.Default.Save,
                title = "Local File",
                subtitle = "Save a file you can move yourself. No account needed.",
                enabled = true,
                onClick = {
                    val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                    createBackupLauncher.launch("OneMoment_Backup_$date.ombk")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        BackupSection(title = "Restore") {
            BackupOptionItem(
                icon = Icons.Default.CloudUpload,
                title = "Restore from Cloud",
                subtitle = "Restore memories from Google Drive.",
                label = "Coming soon",
                enabled = false,
                onClick = { }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            BackupOptionItem(
                icon = Icons.Default.Download,
                title = "Restore from File",
                subtitle = "Load memories from a local .ombk backup file.",
                enabled = true,
                onClick = {
                    openBackupLauncher.launch(arrayOf("application/octet-stream", "*/*"))
                }
            )
        }

        if (uiState is BackupRestoreState.Loading) {
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (uiState is BackupRestoreState.Success) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (uiState as BackupRestoreState.Success).message,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (uiState is BackupRestoreState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (uiState as BackupRestoreState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 24.dp)
            )
        }
    }

    if (showBackupDialog && selectedBackupUri != null) {
        PasswordDialog(
            title = "Secure your Backup",
            description = "Enter a password to encrypt your backup. If you lose this password, your backup cannot be restored.",
            confirmText = "Create Backup",
            onDismiss = { showBackupDialog = false },
            onConfirm = { password ->
                showBackupDialog = false
                viewModel.backupToFile(selectedBackupUri!!, password.toCharArray())
            }
        )
    }

    if (showRestoreDialog && backupMetadata != null) {
        val metadata = backupMetadata!!
        val dateString = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(Date(metadata.date))
        var showMergePrompt by remember { mutableStateOf(false) }
        var decryptedMemories by remember { mutableStateOf<List<com.paridhi.onemoment.data.local.MemoryEntity>?>(null) }

        if (showMergePrompt && decryptedMemories != null) {
            AlertDialog(
                onDismissRequest = {
                    showRestoreDialog = false
                    viewModel.clearSelection()
                },
                title = { Text("Merge or Replace?") },
                text = { Text("You already have memories on this device. Do you want to replace them with the backup, or merge the backup alongside them?") },
                confirmButton = {
                    TextButton(onClick = {
                        showRestoreDialog = false
                        viewModel.saveRestoredMemories(decryptedMemories!!, replaceExisting = false)
                    }) {
                        Text("Merge")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showRestoreDialog = false
                        viewModel.saveRestoredMemories(decryptedMemories!!, replaceExisting = true)
                    }) {
                        Text("Replace", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        } else {
            PasswordDialog(
                title = "Restore Backup",
                description = "Backup from $dateString\nContains ${metadata.entryCount} memories.\n\nEnter the password to decrypt.",
                confirmText = "Restore",
                onDismiss = {
                    showRestoreDialog = false
                    viewModel.clearSelection()
                },
                onConfirm = { password ->
                    scope.launch {
                        val result = viewModel.verifyPasswordAndGetDecryptedMemories(password.toCharArray())
                        if (result.isSuccess) {
                            val memories = result.getOrNull() ?: emptyList()
                            val count = viewModel.getMemoriesCount()
                            if (count > 0) {
                                decryptedMemories = memories
                                showMergePrompt = true
                            } else {
                                showRestoreDialog = false
                                viewModel.saveRestoredMemories(memories, replaceExisting = false)
                            }
                        } else {
                            showRestoreDialog = false
                            viewModel.clearSelection()
                            viewModel.setDecryptionError()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BackupSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun BackupOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    label: String? = null,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    if (label != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.5f)
                )
            }
        }
    }
}

@Composable
fun PasswordDialog(
    title: String,
    description: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Text(description)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = {
                        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*"
                        val random = SecureRandom()
                        password = (1..16).map { chars[random.nextInt(chars.length)] }.joinToString("")
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Generate Strong Passphrase")
                }
                if (title.contains("Secure")) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Write this down! Cannot be recovered.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(password) },
                enabled = password.isNotBlank()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
