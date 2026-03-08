package com.example.swish.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.swish.ui.chats.ChatsListScreen
import com.example.swish.ui.profile.ProfileScreen
import com.example.swish.ui.drift.DriftScreen
import com.example.swish.ui.swift.SwiftScreen
import com.example.swish.ui.surge.SurgeScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment

sealed class Screen(val route: String, val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    object Swish : Screen("swish", "Swish", Icons.Filled.Mail, Icons.Outlined.Mail)
    object Drift : Screen("drift", "Drift", Icons.Filled.Explore, Icons.Outlined.Explore)
    object Swift : Screen("swift", "Swift", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline)
    object Surge : Screen("surge", "Surge", Icons.Filled.Bolt, Icons.Outlined.Bolt)
    object Vault : Screen("vault", "Vault", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
}

@Composable
fun MainContainer(
    onLogout: () -> Unit,
    onChatClick: (String, String) -> Unit
) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Swish,
        Screen.Drift,
        Screen.Swift,
        Screen.Surge,
        Screen.Vault
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Swish.route, Modifier.padding(innerPadding)) {
            composable(Screen.Swish.route) {
                ChatsListScreen(
                    onChatClick = onChatClick,
                    onProfileClick = { navController.navigate(Screen.Vault.route) },
                    onNewChatClick = { navController.navigate(Screen.Swift.route) }
                )
            }
            composable(Screen.Drift.route) {
                DriftScreen()
            }
            composable(Screen.Swift.route) {
                SwiftScreen(
                    onNewPost = { /* TODO */ },
                    onNewChat = { navController.navigate(Screen.Swish.route) },
                    onNewStory = { /* TODO */ }
                )
            }
            composable(Screen.Surge.route) {
                SurgeScreen()
            }
            composable(Screen.Vault.route) {
                ProfileScreen(
                    onBackPressed = { navController.popBackStack() },
                    onLogout = onLogout
                )
            }
        }
    }
}
