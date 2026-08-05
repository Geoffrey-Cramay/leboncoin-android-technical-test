package fr.leboncoin.domain.error

sealed class AlbumError : Exception() {

    class NetworkError : AlbumError()

    class StorageError : AlbumError()

    class NotFoundError : AlbumError()

    class UnknownError : AlbumError()
}
