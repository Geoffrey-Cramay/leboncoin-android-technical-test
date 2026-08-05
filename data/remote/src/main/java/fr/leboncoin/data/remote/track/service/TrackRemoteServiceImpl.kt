package fr.leboncoin.data.remote.track.service

import fr.leboncoin.data.remote.track.api.TrackApiService
import fr.leboncoin.data.remote.track.dto.TrackDto
import javax.inject.Inject

internal class TrackRemoteServiceImpl @Inject constructor(
    private val trackApiService: TrackApiService,
) : TrackRemoteService {

    override suspend fun getTracks(): List<TrackDto> = trackApiService.getTracks()
}
