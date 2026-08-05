package fr.leboncoin.presentation.details

import fr.leboncoin.domain.error.TrackError
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorMessageFormatterTest {

    @Test
    fun `NetworkError maps to the network message`() {
        assertEquals(R.string.details_error_network, TrackError.NetworkError().toErrorMessageRes())
    }

    @Test
    fun `StorageError maps to the storage message`() {
        assertEquals(R.string.details_error_storage, TrackError.StorageError().toErrorMessageRes())
    }

    @Test
    fun `NotFoundError maps to the track-not-found message`() {
        assertEquals(R.string.details_track_not_found, TrackError.NotFoundError().toErrorMessageRes())
    }

    @Test
    fun `UnknownError maps to the unknown message`() {
        assertEquals(R.string.details_error_unknown, TrackError.UnknownError().toErrorMessageRes())
    }
}
