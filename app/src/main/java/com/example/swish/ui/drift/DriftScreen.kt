package com.example.swish.ui.drift

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.swish.data.model.User
import com.example.swish.ui.SwishViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriftScreen(viewModel: SwishViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val currentUser = viewModel.currentUser.value
    
    val searchResults = if (searchQuery.isNotEmpty()) {
        viewModel.allUsers.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.username.contains(searchQuery, ignoreCase = true)
        }
    } else {
        viewModel.allUsers.filter { it.id != currentUser.id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Drift", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search users to follow...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(24.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(searchResults) { user ->
                    UserSearchItem(
                        user = user,
                        isFollowing = currentUser.following.contains(user.id),
                        onFollowClick = { viewModel.followUser(user.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun UserSearchItem(
    user: User,
    isFollowing: Boolean,
    onFollowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            if (user.photoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(user.photoUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    tint = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = user.name, fontWeight = FontWeight.Bold)
            Text(text = "@${user.username}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        
        Button(
            onClick = onFollowClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(if (isFollowing) "Following" else "Follow")
        }
    }
}
