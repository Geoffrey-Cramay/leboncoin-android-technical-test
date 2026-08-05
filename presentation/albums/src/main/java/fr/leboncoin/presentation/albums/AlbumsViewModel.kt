package fr.leboncoin.presentation.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.AlbumError
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    repository: AlbumRepository,
) : ViewModel() {

    val uiState: StateFlow<AlbumsUiState> = repository.getAllAlbums()
        .map { result ->
            result.fold(
                onSuccess = { albums ->
                    if (albums.isEmpty()) AlbumsUiState.Loading else AlbumsUiState.Success(albums)
                },
                onFailure = { error -> AlbumsUiState.Error((error as AlbumError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), AlbumsUiState.Loading)

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
