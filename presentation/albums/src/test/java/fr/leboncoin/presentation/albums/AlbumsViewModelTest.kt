package fr.leboncoin.presentation.albums

import app.cash.turbine.test
import fr.leboncoin.domain.error.AlbumError
import fr.leboncoin.domain.model.Album
import fr.leboncoin.domain.repository.AlbumRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
    private lateinit var repository: AlbumRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk<AlbumRepository>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState starts as Loading before the repository emits`() = runTest {
        every { repository.getAllAlbums() } returns MutableStateFlow(Result.success(emptyList<Album>()))

        val viewModel = AlbumsViewModel(repository)

        viewModel.uiState.test {
            assertEquals(AlbumsUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState becomes Success once the repository emits data`() = runTest {
        val expected = listOf(Album(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        every { repository.getAllAlbums() } returns MutableStateFlow(Result.success(expected))

        val viewModel = AlbumsViewModel(repository)

        viewModel.uiState.test {
            assertEquals(AlbumsUiState.Loading, awaitItem())
            assertEquals(AlbumsUiState.Success(expected), awaitItem())
        }
    }

    @Test
    fun `uiState is updated when the repository's flow emits again`() = runTest {
        val source = MutableStateFlow(Result.success(emptyList<Album>()))
        every { repository.getAllAlbums() } returns source

        val viewModel = AlbumsViewModel(repository)

        viewModel.uiState.test {
            assertEquals(AlbumsUiState.Loading, awaitItem())

            val refreshed = listOf(Album(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
            source.value = Result.success(refreshed)

            assertEquals(AlbumsUiState.Success(refreshed), awaitItem())
        }
    }

    @Test
    fun `uiState becomes Error when the repository emits a failure`() = runTest {
        val source = MutableStateFlow<Result<List<Album>>>(Result.failure(AlbumError.NetworkError()))
        every { repository.getAllAlbums() } returns source

        val viewModel = AlbumsViewModel(repository)

        viewModel.uiState.test {
            assertEquals(AlbumsUiState.Loading, awaitItem())
            assertTrue(awaitItem() is AlbumsUiState.Error)
        }
    }
}
