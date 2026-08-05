package fr.leboncoin.data.repository.mapper

import fr.leboncoin.domain.error.AlbumError
import java.io.IOException
import retrofit2.HttpException

internal fun Throwable.toNetworkAlbumError(): AlbumError = when (this) {
    is IOException -> AlbumError.NetworkError()
    is HttpException -> AlbumError.NetworkError()
    else -> AlbumError.UnknownError()
}
