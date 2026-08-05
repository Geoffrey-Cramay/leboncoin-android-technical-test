package fr.leboncoin.presentation.tracks

import androidx.annotation.StringRes
import fr.leboncoin.domain.error.TrackError

@StringRes
internal fun TrackError.toErrorMessageRes(): Int = when (this) {
    is TrackError.NetworkError -> R.string.tracks_error_network
    is TrackError.StorageError -> R.string.tracks_error_storage
    is TrackError.NotFoundError -> R.string.tracks_error_unknown
    is TrackError.UnknownError -> R.string.tracks_error_unknown
}
