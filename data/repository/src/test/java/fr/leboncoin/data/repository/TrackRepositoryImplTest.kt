package fr.leboncoin.data.repository

import fr.leboncoin.data.local.track.entity.TrackEntity
import fr.leboncoin.data.local.track.service.TrackLocalService
import fr.leboncoin.data.remote.track.dto.TrackDto
import fr.leboncoin.data.remote.track.service.TrackRemoteService
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.model.Track
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrackRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var trackRemoteService: TrackRemoteService
    private lateinit var trackLocalService: TrackLocalService
    private lateinit var repository: TrackRepositoryImpl

    @Before
    fun setUp() {
        trackRemoteService = mockk()
        trackLocalService = mockk()
        repository = TrackRepositoryImpl(
            trackRemoteService = trackRemoteService,
            trackLocalService = trackLocalService,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `getAllTracks emits cached data when cache is not empty`() = runTest(testDispatcher) {
        val cached = listOf(TrackEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        every { trackLocalService.getTracks() } returns MutableStateFlow(cached)

        val tracks = repository.getAllTracks().first().getOrThrow()

        assertEquals(listOf("cached"), tracks.map { it.title })
    }

    @Test
    fun `getAllTracks calls refresh from remote and emits the result when the cache is empty`() = runTest(testDispatcher) {
        val remoteTracks = listOf(TrackDto(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
        val localTracks = MutableStateFlow(emptyList<TrackEntity>())
        every { trackLocalService.getTracks() } returns localTracks
        coEvery { trackRemoteService.getTracks() } returns remoteTracks
        coEvery { trackLocalService.saveTracks(any()) } answers { localTracks.value = firstArg() }

        val emissions = mutableListOf<Result<List<Track>>>()
        val job = launch { repository.getAllTracks().toList(emissions) }
        advanceUntilIdle()
        job.cancel()

        assertEquals(listOf(emptyList(), listOf("fresh")), emissions.map { result -> result.getOrThrow().map { it.title } })
    }

    @Test
    fun `getAllTracks does not call refresh when the cache is not empty`() = runTest(testDispatcher) {
        val cached = listOf(TrackEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        every { trackLocalService.getTracks() } returns MutableStateFlow(cached)

        val job = launch { repository.getAllTracks().collect { } }
        advanceUntilIdle()
        job.cancel()

        coVerify(exactly = 0) { trackRemoteService.getTracks() }
    }

    @Test
    fun `getAllTracks emits a Result failure when an exception is thrown`() = runTest(testDispatcher) {
        every { trackLocalService.getTracks() } returns flow { throw RuntimeException("db unavailable") }

        val result = repository.getAllTracks().first()

        assertTrue(result.isFailure)
    }

    @Test
    fun `getTrackById emits the matching cached track`() = runTest(testDispatcher) {
        val cached = TrackEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu")
        every { trackLocalService.getTrackById(1) } returns MutableStateFlow(cached)

        val track = repository.getTrackById(1).first().getOrThrow()

        assertEquals("cached", track.title)
    }

    @Test
    fun `getTrackById emits a NotFoundError when no track matches the id`() = runTest(testDispatcher) {
        every { trackLocalService.getTrackById(1) } returns MutableStateFlow(null)

        val result = repository.getTrackById(1).first()

        assertTrue(result.exceptionOrNull() is TrackError.NotFoundError)
    }

    @Test
    fun `getTrackById emits a Result failure when an exception is thrown`() = runTest(testDispatcher) {
        every { trackLocalService.getTrackById(1) } returns flow { throw RuntimeException("db unavailable") }

        val result = repository.getTrackById(1).first()

        assertTrue(result.isFailure)
    }
}
