package com.example.swish.ui.chat

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
// import com.google.firebase.Timestamp
// import com.google.firebase.auth.ktx.auth
// import com.google.firebase.firestore.ktx.firestore
// import com.google.firebase.ktx.Firebase
import com.example.swish.data.model.Message
import com.example.swish.data.model.User
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    otherUserId: String,
    onBackPressed: () -> Unit
) {
    // val db = Firebase.firestore
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var newMessage by remember { mutableStateOf("") }
    var otherUser by remember { mutableStateOf<User?>(null) }
    var isTyping by remember { mutableStateOf(false) }
    
    // Fetch messages (Mocked)
    LaunchedEffect(chatId) {
        messages = listOf(
            Message(id = "1", text = "Hey!", senderId = otherUserId),
            Message(id = "2", text = "How are you?", senderId = otherUserId)
        )
    }

    /*
    // Fetch messages in real-time
    DisposableEffect(chatId) {
        val listener = db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                
                messages = snapshot?.documents?.map { doc ->
                    Message(
                        id = doc.id,
                        chatId = doc.getString("chatId") ?: "",
                        senderId = doc.getString("senderId") ?: "",
                        text = doc.getString("text") ?: "",
                        timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now(),
                        isRead = doc.getBoolean("isRead") ?: false
                    )
                } ?: emptyList()
                
                // Scroll to bottom on new message
                if (messages.isNotEmpty()) {
                    scope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }
            }
        
        onDispose {
            listener.remove()
        }
    }
    */
    
    // Fetch other user info (Mocked)
    LaunchedEffect(otherUserId) {
        otherUser = User(id = otherUserId, name = "John Doe", isOnline = true)
        /*
        db.collection("users").document(otherUserId).get()
            .addOnSuccessListener { document ->
                otherUser = User(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    photoUrl = document.getString("photoUrl") ?: "",
                    isOnline = document.getBoolean("isOnline") ?: false
                )
            }
        */
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (otherUser != null) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            if (otherUser!!.photoUrl.isNotEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(otherUser!!.photoUrl),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            
                            if (otherUser!!.isOnline) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(Color.Green)
                                        .align(Alignment.BottomEnd)
                                        .border(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.background,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = otherUser!!.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (isTyping) {
                                Text(
                                    text = "typing...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { /* Video call */ }) {
                    Icon(Icons.Default.Videocam, contentDescription = "Video call")
                }
                IconButton(onClick = { /* Phone call */ }) {
                    Icon(Icons.Default.Phone, contentDescription = "Phone call")
                }
                IconButton(onClick = { /* More options */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = false,
            state = listState
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        
        // Typing indicator
        AnimatedVisibility(
            visible = isTyping,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = "typing...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        // Message input
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Attach file */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { 
                        newMessage = it
                        // db.collection("chats").document(chatId)
                        //    .update("typingUsers", listOf(Firebase.auth.currentUser?.uid))
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Message...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                AnimatedContent(
                    targetState = newMessage.isNotEmpty(),
                    transitionSpec = {
                        fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                    }
                ) { hasMessage ->
                    if (hasMessage) {
                        FloatingActionButton(
                            onClick = {
                                if (newMessage.isNotBlank()) {
                                    // val currentUserId = Firebase.auth.currentUser?.uid ?: ""
                                    val currentUserId = "mock_user_id"
                                    messages = messages + Message(
                                        id = UUID.randomUUID().toString(),
                                        text = newMessage,
                                        senderId = currentUserId
                                    )
                                    /*
                                    val message = mapOf(
                                        "chatId" to chatId,
                                        "senderId" to currentUserId,
                                        "receiverId" to otherUserId,
                                        "text" to newMessage,
                                        "timestamp" to Timestamp.now(),
                                        "isRead" to false,
                                        "isDelivered" to true
                                    )
                                    
                                    db.collection("chats")
                                        .document(chatId)
                                        .collection("messages")
                                        .add(message)
                                    
                                    db.collection("chats")
                                        .document(chatId)
                                        .update(
                                            mapOf(
                                                "lastMessage" to newMessage,
                                                "lastMessageTimestamp" to Timestamp.now(),
                                                "lastMessageSenderId" to currentUserId
                                            )
                                        )
                                    */
                                    
                                    newMessage = ""
                                    // db.collection("chats").document(chatId)
                                    //    .update("typingUsers", emptyList<String>())
                                }
                            },
                            modifier = Modifier.size(48.dp),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    } else {
                        Row {
                            IconButton(
                                onClick = { /* Emoji picker */ },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    Icons.Default.EmojiEmotions,
                                    contentDescription = "Emoji",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            IconButton(
                                onClick = { /* Voice message */ },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    Icons.Default.Mic,
                                    contentDescription = "Voice",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    // val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val isCurrentUser = message.senderId == "mock_user_id"
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isCurrentUser) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isCurrentUser) 
                        Color.White 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "10:00", // formatter.format(message.timestamp.toDate()),
                        color = if (isCurrentUser) 
                            Color.White.copy(alpha = 0.7f) 
                        else 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp
                    )
                    
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (message.isRead) 
                                Icons.Default.DoneAll 
                            else 
                                Icons.Default.Done,
                            contentDescription = if (message.isRead) "Read" else "Sent",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
