package fr.leboncoin.presentation.details

import androidx.annotation.StringRes
import fr.leboncoin.domain.error.TrackError

@StringRes
internal fun TrackError.toErrorMessageRes(): Int = when (this) {
    is TrackError.NetworkError -> R.string.details_error_network
    is TrackError.StorageError -> R.string.details_error_storage
    is TrackError.NotFoundError -> R.string.details_track_not_found
    is TrackError.UnknownError -> R.string.details_error_unknown
}
