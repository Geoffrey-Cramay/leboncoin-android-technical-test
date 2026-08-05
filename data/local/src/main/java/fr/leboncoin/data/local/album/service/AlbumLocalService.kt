package fr.leboncoin.data.local.album.service

import fr.leboncoin.data.local.album.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

interface AlbumLocalService {

    fun getAlbums(): Flow<List<AlbumEntity>>

    suspend fun saveAlbums(albums: List<AlbumEntity>)
}
