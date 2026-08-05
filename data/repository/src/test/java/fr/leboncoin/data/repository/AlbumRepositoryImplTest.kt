package fr.leboncoin.data.repository

import fr.leboncoin.data.local.album.entity.AlbumEntity
import fr.leboncoin.data.local.album.service.AlbumLocalService
import fr.leboncoin.data.remote.album.dto.AlbumDto
import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.domain.model.Album
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumRepositoryImplTest {

    @Test
    fun `getAllAlbums emits cached data when cache is not empty`() = runTest {
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        val repository = repository(
            albumRemoteService = fakeRemoteService { error("should never be called") },
            albumLocalService = fakeLocalService(initial = cached),
        )

        val albums = repository.getAllAlbums().first().getOrThrow()

        assertEquals(listOf("cached"), albums.map { it.title })
    }

    @Test
    fun `getAllAlbums calls refresh from remote and emits the result when the cache is empty`() = runTest {
        val remoteAlbums = listOf(AlbumDto(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
        val repository = repository(
            albumRemoteService = fakeRemoteService { remoteAlbums },
            albumLocalService = fakeLocalService(initial = emptyList()),
        )

        val emissions = mutableListOf<Result<List<Album>>>()
        val job = launch { repository.getAllAlbums().toList(emissions) }
        advanceUntilIdle()
        job.cancel()

        assertEquals(listOf(emptyList(), listOf("fresh")), emissions.map { result -> result.getOrThrow().map { it.title } })
    }

    @Test
    fun `getAllAlbums does not call refresh when the cache is not empty`() = runTest {
        var refreshCallCount = 0
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        val repository = repository(
            albumRemoteService = fakeRemoteService {
                refreshCallCount++
                emptyList()
            },
            albumLocalService = fakeLocalService(initial = cached),
        )

        val job = launch { repository.getAllAlbums().collect { } }
        advanceUntilIdle()
        job.cancel()

        assertEquals(0, refreshCallCount)
    }

    @Test
    fun `getAllAlbums emits a Result failure when an exception is thrown`() = runTest {
        val repository = repository(
            albumRemoteService = fakeRemoteService { error("should never be called") },
            albumLocalService = object : AlbumLocalService {
                override fun getAlbums(): Flow<List<AlbumEntity>> = flow { throw RuntimeException("db unavailable") }
                override fun getAlbumById(id: Int): Flow<AlbumEntity?> = error("not used")
                override suspend fun saveAlbums(albums: List<AlbumEntity>) = error("not used")
            },
        )

        val result = repository.getAllAlbums().first()

        assertTrue(result.isFailure)
    }

    @Test
    fun `getAlbumById emits the matching cached album`() = runTest {
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        val repository = repository(
            albumRemoteService = fakeRemoteService { error("should never be called") },
            albumLocalService = fakeLocalService(initial = cached),
        )

        val album = repository.getAlbumById(1).first()

        assertEquals("cached", album?.title)
    }

    private fun TestScope.repository(
        albumRemoteService: AlbumRemoteService,
        albumLocalService: AlbumLocalService,
    ) = AlbumRepositoryImpl(
        albumRemoteService = albumRemoteService,
        albumLocalService = albumLocalService,
        ioDispatcher = StandardTestDispatcher(testScheduler),
    )

    private fun fakeRemoteService(getAlbumDtos: suspend () -> List<AlbumDto>) = object : AlbumRemoteService {
        override suspend fun getAlbums(): List<AlbumDto> = getAlbumDtos()
    }

    private fun fakeLocalService(initial: List<AlbumEntity>) = object : AlbumLocalService {
        private val albums = MutableStateFlow(initial)

        override fun getAlbums(): Flow<List<AlbumEntity>> = albums

        override fun getAlbumById(id: Int): Flow<AlbumEntity?> =
            albums.map { entities -> entities.find { it.id == id } }

        override suspend fun saveAlbums(albums: List<AlbumEntity>) {
            this.albums.value = albums
        }
    }
}
