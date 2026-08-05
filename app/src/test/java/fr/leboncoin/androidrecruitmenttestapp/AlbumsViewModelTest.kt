package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    private fun viewModel(result: suspend () -> Result<List<Album>>) =
        AlbumsViewModel(fakeRepository(result))

    @Test
    fun `loadAlbums emits the repository result`() = runTest {
        val expected = listOf(Album(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        val vm = viewModel { Result.success(expected) }

        vm.loadAlbums()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, vm.albums.value)
    }

    @Test
    fun `loadAlbums failure leaves albums unchanged`() = runTest {
        val vm = viewModel { Result.failure(RuntimeException("network error")) }

        vm.loadAlbums()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.albums.value.isEmpty())
    }

    private fun fakeRepository(result: suspend () -> Result<List<Album>>) = object : AlbumRepository {
        override suspend fun getAllAlbums(): Result<List<Album>> = result()
    }
}
