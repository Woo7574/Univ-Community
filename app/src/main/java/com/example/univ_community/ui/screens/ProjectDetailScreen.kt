package com.example.univ_community.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.univ_community.ui.theme.Univ_CommunityTheme
import com.example.univ_community.ui.viewmodel.ProjectViewModel
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(projectId: Int, navController: NavController, projectViewModel: ProjectViewModel) {
    val projects by projectViewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }
    val bookmarkedIds by projectViewModel.bookmarkedIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.title ?: "프로젝트 개요") },
                actions = {
                    val isBookmarked = bookmarkedIds.contains(projectId.toString())
                    IconButton(onClick = { projectViewModel.toggleBookmark(projectId) }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isBookmarked) "북마크 해제" else "북마크",
                            tint = if (isBookmarked) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }

                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "닫기", tint = Color.Red)
                    }
                }
            )
        }
    ) {
        if (project != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(project.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = project.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "작성자: ${project.author}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "팀 멤버",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    // TODO: 팀 멤버 목록 UI 추가
                }
            }
        } else {
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Project not found.")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectDetailScreenPreview() {
    Univ_CommunityTheme {
        ProjectDetailScreen(projectId = 1, navController = rememberNavController(), projectViewModel = viewModel())
    }
}
