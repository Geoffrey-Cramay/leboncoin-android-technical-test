package fr.leboncoin.presentation.details

import androidx.annotation.StringRes
import fr.leboncoin.domain.model.Track

sealed interface TrackDetailsUiState {

    data object Loading : TrackDetailsUiState

    data class Success(val track: Track) : TrackDetailsUiState

    data class Error(@StringRes val messageRes: Int) : TrackDetailsUiState
}
