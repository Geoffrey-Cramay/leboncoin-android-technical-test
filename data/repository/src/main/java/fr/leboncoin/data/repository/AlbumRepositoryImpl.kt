package fr.leboncoin.data.repository

import fr.leboncoin.data.remote.api.AlbumApiService
import fr.leboncoin.data.repository.mapper.toDomain
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository

class AlbumRepositoryImpl(
    private val albumApiService: AlbumApiService,
) : AlbumRepository {

    override suspend fun getAllAlbums(): Result<List<Album>> = runCatching {
        albumApiService.getAlbums().map { album -> album.toDomain() }
    }
}
