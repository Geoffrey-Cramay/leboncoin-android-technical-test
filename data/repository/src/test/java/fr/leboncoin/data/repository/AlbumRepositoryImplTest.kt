package fr.leboncoin.data.repository

import fr.leboncoin.data.local.album.entity.AlbumEntity
import fr.leboncoin.data.local.album.service.AlbumLocalService
import fr.leboncoin.data.remote.album.dto.AlbumDto
import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.domain.model.Album
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
class AlbumRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var albumRemoteService: AlbumRemoteService
    private lateinit var albumLocalService: AlbumLocalService
    private lateinit var repository: AlbumRepositoryImpl

    @Before
    fun setUp() {
        albumRemoteService = mockk()
        albumLocalService = mockk()
        repository = AlbumRepositoryImpl(
            albumRemoteService = albumRemoteService,
            albumLocalService = albumLocalService,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `getAllAlbums emits cached data when cache is not empty`() = runTest(testDispatcher) {
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        every { albumLocalService.getAlbums() } returns MutableStateFlow(cached)

        val albums = repository.getAllAlbums().first().getOrThrow()

        assertEquals(listOf("cached"), albums.map { it.title })
    }

    @Test
    fun `getAllAlbums calls refresh from remote and emits the result when the cache is empty`() = runTest(testDispatcher) {
        val remoteAlbums = listOf(AlbumDto(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
        val localAlbums = MutableStateFlow(emptyList<AlbumEntity>())
        every { albumLocalService.getAlbums() } returns localAlbums
        coEvery { albumRemoteService.getAlbums() } returns remoteAlbums
        coEvery { albumLocalService.saveAlbums(any()) } answers { localAlbums.value = firstArg() }

        val emissions = mutableListOf<Result<List<Album>>>()
        val job = launch { repository.getAllAlbums().toList(emissions) }
        advanceUntilIdle()
        job.cancel()

        assertEquals(listOf(emptyList(), listOf("fresh")), emissions.map { result -> result.getOrThrow().map { it.title } })
    }

    @Test
    fun `getAllAlbums does not call refresh when the cache is not empty`() = runTest(testDispatcher) {
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        every { albumLocalService.getAlbums() } returns MutableStateFlow(cached)

        val job = launch { repository.getAllAlbums().collect { } }
        advanceUntilIdle()
        job.cancel()

        coVerify(exactly = 0) { albumRemoteService.getAlbums() }
    }

    @Test
    fun `getAllAlbums emits a Result failure when an exception is thrown`() = runTest(testDispatcher) {
        every { albumLocalService.getAlbums() } returns flow { throw RuntimeException("db unavailable") }

        val result = repository.getAllAlbums().first()

        assertTrue(result.isFailure)
    }
}
