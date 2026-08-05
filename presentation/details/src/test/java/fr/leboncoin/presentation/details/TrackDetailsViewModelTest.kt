package fr.leboncoin.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.repository.TrackRepository
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
class TrackDetailsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: TrackRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk<TrackRepository>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState starts as Loading before the repository emits`() = runTest {
        every { repository.getTrackById(any()) } returns MutableStateFlow(Result.failure(TrackError.NotFoundError()))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(TrackDetailsUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState becomes Success once the repository emits the matching track`() = runTest {
        val track = Track(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu")
        every { repository.getTrackById(any()) } returns MutableStateFlow(Result.success(track))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(TrackDetailsUiState.Loading, awaitItem())
            assertEquals(TrackDetailsUiState.Success(track), awaitItem())
        }
    }

    @Test
    fun `uiState becomes Error when the repository emits a failure`() = runTest {
        every { repository.getTrackById(any()) } returns MutableStateFlow(Result.failure(TrackError.UnknownError()))

        val viewModel = viewModel()

        viewModel.uiState.test {
            assertEquals(TrackDetailsUiState.Loading, awaitItem())
            assertTrue(awaitItem() is TrackDetailsUiState.Error)
        }
    }

    private fun viewModel() = TrackDetailsViewModel(
        savedStateHandle = SavedStateHandle(mapOf(TrackDetailsViewModel.TRACK_ID_KEY to 1)),
        repository = repository,
    )
}
