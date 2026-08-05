package fr.leboncoin.presentation.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.repository.TrackRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TracksViewModel @Inject constructor(
    repository: TrackRepository,
) : ViewModel() {

    val uiState: StateFlow<TracksUiState> = repository.getAllTracks()
        .map { result ->
            result.fold(
                onSuccess = { tracks ->
                    if (tracks.isEmpty()) TracksUiState.Loading else TracksUiState.Success(tracks)
                },
                onFailure = { error -> TracksUiState.Error((error as TrackError).toErrorMessageRes()) },
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), TracksUiState.Loading)

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
