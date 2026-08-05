package fr.leboncoin.data.local.track.service

import fr.leboncoin.data.local.track.dao.TrackDao
import fr.leboncoin.data.local.track.entity.TrackEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class TrackLocalServiceImpl @Inject constructor(
    private val trackDao: TrackDao,
) : TrackLocalService {

    override fun getTracks(): Flow<List<TrackEntity>> = trackDao.getTracks()

    override fun getTrackById(id: Int): Flow<TrackEntity?> = trackDao.getTrackById(id)

    override suspend fun saveTracks(tracks: List<TrackEntity>) = trackDao.insertTracks(tracks)
}
