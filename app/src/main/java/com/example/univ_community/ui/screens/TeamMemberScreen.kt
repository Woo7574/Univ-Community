package com.example.univ_community.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.univ_community.ui.theme.Univ_CommunityTheme

// 1. 데이터 클래스 정의
data class TeamMember(
    val id: Int,
    val name: String,
    val skills: String,
    val introduction: String
)

// 2. 가상 데이터 생성
val dummyTeamMembers = listOf(
    TeamMember(1, "김민준", "Android, Kotlin, Jetpack Compose", "모바일 앱 개발에 관심이 많습니다."),
    TeamMember(2, "이서연", "React, Next.js, TypeScript", "사용자 경험을 중시하는 프론트엔드 개발자입니다."),
    TeamMember(3, "박지훈", "Python, TensorFlow, PyTorch", "AI와 머신러닝으로 세상을 바꾸고 싶습니다."),
    TeamMember(4, "최유진", "Unity, C#", "재미있는 게임을 만드는 것을 좋아합니다."),
    TeamMember(5, "정현우", "Figma, Sketch, Adobe XD", "직관적이고 아름다운 UI/UX를 디자인합니다.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamMemberScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("팀원 찾기") },
                actions = {
                    IconButton(onClick = { /* TODO: 검색 UI 토글 */ }) {
                        Icon(Icons.Default.Search, contentDescription = "검색")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(dummyTeamMembers) { member ->
                TeamMemberCard(member = member)
            }
        }
    }
}

@Composable
fun TeamMemberCard(member: TeamMember) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = member.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "보유 기술: ${member.skills}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = member.introduction,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamMemberScreenPreview() {
    Univ_CommunityTheme {
        TeamMemberScreen()
    }
}