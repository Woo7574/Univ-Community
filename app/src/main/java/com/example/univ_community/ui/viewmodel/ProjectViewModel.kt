package com.example.univ_community.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    val imageUrl: String
)

val dummyProjects = listOf(
    Project(1, "앱 개발 프로젝트", "대학생 커뮤니티 앱을 함께 만들 팀원을 구합니다.", "김민준", "https://picsum.photos/seed/1/600/400"),
    Project(2, "웹 프론트엔드 스터디", "React, Next.js 스터디원을 모집합니다.", "이서연", "https://picsum.photos/seed/2/600/400"),
    Project(3, "AI 모델링 공모전", "자연어 처리 기술을 이용한 챗봇 개발 공모전에 참여할 팀원을 찾습니다.", "박지훈", "https://picsum.photos/seed/3/600/400"),
    Project(4, "게임 개발 사이드 프로젝트", "Unity를 사용한 2D 플랫포머 게임을 만들어보고 싶으신 분!", "최유진", "https://picsum.photos/seed/4/600/400"),
    Project(5, "블록체인 기반 서비스 기획", "탈중앙화 금융(DeFi) 서비스 기획 및 프로토타입 개발", "정현우", "https://picsum.photos/seed/5/600/400")
)

class ProjectViewModel : ViewModel() {

    private val _projects = MutableStateFlow(dummyProjects)
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    fun addProject(title: String, description: String, imageUri: Uri?) {
        val newProject = Project(
            id = (_projects.value.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            description = description,
            author = "새로운 작성자", // Placeholder
            imageUrl = imageUri?.toString() ?: "https://picsum.photos/seed/${System.currentTimeMillis()}/600/400"
        )
        // New projects are added to the top of the list.
        _projects.update { currentList -> listOf(newProject) + currentList.sortedByDescending { it.id } }
    }
}
