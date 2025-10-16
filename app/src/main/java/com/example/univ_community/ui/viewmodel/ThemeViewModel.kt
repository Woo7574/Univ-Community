package com.example.univ_community.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.univ_community.data.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ThemeMode {
    object Light : ThemeMode()
    object Dark : ThemeMode()
    object System : ThemeMode()
}

class ThemeViewModel(private val repository: ThemeRepository) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = repository.getThemeMode
        .map { themeString ->
            when (themeString) {
                "Light" -> ThemeMode.Light
                "Dark" -> ThemeMode.Dark
                else -> ThemeMode.System
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeMode.System
        )

    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            val themeString = when (themeMode) {
                ThemeMode.Light -> "Light"
                ThemeMode.Dark -> "Dark"
                ThemeMode.System -> "System"
            }
            repository.setThemeMode(themeString)
        }
    }
}

class ThemeViewModelFactory(private val repository: ThemeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
