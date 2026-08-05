package fr.leboncoin.presentation.albums

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository,
) : ViewModel() {

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    fun loadAlbums() {
        viewModelScope.launch {
            repository.getAllAlbums()
                .onSuccess { albums -> _albums.value = albums }
                .onFailure { error -> Log.e(TAG, "Failed to load albums", error) }
        }
    }

    private companion object {
        const val TAG = "AlbumsViewModel"
    }
}
