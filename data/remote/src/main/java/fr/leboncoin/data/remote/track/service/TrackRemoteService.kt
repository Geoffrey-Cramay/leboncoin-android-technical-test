package fr.leboncoin.data.remote.track.service

import fr.leboncoin.data.remote.track.dto.TrackDto

interface TrackRemoteService {

    suspend fun getTracks(): List<TrackDto>
}
