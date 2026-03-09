package com.example.swish.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.swish.data.model.Chat
import com.example.swish.data.model.Post
import com.example.swish.data.model.User
import java.util.UUID

class SwishViewModel : ViewModel() {
    // Current User State
    var currentUser = mutableStateOf(
        User(
            id = "me",
            name = "Alex Swish",
            username = "alex_swish",
            status = "Building the future of social 🚀",
            followers = listOf("u1", "u2", "u3"),
            following = listOf("u4")
        )
    )

    // Global Mock Data
    val allUsers = mutableStateListOf(
        User(id = "u1", name = "Jamie Drift", username = "jamie_d", status = "Catching waves"),
        User(id = "u2", name = "Riley Swift", username = "riley_s", status = "Always moving"),
        User(id = "u3", name = "Jordan Surge", username = "j_surge", status = "High energy only"),
        User(id = "u4", name = "Taylor Vault", username = "t_vault", status = "Secure and steady")
    )

    val posts = mutableStateListOf(
        Post(id = "p1", userId = "u1", userName = "Jamie Drift", caption = "Beautiful day!", likes = listOf("me", "u2")),
        Post(id = "p2", userId = "me", userName = "Alex Swish", caption = "Working on Swish App", likes = listOf("u1"))
    )

    val chats = mutableStateListOf(
        Chat(id = "c1", participants = listOf("me", "u1"), lastMessage = "Hey Jamie!", lastMessageSenderId = "me"),
        Chat(id = "c2", participants = listOf("me", "u2"), lastMessage = "Let's catch up soon.", lastMessageSenderId = "u2")
    )

    // Actions
    fun followUser(userId: String) {
        val updatedFollowing = currentUser.value.following.toMutableList()
        if (updatedFollowing.contains(userId)) {
            updatedFollowing.remove(userId)
        } else {
            updatedFollowing.add(userId)
        }
        currentUser.value = currentUser.value.copy(following = updatedFollowing)
    }

    fun addPost(caption: String) {
        val newPost = Post(
            id = UUID.randomUUID().toString(),
            userId = "me",
            userName = currentUser.value.name,
            caption = caption,
            likes = emptyList()
        )
        posts.add(0, newPost)
    }

    fun startChat(otherUser: User): String {
        val existingChat = chats.find { it.participants.contains(otherUser.id) && it.participants.contains("me") }
        if (existingChat != null) return existingChat.id
        
        val newChatId = UUID.randomUUID().toString()
        chats.add(0, Chat(id = newChatId, participants = listOf("me", otherUser.id), lastMessage = "Start of a new conversation"))
        return newChatId
    }
}
