package fr.leboncoin.data.repository

import android.util.Log
import fr.leboncoin.data.local.album.service.AlbumLocalService
import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.data.repository.mapper.toDomain
import fr.leboncoin.data.repository.mapper.toEntity
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class AlbumRepositoryImpl @Inject constructor(
    private val albumRemoteService: AlbumRemoteService,
    private val albumLocalService: AlbumLocalService,
) : AlbumRepository {

    override fun getAllAlbums(): Flow<List<Album>> = channelFlow {
        albumLocalService.getAlbums().collect { entities ->
            if (entities.isEmpty()) {
                launch { refresh() }
            }
            send(entities.map { entity -> entity.toDomain() })
        }
    }

    private suspend fun refresh() {
        try {
            val albums = albumRemoteService.getAlbums().map { dto -> dto.toEntity() }
            albumLocalService.saveAlbums(albums)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to refresh albums", e)
        }
    }

    private companion object {
        const val TAG = "AlbumRepositoryImpl"
    }
}
