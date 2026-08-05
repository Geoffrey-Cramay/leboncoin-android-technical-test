package fr.leboncoin.presentation.tracks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.usecase.GetTracksUseCase
import fr.leboncoin.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TracksViewModel @Inject constructor(
    getTracksUseCase: GetTracksUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<TracksUiState> = getTracksUseCase()
        .map { result ->
            result.fold(
                onSuccess = { tracks ->
                    if (tracks.isEmpty()) TracksUiState.Loading else TracksUiState.Success(tracks)
                },
                onFailure = { error -> TracksUiState.Error((error as TrackError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), TracksUiState.Loading)

    private val _favoriteToggleErrors = Channel<Int>(Channel.BUFFERED)
    val favoriteToggleErrors: Flow<Int> = _favoriteToggleErrors.receiveAsFlow()

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            toggleFavoriteUseCase(track.id).onFailure { error ->
                Log.e(TAG, "Failed to toggle favorite for track ${track.id}", error)
                _favoriteToggleErrors.send((error as TrackError).toErrorMessageRes())
            }
        }
    }

    private companion object {
        const val TAG = "TracksViewModel"
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
