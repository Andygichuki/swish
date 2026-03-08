package com.example.swish.ui.chats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
// import com.google.firebase.Timestamp
// import com.google.firebase.auth.ktx.auth
// import com.google.firebase.firestore.ktx.firestore
// import com.google.firebase.ktx.Firebase
import com.example.swish.data.model.Chat
import com.example.swish.data.model.User
import com.example.swish.data.model.Story
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsListScreen(
    onChatClick: (String, String) -> Unit,
    onProfileClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    // val db = Firebase.firestore
    // val currentUserId = Firebase.auth.currentUser?.uid ?: ""
    val currentUserId = "mock_user_id"
    
    var chats by remember { mutableStateOf<List<Pair<Chat, User>>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var stories by remember { mutableStateOf<List<Story>>(emptyList()) }
    
    // Fetch chats (Mocked)
    LaunchedEffect(Unit) {
        val mockUser = User(id = "other_user", name = "John Doe", isOnline = true)
        val mockChat = Chat(
            id = "chat_1",
            participants = listOf(currentUserId, "other_user"),
            lastMessage = "Hello there!",
            // lastMessageTimestamp = Timestamp.now()
        )
        chats = listOf(Pair(mockChat, mockUser))
    }
    
    /*
    // Fetch chats in real-time
    DisposableEffect(Unit) {
        val listener = db.collection("chats")
            .whereArrayContains("participants", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                snapshot?.documents?.let { docs ->
                    val chatList = mutableListOf<Pair<Chat, User>>()
                    var loadedCount = 0
                    if (docs.isEmpty()) {
                        chats = emptyList()
                    }
                    docs.forEach { doc ->
                        val chat = Chat(
                            id = doc.id,
                            participants = doc.get("participants") as? List<String> ?: emptyList(),
                            lastMessage = doc.getString("lastMessage") ?: "",
                            lastMessageTimestamp = doc.getTimestamp("lastMessageTimestamp") ?: Timestamp.now(),
                            unreadCount = doc.get("unreadCount") as? Map<String, Int> ?: emptyMap(),
                            lastMessageSenderId = doc.getString("lastMessageSenderId") ?: ""
                        )
                        
                        val otherUserId = chat.participants.find { it != currentUserId }
                        if (otherUserId != null) {
                            db.collection("users").document(otherUserId).get()
                                .addOnSuccessListener { userDoc ->
                                    val user = User(
                                        id = userDoc.id,
                                        name = userDoc.getString("name") ?: "",
                                        photoUrl = userDoc.getString("photoUrl") ?: "",
                                        isOnline = userDoc.getBoolean("isOnline") ?: false
                                    )
                                    chatList.add(Pair(chat, user))
                                    loadedCount++
                                    if (loadedCount == docs.size) {
                                        chats = chatList.sortedByDescending { it.first.lastMessageTimestamp }
                                    }
                                }
                        } else {
                            loadedCount++
                            if (loadedCount == docs.size) {
                                chats = chatList.sortedByDescending { it.first.lastMessageTimestamp }
                            }
                        }
                    }
                }
            }
        
        onDispose {
            listener.remove()
        }
    }
    */
    
    /*
    // Fetch stories
    LaunchedEffect(Unit) {
        db.collection("stories")
            .whereGreaterThan("expiresAt", Timestamp.now())
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                stories = snapshot?.documents?.map { doc ->
                    Story(
                        id = doc.id,
                        userId = doc.getString("userId") ?: "",
                        mediaUrl = doc.getString("mediaUrl") ?: "",
                        timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now(),
                        viewers = doc.get("viewers") as? List<String> ?: emptyList()
                    )
                }?.filter { !it.viewers.contains(currentUserId) } ?: emptyList()
            }
    }
    */
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chats", style = MaterialTheme.typography.titleLarge)
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Edit, contentDescription = "New chat", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search conversations...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            )
            
            // Stories row
            if (stories.isNotEmpty()) {
                StoriesRow(stories = stories)
            }
            
            // Chats list
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(chats) { (chat, user) ->
                    if (searchQuery.isEmpty() || 
                        user.name.contains(searchQuery, ignoreCase = true)) {
                        ChatItem(
                            chat = chat,
                            user = user,
                            currentUserId = currentUserId,
                            onClick = {
                                onChatClick(chat.id, user.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    user: User,
    currentUserId: String,
    onClick: () -> Unit
) {
    val unreadCount = chat.unreadCount[currentUserId] ?: 0
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image with online indicator
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                if (user.photoUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(user.photoUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                if (user.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .align(Alignment.BottomEnd)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Chat info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Text(
                        text = "10:00", // formatter.format(chat.lastMessageTimestamp.toDate()),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (chat.lastMessageSenderId == currentUserId) 
                            "You: ${chat.lastMessage}" 
                        else 
                            chat.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (unreadCount > 0)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = unreadCount.toString(),
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoriesRow(stories: List<Story>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Stories",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(stories) { story ->
                StoryItem(story = story)
            }
        }
    }
}

@Composable
fun StoryItem(story: Story) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                )
            )
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = rememberAsyncImagePainter(story.mediaUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
