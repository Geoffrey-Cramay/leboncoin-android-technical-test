package fr.leboncoin.data.repository

import android.util.Log
import fr.leboncoin.data.local.album.service.AlbumLocalService
import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.data.repository.di.IoDispatcher
import fr.leboncoin.data.repository.mapper.toDomain
import fr.leboncoin.data.repository.mapper.toEntity
import fr.leboncoin.data.repository.mapper.toNetworkAlbumError
import fr.leboncoin.domain.error.AlbumError
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AlbumRepositoryImpl @Inject constructor(
    private val albumRemoteService: AlbumRemoteService,
    private val albumLocalService: AlbumLocalService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AlbumRepository {

    override fun getAllAlbums(): Flow<Result<List<Album>>> = channelFlow {
        albumLocalService.getAlbums()
            .catch { error ->
                Log.e(TAG, "Failed to read cached albums", error)
                send(Result.failure(exception = AlbumError.StorageError()))
            }
            .collect { entities ->
                if (entities.isEmpty()) {
                    launch { refresh().onFailure { error -> send(Result.failure(exception = error)) } }
                }
                send(Result.success(value = entities.map { entity -> entity.toDomain() }))
            }
    }.flowOn(ioDispatcher)

    override fun getAlbumById(id: Int): Flow<Result<Album>> = albumLocalService.getAlbumById(id)
        .map { entity -> entity?.toDomain()?.let { album -> Result.success(value = album) } ?: Result.failure(exception = AlbumError.NotFoundError()) }
        .catch { error ->
            Log.e(TAG, "Failed to read cached album", error)
            emit(Result.failure(exception = AlbumError.StorageError()))
        }
        .flowOn(ioDispatcher)

    private suspend fun refresh(): Result<Unit> {
        val remoteAlbums = try {
            albumRemoteService.getAlbums()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch albums from remote", e)
            return Result.failure(exception = e.toNetworkAlbumError())
        }

        return try {
            albumLocalService.saveAlbums(albums = remoteAlbums.map { dto -> dto.toEntity() })
            Result.success(value = Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save albums locally", e)
            Result.failure(exception = AlbumError.StorageError())
        }
    }

    private companion object {
        const val TAG = "AlbumRepositoryImpl"
    }
}
