package fr.leboncoin.data.repository

import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.data.repository.mapper.toDomain
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumRemoteService: AlbumRemoteService,
) : AlbumRepository {

    override suspend fun getAllAlbums(): Result<List<Album>> = runCatching {
        albumRemoteService.getAlbums().map { album -> album.toDomain() }
    }
}
