package fr.leboncoin.data.remote.album.api

import fr.leboncoin.data.remote.album.dto.AlbumDto
import retrofit2.http.GET

internal interface AlbumApiService {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDto>

    companion object {
        const val BASE_URL = "https://static.leboncoin.fr/"
    }
}
