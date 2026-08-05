package fr.leboncoin.data.remote.track.api

import fr.leboncoin.data.remote.track.dto.TrackDto
import retrofit2.http.GET

internal interface TrackApiService {

    @GET("img/shared/technical-test.json")
    suspend fun getTracks(): List<TrackDto>

    companion object {
        const val BASE_URL = "https://static.leboncoin.fr/"
    }
}
