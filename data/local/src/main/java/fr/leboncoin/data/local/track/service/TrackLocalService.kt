package fr.leboncoin.data.local.track.service

import fr.leboncoin.data.local.track.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

interface TrackLocalService {

    fun getTracks(): Flow<List<TrackEntity>>

    fun getTrackById(id: Int): Flow<TrackEntity?>

    suspend fun saveTracks(tracks: List<TrackEntity>)

    suspend fun toggleFavorite(id: Int)

    suspend fun getFavoriteTrackIds(): List<Int>
}
