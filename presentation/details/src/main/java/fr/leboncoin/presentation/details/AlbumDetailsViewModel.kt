package fr.leboncoin.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.AlbumError
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: AlbumRepository,
) : ViewModel() {

    private val albumId: Int = checkNotNull(savedStateHandle.get<Int>(ALBUM_ID_KEY)) { "Missing $ALBUM_ID_KEY" }

    val uiState: StateFlow<AlbumDetailsUiState> = repository
        .getAlbumById(albumId)
        .map { result ->
            result.fold(
                onSuccess = { album -> AlbumDetailsUiState.Success(album) },
                onFailure = { error -> AlbumDetailsUiState.Error((error as AlbumError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), AlbumDetailsUiState.Loading)

    companion object {
        const val ALBUM_ID_KEY = "album_id"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
