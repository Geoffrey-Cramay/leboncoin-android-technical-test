package fr.leboncoin.domain.error

sealed class TrackError : Exception() {

    class NetworkError : TrackError()

    class StorageError : TrackError()

    class NotFoundError : TrackError()

    class UnknownError : TrackError()
}
