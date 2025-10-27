package com.example.univ_community.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("bookmark_prefs")

class BookmarkRepository(private val context: Context) {

    private val KEY_BOOKMARKS = stringSetPreferencesKey("bookmarked_project_ids")

    val bookmarkedIdsFlow: Flow<Set<String>> =
        context.dataStore.data.map { pref -> pref[KEY_BOOKMARKS] ?: emptySet() }

    suspend fun toggle(projectId: String) {
        context.dataStore.edit { pref ->
            val current = pref[KEY_BOOKMARKS]?.toMutableSet() ?: mutableSetOf()
            if (current.contains(projectId)) current.remove(projectId) else current.add(projectId)
            pref[KEY_BOOKMARKS] = current
        }
    }
}
