package fr.leboncoin.presentation.details

import androidx.annotation.StringRes
import fr.leboncoin.domain.model.Album

sealed interface AlbumDetailsUiState {

    data object Loading : AlbumDetailsUiState

    data class Success(val album: Album) : AlbumDetailsUiState

    data class Error(@StringRes val messageRes: Int) : AlbumDetailsUiState
}
