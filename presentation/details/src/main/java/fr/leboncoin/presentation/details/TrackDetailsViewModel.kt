package fr.leboncoin.presentation.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.usecase.GetTrackByIdUseCase
import fr.leboncoin.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getTrackByIdUseCase: GetTrackByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val albumId: Int = checkNotNull(savedStateHandle.get<Int>(TRACK_ID_KEY)) { "Missing $TRACK_ID_KEY" }

    val uiState: StateFlow<TrackDetailsUiState> = getTrackByIdUseCase(albumId)
        .map { result ->
            result.fold(
                onSuccess = { track -> TrackDetailsUiState.Success(track) },
                onFailure = { error -> TrackDetailsUiState.Error((error as TrackError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), TrackDetailsUiState.Loading)

    private val _favoriteToggleErrors = Channel<Int>(Channel.BUFFERED)
    val favoriteToggleErrors: Flow<Int> = _favoriteToggleErrors.receiveAsFlow()

    fun onFavoriteClicked() {
        viewModelScope.launch {
            toggleFavoriteUseCase(albumId).onFailure { error ->
                Log.e(TAG, "Failed to toggle favorite for track $albumId", error)
                _favoriteToggleErrors.send((error as TrackError).toErrorMessageRes())
            }
        }
    }

    companion object {
        const val TRACK_ID_KEY = "track_id"
        private const val TAG = "TrackDetailsViewModel"
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
