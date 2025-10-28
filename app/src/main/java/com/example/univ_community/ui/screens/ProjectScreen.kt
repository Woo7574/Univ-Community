package com.example.univ_community.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.univ_community.ui.navigation.Screen
import com.example.univ_community.ui.theme.Univ_CommunityTheme
import com.example.univ_community.ui.viewmodel.Project
import com.example.univ_community.ui.viewmodel.ProjectViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController, projectViewModel: ProjectViewModel) {
    val projects by projectViewModel.projects.collectAsState()

    // 상태 구독 (태그 + 북마크)
    val tagsMap by projectViewModel.projectTags.collectAsState()
    val allTags by projectViewModel.allTags.collectAsState()
    val bookmarkedIds by projectViewModel.bookmarkedIds.collectAsState()

    // 선택 상태들
    var selectedTags by remember { mutableStateOf(setOf<String>()) }
    var showBookmarkedOnly by remember { mutableStateOf(false) }

    // 최종 표시할 프로젝트 목록 (태그 + 북마크 조건 통합)
    val visibleList = projects
        .let { list ->
            if (selectedTags.isEmpty()) list
            else list.filter { p ->
                val ptags = tagsMap[p.id].orEmpty()
                ptags.any { it in selectedTags }
            }
        }
        .let { list ->
            if (showBookmarkedOnly) list.filter { it.id.toString() in bookmarkedIds } else list
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프로젝트") },
                actions = {
                    TextButton(onClick = { showBookmarkedOnly = !showBookmarkedOnly }) {
                        Text(if (showBookmarkedOnly) "전체보기" else "북마크")
                    }
                    IconButton(onClick = { navController.navigate(Screen.AddProject.route) }) {
                        Icon(Icons.Default.Add, contentDescription = "프로젝트 추가")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // 태그 칩 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    .wrapContentHeight()
            ) {
                allTags.forEachIndexed { idx, tag ->
                    val selected = tag in selectedTags
                    FilterChip(
                        selected = selected,
                        onClick = {
                            selectedTags = if (selected) selectedTags - tag else selectedTags + tag
                        },
                        label = { Text(tag) },
                        colors = FilterChipDefaults.filterChipColors()
                    )
                    if (idx != allTags.size - 1) Spacer(Modifier.width(8.dp))
                }
            }

            // 프로젝트 리스트
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(visibleList) { project ->
                    ProjectCard(
                        project = project,
                        onClick = { navController.navigate(Screen.ProjectDetail.createRoute(project.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "작성자: ${project.author}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    Univ_CommunityTheme {
        ProjectCard(
            project = Project(
                id = 999,
                title = "프리뷰용 프로젝트",
                description = "미리보기에서만 쓰는 더미 설명입니다.",
                author = "PreviewUser",
                imageUrl = "https://picsum.photos/seed/preview/600/400"
            ),
            onClick = {}
        )
    }
}
