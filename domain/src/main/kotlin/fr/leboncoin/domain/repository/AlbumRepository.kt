package fr.leboncoin.domain.repository

import fr.leboncoin.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun getAllAlbums(): Flow<List<Album>>
}
