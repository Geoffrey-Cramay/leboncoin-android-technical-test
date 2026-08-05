package fr.leboncoin.domain.usecase

import fr.leboncoin.domain.repository.TrackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToggleFavoriteUseCaseTest {

    private lateinit var repository: TrackRepository
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke delegates to the repository's toggleFavorite`() = runTest {
        coEvery { repository.toggleFavorite(1) } returns Result.success(Unit)

        val result = toggleFavoriteUseCase(1)

        assertTrue(result.isSuccess)
        coVerify { repository.toggleFavorite(1) }
    }
}
