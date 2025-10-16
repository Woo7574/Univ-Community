package com.example.univ_community.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.univ_community.R
import com.example.univ_community.ui.navigation.Screen
import com.example.univ_community.ui.theme.Univ_CommunityTheme
import com.example.univ_community.ui.viewmodel.ProfileViewModel

@Composable
fun PersonalSpaceScreen(navController: NavController, profileViewModel: ProfileViewModel) {
    val profile by profileViewModel.profile.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> profileViewModel.setProfileImage(uri) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "내 공간",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        ProfileCard(profile = profile, onImageClick = { imagePickerLauncher.launch("image/*") })

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PersonalSpaceMenuItem(
                title = "To-Do 리스트",
                icon = Icons.AutoMirrored.Filled.List,
                onClick = { /* TODO: To-Do 리스트 화면으로 이동 */ }
            )
            PersonalSpaceMenuItem(
                title = "노트",
                icon = Icons.Default.Edit,
                onClick = { /* TODO: 노트 화면으로 이동 */ }
            )
            PersonalSpaceMenuItem(
                title = "캘린더",
                icon = Icons.Default.DateRange,
                onClick = { /* TODO: 캘린더 화면으로 이동 */ }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PersonalSpaceMenuItem(
            title = "설정",
            icon = Icons.Default.Settings,
            onClick = { navController.navigate(Screen.Settings.route) }
        )
    }
}

@Composable
fun ProfileCard(profile: com.example.univ_community.ui.viewmodel.Profile, onImageClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable(onClick = onImageClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (profile.profileImageUri != null) rememberAsyncImagePainter(profile.profileImageUri) else painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "프로필 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(text = profile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = profile.statusMessage, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "주요 기술: ${profile.skills.joinToString()}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PersonalSpaceMenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalSpaceScreenPreview() {
    Univ_CommunityTheme {
        PersonalSpaceScreen(navController = rememberNavController(), profileViewModel = viewModel())
    }
}
