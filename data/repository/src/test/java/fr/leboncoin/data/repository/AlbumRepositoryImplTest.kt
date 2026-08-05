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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumRepositoryImplTest {

    @Test
    fun `getAllAlbums emits cached data when cache is not empty`() = runTest {
        val cached = listOf(AlbumEntity(id = 1, albumId = 1, title = "cached", url = "u", thumbnailUrl = "tu"))
        val repository = AlbumRepositoryImpl(
            albumRemoteService = fakeRemoteService { error("should never be called") },
            albumLocalService = fakeLocalService(initial = cached),
        )

        val albums = repository.getAllAlbums().first()

        assertEquals(listOf("cached"), albums.map { it.title })
    }

    @Test
    fun `getAllAlbums refreshes from remote and emits the result when the cache is empty`() = runTest {
        val remoteAlbums = listOf(AlbumDto(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
        val repository = AlbumRepositoryImpl(
            albumRemoteService = fakeRemoteService { remoteAlbums },
            albumLocalService = fakeLocalService(initial = emptyList()),
        )

        val emissions = mutableListOf<List<Album>>()
        val job = launch { repository.getAllAlbums().toList(emissions) }
        advanceUntilIdle()
        job.cancel()

        assertEquals(listOf(emptyList(), listOf("fresh")), emissions.map { albums -> albums.map { it.title } })
    }

    @Test
    fun `getAllAlbums stays empty and doesn't throw when remote fails on an empty cache`() = runTest {
        val repository = AlbumRepositoryImpl(
            albumRemoteService = fakeRemoteService { throw RuntimeException("no network") },
            albumLocalService = fakeLocalService(initial = emptyList()),
        )

        val emissions = mutableListOf<List<Album>>()
        val job = launch { repository.getAllAlbums().toList(emissions) }
        advanceUntilIdle()
        job.cancel()

        assertEquals(listOf(emptyList<Album>()), emissions)
    }

    private fun fakeRemoteService(getAlbums: suspend () -> List<AlbumDto>) = object : AlbumRemoteService {
        override suspend fun getAlbums(): List<AlbumDto> = getAlbums()
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
