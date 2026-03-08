package com.example.swish.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val status: String = "Hey there! I'm using Swish",
    val isOnline: Boolean = false,
    val phoneNumber: String = "",
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val type: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val isRead: Boolean = false,
    val isDelivered: Boolean = false,
    val reactions: Map<String, String> = emptyMap()
)

enum class MessageType {
    TEXT, IMAGE, FILE, VOICE
}

data class Chat(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageSenderId: String = "",
    val unreadCount: Map<String, Int> = emptyMap(),
    val isGroup: Boolean = false,
    val groupName: String? = null,
    val groupPhoto: String? = null,
    val typingUsers: List<String> = emptyList()
)

data class Story(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val mediaUrl: String = "",
    val viewers: List<String> = emptyList()
)

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val imageUrl: String = "",
    val caption: String = "",
    val likes: List<String> = emptyList(),
    val comments: Int = 0
)

data class Reel(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val videoUrl: String = "",
    val caption: String = "",
    val musicName: String = "",
    val likes: List<String> = emptyList(),
    val comments: Int = 0
)
