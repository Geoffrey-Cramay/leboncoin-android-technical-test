package fr.leboncoin.data.remote.album.service

import fr.leboncoin.data.remote.album.dto.AlbumDto

interface AlbumRemoteService {

    suspend fun getAlbums(): List<AlbumDto>
}
