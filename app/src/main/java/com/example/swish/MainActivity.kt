package com.example.swish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.swish.ui.MainContainer
import com.example.swish.ui.auth.LoginScreen
import com.example.swish.ui.auth.SignUpScreen
import com.example.swish.ui.chat.ChatScreen
import com.example.swish.ui.theme.MessengerTheme
import com.example.swish.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            MessengerTheme(
                darkTheme = themeViewModel.isDarkTheme.value
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SwishApp()
                }
            }
        }
    }
}

@Composable
fun SwishApp() {
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }
    
    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    isAuthenticated = true
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                }
            )
        }
        
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    isAuthenticated = true
                    navController.navigate("main") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("main") {
            MainContainer(
                onLogout = {
                    isAuthenticated = false
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onChatClick = { chatId, userId ->
                    navController.navigate("chat/$chatId/$userId")
                }
            )
        }
        
        composable("chat/{chatId}/{userId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ChatScreen(
                chatId = chatId,
                otherUserId = userId,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}
