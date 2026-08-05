package fr.leboncoin.presentation.details

import androidx.lifecycle.SavedStateHandle
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
class AlbumDetailsViewModelTest {

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
        every { repository.getAlbumById(any()) } returns MutableStateFlow(Result.failure(AlbumError.NotFoundError()))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(AlbumDetailsUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState becomes Success once the repository emits the matching album`() = runTest {
        val album = Album(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu")
        every { repository.getAlbumById(any()) } returns MutableStateFlow(Result.success(album))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(AlbumDetailsUiState.Loading, awaitItem())
            assertEquals(AlbumDetailsUiState.Success(album), awaitItem())
        }
    }

    @Test
    fun `uiState becomes Error when the repository emits a failure`() = runTest {
        every { repository.getAlbumById(any()) } returns MutableStateFlow(Result.failure(AlbumError.UnknownError()))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(AlbumDetailsUiState.Loading, awaitItem())
            assertTrue(awaitItem() is AlbumDetailsUiState.Error)
        }
    }

    private fun viewModel() = AlbumDetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(AlbumDetailsViewModel.ALBUM_ID_KEY to 1)),
        repository = repository,
    )
}
