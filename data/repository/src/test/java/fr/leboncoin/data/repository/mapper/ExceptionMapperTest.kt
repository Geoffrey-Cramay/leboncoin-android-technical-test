package fr.leboncoin.data.repository.mapper

import fr.leboncoin.domain.error.TrackError
import java.io.IOException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ExceptionMapperTest {

    @Test
    fun `IOException maps to NetworkError`() {
        val error = IOException("no network").toNetworkTrackError()

        assertTrue(error is TrackError.NetworkError)
    }

    @Test
    fun `HttpException maps to NetworkError`() {
        val httpException = HttpException(Response.error<Any>(500, "".toResponseBody(null)))

        val error = httpException.toNetworkTrackError()

        assertTrue(error is TrackError.NetworkError)
    }

    @Test
    fun `any other exception maps to UnknownError`() {
        val error = RuntimeException("boom").toNetworkTrackError()

        assertTrue(error is TrackError.UnknownError)
    }
}
