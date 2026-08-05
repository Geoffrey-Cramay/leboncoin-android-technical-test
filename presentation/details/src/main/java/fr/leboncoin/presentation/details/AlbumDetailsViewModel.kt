package fr.leboncoin.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: AlbumRepository,
) : ViewModel() {

    val album: StateFlow<Album?> = repository
        .getAlbumById(checkNotNull(savedStateHandle.get<Int>(ALBUM_ID_KEY)) { "Missing $ALBUM_ID_KEY" })
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), null)

    companion object {
        const val ALBUM_ID_KEY = "album_id"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
