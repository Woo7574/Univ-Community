package com.example.univ_community.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Profile(
    val name: String,
    val statusMessage: String,
    val skills: List<String>,
    val profileImageUri: Uri?
)

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow(
        Profile(
            name = "김민준",
            statusMessage = "열심히 개발 공부 중!",
            skills = listOf("Android", "Kotlin", "Jetpack Compose"),
            profileImageUri = null
        )
    )
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    fun setProfileImage(uri: Uri?) {
        _profile.update { it.copy(profileImageUri = uri) }
    }
    
    // Functions to update name, status, skills will be added later.
}
