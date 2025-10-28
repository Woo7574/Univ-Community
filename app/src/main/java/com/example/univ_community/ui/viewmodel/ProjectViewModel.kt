package com.example.univ_community.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.univ_community.data.BookmarkRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.map


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

class ProjectViewModel(app: Application) : AndroidViewModel(app) {

    private val _projects = MutableStateFlow(dummyProjects)
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()
    //  각 프로젝트의 태그 목록을 관리 (projectId -> tags)
    private val _projectTags = MutableStateFlow<Map<Int, List<String>>>(
        mapOf(
            // 더미 프로젝트용 기본 태그 예시 (원하시면 변경하세요)
            1 to listOf("앱", "Kotlin", "팀원모집"),
            2 to listOf("웹", "React", "스터디"),
            3 to listOf("AI", "NLP", "공모전"),
            4 to listOf("게임", "Unity", "사이드"),
            5 to listOf("블록체인", "기획", "DeFi")
        )
    )
    val projectTags: StateFlow<Map<Int, List<String>>> = _projectTags.asStateFlow()

    //  모든 태그 목록 (필터 칩에 렌더링용)
    val allTags: StateFlow<Set<String>> =
        projectTags
            .map { it.values.flatten().toSet() }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    //  프로젝트별 태그 갱신(추후 편집 화면에서 사용 가능)
    fun updateTags(projectId: Int, tags: List<String>) {
        _projectTags.update { old ->
            old.toMutableMap().apply { this[projectId] = tags }
        }
    }

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

        _projectTags.update { it + (newProject.id to emptyList()) }
    }
    private val bookmarkRepo = BookmarkRepository(getApplication())

    val bookmarkedIds = bookmarkRepo.bookmarkedIdsFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    fun isBookmarked(projectId: Int): Boolean =
        bookmarkedIds.value.contains(projectId.toString())

    fun toggleBookmark(projectId: Int) {
        viewModelScope.launch {
            bookmarkRepo.toggle(projectId.toString())
        }
    }
}
