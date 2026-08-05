package fr.leboncoin.domain.usecase

import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.repository.TrackRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTracksUseCase @Inject constructor(
    private val repository: TrackRepository,
) {
    operator fun invoke(): Flow<Result<List<Track>>> = repository.getAllTracks()
}
