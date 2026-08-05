package fr.leboncoin.presentation.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    repository: AlbumRepository,
) : ViewModel() {

    val albums: StateFlow<List<Album>> = repository.getAllAlbums()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), emptyList())

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
