package fr.leboncoin.presentation.details

import androidx.annotation.StringRes
import fr.leboncoin.domain.error.AlbumError

@StringRes
internal fun AlbumError.toErrorMessageRes(): Int = when (this) {
    is AlbumError.NetworkError -> R.string.details_error_network
    is AlbumError.StorageError -> R.string.details_error_storage
    is AlbumError.NotFoundError -> R.string.details_album_not_found
    is AlbumError.UnknownError -> R.string.details_error_unknown
}
