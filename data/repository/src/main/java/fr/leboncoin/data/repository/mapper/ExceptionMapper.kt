package fr.leboncoin.data.repository.mapper

import fr.leboncoin.domain.error.TrackError
import java.io.IOException
import retrofit2.HttpException

internal fun Throwable.toNetworkTrackError(): TrackError = when (this) {
    is IOException -> TrackError.NetworkError()
    is HttpException -> TrackError.NetworkError()
    else -> TrackError.UnknownError()
}
