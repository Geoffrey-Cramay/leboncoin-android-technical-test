package fr.leboncoin.presentation.tracks

import androidx.annotation.StringRes
import fr.leboncoin.domain.model.Track

sealed interface TracksUiState {

    data object Loading : TracksUiState

    data class Success(val tracks: List<Track>) : TracksUiState

    data class Error(@StringRes val messageRes: Int) : TracksUiState
}
