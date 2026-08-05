package fr.leboncoin.domain.usecase

import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.repository.TrackRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetTracksUseCaseTest {

    private lateinit var repository: TrackRepository
    private lateinit var getTracksUseCase: GetTracksUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getTracksUseCase = GetTracksUseCase(repository)
    }

    @Test
    fun `invoke returns the repository's getAllTracks flow`() = runTest {
        val expected = listOf(Track(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        every { repository.getAllTracks() } returns MutableStateFlow(Result.success(expected))

        val result = getTracksUseCase().first()

        assertEquals(expected, result.getOrThrow())
    }
}
