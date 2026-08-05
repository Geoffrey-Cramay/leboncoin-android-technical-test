package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.data.network.api.AlbumApiService
import fr.leboncoin.data.network.model.AlbumDto
import fr.leboncoin.data.repository.AlbumRepository
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

    private fun viewModel(service: AlbumApiService) =
        AlbumsViewModel(AlbumRepository(service))

    @Test
    fun `loadAlbums emits the repository result`() = runTest {
        val expected = listOf(AlbumDto(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        val vm = viewModel(fakeService { expected })

        vm.loadAlbums()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(expected, vm.albums.value)
    }

    @Test
    fun `loadAlbums failure leaves albums unchanged`() = runTest {
        val vm = viewModel(fakeService { throw RuntimeException("network error") })

        vm.loadAlbums()
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(vm.albums.value.isEmpty())
    }

    private fun fakeService(getAlbums: suspend () -> List<AlbumDto>) = object : AlbumApiService {
        override suspend fun getAlbums(): List<AlbumDto> = getAlbums()
    }
}
