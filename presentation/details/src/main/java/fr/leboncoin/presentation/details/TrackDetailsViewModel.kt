package fr.leboncoin.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.repository.TrackRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TrackDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: TrackRepository,
) : ViewModel() {

    private val albumId: Int = checkNotNull(savedStateHandle.get<Int>(TRACK_ID_KEY)) { "Missing $TRACK_ID_KEY" }

    val uiState: StateFlow<TrackDetailsUiState> = repository
        .getTrackById(albumId)
        .map { result ->
            result.fold(
                onSuccess = { track -> TrackDetailsUiState.Success(track) },
                onFailure = { error -> TrackDetailsUiState.Error((error as TrackError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), TrackDetailsUiState.Loading)

    companion object {
        const val TRACK_ID_KEY = "track_id"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
