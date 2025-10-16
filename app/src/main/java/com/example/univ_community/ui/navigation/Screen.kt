package com.example.univ_community.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Project : Screen("project", "프로젝트", Icons.Default.Home)
    object TeamMember : Screen("team_member", "팀원 찾기", Icons.Default.Person)
    object PersonalSpace : Screen("personal_space", "내 공간", Icons.Default.DateRange)
    object Settings : Screen("settings", "설정")
    object AddProject : Screen("add_project", "프로젝트 추가")
    object ProjectDetail : Screen("project_detail/{projectId}", "프로젝트 개요") {
        fun createRoute(projectId: Int) = "project_detail/$projectId"
    }
}
