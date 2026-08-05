package fr.leboncoin.data.repository

import android.util.Log
import fr.leboncoin.data.local.track.service.TrackLocalService
import fr.leboncoin.data.remote.track.service.TrackRemoteService
import fr.leboncoin.data.repository.di.IoDispatcher
import fr.leboncoin.data.repository.mapper.toDomain
import fr.leboncoin.data.repository.mapper.toEntity
import fr.leboncoin.data.repository.mapper.toNetworkTrackError
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.repository.TrackRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TrackRepositoryImpl @Inject constructor(
    private val trackRemoteService: TrackRemoteService,
    private val trackLocalService: TrackLocalService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TrackRepository {

    override fun getAllTracks(): Flow<Result<List<Track>>> = channelFlow {
        trackLocalService.getTracks()
            .catch { error ->
                Log.e(TAG, "Failed to read cached tracks", error)
                send(Result.failure(exception = TrackError.StorageError()))
            }
            .collect { entities ->
                if (entities.isEmpty()) {
                    launch { refresh().onFailure { error -> send(Result.failure(exception = error)) } }
                }
                send(Result.success(value = entities.map { entity -> entity.toDomain() }))
            }
    }.flowOn(ioDispatcher)

    override fun getTrackById(id: Int): Flow<Result<Track>> = trackLocalService.getTrackById(id)
        .map { entity -> entity?.toDomain()?.let { track -> Result.success(value = track) } ?: Result.failure(exception = TrackError.NotFoundError()) }
        .catch { error ->
            Log.e(TAG, "Failed to read cached track", error)
            emit(Result.failure(exception = TrackError.StorageError()))
        }
        .flowOn(ioDispatcher)

    override suspend fun toggleFavorite(id: Int): Result<Unit> = try {
        trackLocalService.toggleFavorite(id)
        Result.success(value = Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.e(TAG, "Failed to toggle favorite for track $id", e)
        Result.failure(exception = TrackError.StorageError())
    }

    private suspend fun refresh(): Result<Unit> {
        val remoteTracks = try {
            trackRemoteService.getTracks()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch tracks from remote", e)
            return Result.failure(exception = e.toNetworkTrackError())
        }

        return try {
            val favoriteIds = trackLocalService.getFavoriteTrackIds().toSet()
            val entities = remoteTracks.map { dto -> dto.toEntity(isFavorite = dto.id in favoriteIds) }
            trackLocalService.saveTracks(tracks = entities)
            Result.success(value = Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save tracks locally", e)
            Result.failure(exception = TrackError.StorageError())
        }
    }

    private companion object {
        const val TAG = "TrackRepositoryImpl"
    }
}
