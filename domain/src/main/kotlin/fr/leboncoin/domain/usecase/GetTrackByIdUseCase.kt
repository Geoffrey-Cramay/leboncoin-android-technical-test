package fr.leboncoin.domain.usecase

import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.repository.TrackRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTrackByIdUseCase @Inject constructor(
    private val repository: TrackRepository,
) {
    operator fun invoke(id: Int): Flow<Result<Track>> = repository.getTrackById(id)
}
