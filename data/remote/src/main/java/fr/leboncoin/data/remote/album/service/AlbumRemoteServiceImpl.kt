package fr.leboncoin.data.remote.album.service

import fr.leboncoin.data.remote.album.api.AlbumApiService
import fr.leboncoin.data.remote.album.dto.AlbumDto
import javax.inject.Inject

internal class AlbumRemoteServiceImpl @Inject constructor(
    private val albumApiService: AlbumApiService,
) : AlbumRemoteService {

    override suspend fun getAlbums(): List<AlbumDto> = albumApiService.getAlbums()
}
