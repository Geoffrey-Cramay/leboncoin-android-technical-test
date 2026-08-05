package fr.leboncoin.domain.repository

import fr.leboncoin.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun getAllTracks(): Flow<Result<List<Track>>>

    fun getTrackById(id: Int): Flow<Result<Track>>

    suspend fun toggleFavorite(id: Int): Result<Unit>
}
