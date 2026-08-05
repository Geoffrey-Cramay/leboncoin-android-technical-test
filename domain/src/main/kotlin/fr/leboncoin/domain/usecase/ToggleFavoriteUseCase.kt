package fr.leboncoin.domain.usecase

import fr.leboncoin.domain.repository.TrackRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: TrackRepository,
) {
    suspend operator fun invoke(id: Int): Result<Unit> = repository.toggleFavorite(id)
}
