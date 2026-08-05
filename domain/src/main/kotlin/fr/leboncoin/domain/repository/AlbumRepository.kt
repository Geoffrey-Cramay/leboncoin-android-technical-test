package fr.leboncoin.domain.repository

import fr.leboncoin.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun getAllAlbums(): Flow<Result<List<Album>>>

    fun getAlbumById(id: Int): Flow<Album?>
}
