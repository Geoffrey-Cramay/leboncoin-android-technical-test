package fr.leboncoin.presentation.albums

import androidx.annotation.StringRes
import fr.leboncoin.domain.model.Album

sealed interface AlbumsUiState {

    data object Loading : AlbumsUiState

    data class Success(val albums: List<Album>) : AlbumsUiState

    data class Error(@StringRes val messageRes: Int) : AlbumsUiState
}
