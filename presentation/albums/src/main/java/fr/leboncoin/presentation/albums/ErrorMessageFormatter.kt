package fr.leboncoin.presentation.albums

import androidx.annotation.StringRes
import fr.leboncoin.domain.error.AlbumError

@StringRes
internal fun AlbumError.toErrorMessageRes(): Int = when (this) {
    is AlbumError.NetworkError -> R.string.albums_error_network
    is AlbumError.StorageError -> R.string.albums_error_storage
    is AlbumError.NotFoundError -> R.string.albums_error_unknown
    is AlbumError.UnknownError -> R.string.albums_error_unknown
}
