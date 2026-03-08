package com.example.swish.ui.surge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swish.data.model.Reel

@Composable
fun SurgeScreen() {
    val reels = remember {
        listOf(
            Reel(
                id = "1",
                userName = "travel_bug",
                caption = "Catching the sunset #swish #surge",
                musicName = "Golden Hour - JVKE",
                likes = listOf("1", "2"),
                comments = 45
            ),
            Reel(
                id = "2",
                userName = "tech_wiz",
                caption = "New Swish features are insane! 🔥",
                musicName = "Original Audio",
                likes = listOf("1"),
                comments = 12
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reels) { reel ->
                Box(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .background(Color.DarkGray)
                ) {
                    ReelItemContent(reel)
                }
            }
        }
        
        Text(
            text = "Surge",
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ReelItemContent(reel: Reel) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Placeholder for video
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White.copy(alpha = 0.2f), modifier = Modifier.size(100.dp))
        }

        // Bottom Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        // Right actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReelActionItem(Icons.Default.Favorite, "${reel.likes.size}K")
            Spacer(modifier = Modifier.height(20.dp))
            ReelActionItem(Icons.AutoMirrored.Filled.Comment, "${reel.comments}")
            Spacer(modifier = Modifier.height(20.dp))
            ReelActionItem(Icons.Default.Share, "")
        }

        // Bottom Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = reel.userName, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = { /* Follow */ },
                    modifier = Modifier.height(30.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Follow", color = Color.White, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = reel.caption, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = reel.musicName, color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ReelActionItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { /* Action */ }) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        if (label.isNotEmpty()) {
            Text(text = label, color = Color.White, fontSize = 12.sp)
        }
    }
}
