package com.example.univ_community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.univ_community.data.ThemeRepository
import com.example.univ_community.ui.screens.MainScreen
import com.example.univ_community.ui.theme.Univ_CommunityTheme
import com.example.univ_community.ui.viewmodel.ProfileViewModel
import com.example.univ_community.ui.viewmodel.ProjectViewModel
import com.example.univ_community.ui.viewmodel.ThemeViewModel
import com.example.univ_community.ui.viewmodel.ThemeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel(
                factory = ThemeViewModelFactory(ThemeRepository(this))
            )
            val projectViewModel: ProjectViewModel = viewModel()
            val profileViewModel: ProfileViewModel = viewModel()
            val themeMode by themeViewModel.themeMode.collectAsState()

            Univ_CommunityTheme(
                darkTheme = when (themeMode) {
                    is com.example.univ_community.ui.viewmodel.ThemeMode.Light -> false
                    is com.example.univ_community.ui.viewmodel.ThemeMode.Dark -> true
                    is com.example.univ_community.ui.viewmodel.ThemeMode.System -> isSystemInDarkTheme()
                }
            ) {
                MainScreen(
                    themeViewModel = themeViewModel, 
                    projectViewModel = projectViewModel,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}