package fr.leboncoin.presentation.albums

import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `albums returns the repository's data once observed`() = runTest {
        val expected = listOf(Album(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        val vm = AlbumsViewModel(fakeRepository(MutableStateFlow(expected)))

        val job = launch { vm.albums.collect { } }
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, vm.albums.value)
        job.cancel()
    }

    @Test
    fun `albums is updated when the repository's flow emits again`() = runTest {
        val source = MutableStateFlow(emptyList<Album>())
        val vm = AlbumsViewModel(fakeRepository(source))

        val job = launch { vm.albums.collect { } }
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals(emptyList<Album>(), vm.albums.value)

        val refreshed = listOf(Album(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
        source.value = refreshed
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(refreshed, vm.albums.value)
        job.cancel()
    }

    private fun fakeRepository(albums: Flow<List<Album>>) = object : AlbumRepository {
        override fun getAllAlbums(): Flow<List<Album>> = albums
    }
}
