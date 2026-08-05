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
class GetTrackByIdUseCaseTest {

    private lateinit var repository: TrackRepository
    private lateinit var getTrackByIdUseCase: GetTrackByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getTrackByIdUseCase = GetTrackByIdUseCase(repository)
    }

    @Test
    fun `invoke returns the repository's getTrackById flow for the given id`() = runTest {
        val expected = Track(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu")
        every { repository.getTrackById(1) } returns MutableStateFlow(Result.success(expected))

        val result = getTrackByIdUseCase(1).first()

        assertEquals(expected, result.getOrThrow())
    }
}
