package com.example.swish.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings and privacy", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SettingsSectionTitle("Your account")
                SettingsItem(Icons.Outlined.Person, "Account Center", "Password, security, personal details, ad preferences") {
                    onNavigateToDetail("Account Center")
                }
            }

            item {
                SettingsSectionTitle("How you use Swish")
                SettingsItem(Icons.Outlined.Notifications, "Notifications") {
                    onNavigateToDetail("Notifications")
                }
                SettingsItem(Icons.Outlined.History, "Time spent") {
                    onNavigateToDetail("Time spent")
                }
            }

            item {
                SettingsSectionTitle("What you see")
                SettingsItem(Icons.Outlined.FavoriteBorder, "Favorites") {
                    onNavigateToDetail("Favorites")
                }
                SettingsItem(Icons.Outlined.Block, "Muted accounts") {
                    onNavigateToDetail("Muted accounts")
                }
                SettingsItem(Icons.Outlined.VisibilityOff, "Hide like and share counts") {
                    onNavigateToDetail("Hide counts")
                }
            }

            item {
                SettingsSectionTitle("Who can see your content")
                SettingsItem(Icons.Outlined.Lock, "Account privacy", "Public") {
                    onNavigateToDetail("Account privacy")
                }
                SettingsItem(Icons.Outlined.StarBorder, "Close Friends") {
                    onNavigateToDetail("Close Friends")
                }
                SettingsItem(Icons.Outlined.Block, "Blocked") {
                    onNavigateToDetail("Blocked")
                }
                SettingsItem(Icons.Outlined.History, "Hide story and live") {
                    onNavigateToDetail("Hide story")
                }
            }

            item {
                SettingsSectionTitle("How others can interact with you")
                SettingsItem(Icons.AutoMirrored.Outlined.Message, "Messages and story replies") {
                    onNavigateToDetail("Messages")
                }
                SettingsItem(Icons.Outlined.AlternateEmail, "Tags and mentions") {
                    onNavigateToDetail("Tags")
                }
                SettingsItem(Icons.AutoMirrored.Outlined.Comment, "Comments") {
                    onNavigateToDetail("Comments")
                }
                SettingsItem(Icons.Outlined.Share, "Sharing and remixes") {
                    onNavigateToDetail("Sharing")
                }
                SettingsItem(Icons.Outlined.NoAccounts, "Restricted accounts") {
                    onNavigateToDetail("Restricted accounts")
                }
            }

            item {
                SettingsSectionTitle("Your app and media")
                SettingsItem(Icons.Outlined.FileDownload, "Archiving and downloading") {
                    onNavigateToDetail("Archiving")
                }
                SettingsItem(Icons.Outlined.Language, "Language") {
                    onNavigateToDetail("Language")
                }
                SettingsItem(Icons.Outlined.BarChart, "Data usage and media quality") {
                    onNavigateToDetail("Data usage")
                }
            }

            item {
                SettingsSectionTitle("For families")
                SettingsItem(Icons.Outlined.FamilyRestroom, "Supervision") {
                    onNavigateToDetail("Supervision")
                }
            }

            item {
                SettingsSectionTitle("More info and support")
                SettingsItem(Icons.AutoMirrored.Outlined.HelpOutline, "Help") {
                    onNavigateToDetail("Help")
                }
                SettingsItem(Icons.Outlined.Info, "About") {
                    onNavigateToDetail("About")
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Log out",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLogout() }
                        .padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        color = Color.Gray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 16.sp)
            if (subtitle != null) {
                Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
    }
}
