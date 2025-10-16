package com.example.univ_community.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.univ_community.data.ThemeRepository
import com.example.univ_community.ui.navigation.Screen
import com.example.univ_community.ui.theme.Univ_CommunityTheme
import com.example.univ_community.ui.viewmodel.ProfileViewModel
import com.example.univ_community.ui.viewmodel.ProjectViewModel
import com.example.univ_community.ui.viewmodel.ThemeViewModel
import com.example.univ_community.ui.viewmodel.ThemeViewModelFactory

@Composable
fun MainScreen(themeViewModel: ThemeViewModel, projectViewModel: ProjectViewModel, profileViewModel: ProfileViewModel) {
    val navController = rememberNavController()
    val screens = listOf(
        Screen.Project,
        Screen.TeamMember,
        Screen.PersonalSpace
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    screen.icon?.let {
                        NavigationBarItem(
                            icon = { Icon(it, contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { dest -> dest.route == screen.route } == true,
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Project.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Project.route) { ProjectScreen(navController = navController, projectViewModel = projectViewModel) }
            composable(Screen.TeamMember.route) { TeamMemberScreen() }
            composable(Screen.PersonalSpace.route) { PersonalSpaceScreen(navController = navController, profileViewModel = profileViewModel) }
            composable(Screen.Settings.route) { SettingsScreen(themeViewModel = themeViewModel) }
            composable(Screen.AddProject.route) { AddProjectScreen(navController = navController, projectViewModel = projectViewModel) }
            composable(
                route = Screen.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.IntType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getInt("projectId")
                projectId?.let {
                    ProjectDetailScreen(projectId = it, navController = navController, projectViewModel = projectViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Univ_CommunityTheme {
        MainScreen(
            themeViewModel = viewModel(factory = ThemeViewModelFactory(ThemeRepository(LocalContext.current))),
            projectViewModel = viewModel(),
            profileViewModel = viewModel()
        )
    }
}
