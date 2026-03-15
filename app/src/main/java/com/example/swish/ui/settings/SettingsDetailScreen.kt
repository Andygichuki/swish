package com.example.swish.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDetailScreen(
    title: String,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (title) {
                "Account privacy" -> AccountPrivacyContent()
                "Notifications" -> NotificationsContent()
                "Language" -> LanguageContent()
                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Settings for $title will appear here.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun AccountPrivacyContent() {
    var isPrivate by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Private account", fontWeight = FontWeight.Bold)
                Text(
                    "When your account is private, only people you approve can see your photos and videos.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = isPrivate, onCheckedChange = { isPrivate = it })
        }
    }
}

@Composable
fun NotificationsContent() {
    Column {
        Text("Push Notifications", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        NotificationToggle("Pause all")
        NotificationToggle("Posts, stories and comments")
        NotificationToggle("Following and followers")
        NotificationToggle("Messages")
        NotificationToggle("Calls")
    }
}

@Composable
fun NotificationToggle(label: String) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
fun LanguageContent() {
    val languages = listOf("English", "Spanish", "French", "German", "Chinese", "Japanese")
    var selectedLanguage by remember { mutableStateOf("English") }
    
    LazyColumn {
        items(languages.size) { index ->
            val lang = languages[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedLanguage = lang }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(lang)
                RadioButton(selected = selectedLanguage == lang, onClick = { selectedLanguage = lang })
            }
        }
    }
}
