package fr.leboncoin.presentation.details

import fr.leboncoin.domain.error.AlbumError
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMessageFormatterTest {

    @Test
    fun `NetworkError maps to the network message`() {
        assertEquals(R.string.details_error_network, AlbumError.NetworkError().toErrorMessageRes())
    }

    @Test
    fun `StorageError maps to the storage message`() {
        assertEquals(R.string.details_error_storage, AlbumError.StorageError().toErrorMessageRes())
    }

    @Test
    fun `NotFoundError maps to the album-not-found message`() {
        assertEquals(R.string.details_album_not_found, AlbumError.NotFoundError().toErrorMessageRes())
    }

    @Test
    fun `UnknownError maps to the unknown message`() {
        assertEquals(R.string.details_error_unknown, AlbumError.UnknownError().toErrorMessageRes())
    }
}
