package fr.leboncoin.presentation.tracks

import app.cash.turbine.test
import fr.leboncoin.domain.error.TrackError
import fr.leboncoin.domain.model.Track
import fr.leboncoin.domain.usecase.GetTracksUseCase
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
class TracksViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var getTracksUseCase: GetTracksUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        getTracksUseCase = mockk<GetTracksUseCase>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState starts as Loading before the use case emits`() = runTest {
        every { getTracksUseCase() } returns MutableStateFlow(Result.success(emptyList<Track>()))

        val viewModel = TracksViewModel(getTracksUseCase)

        viewModel.uiState.test {
            assertEquals(TracksUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState becomes Success once the use case emits data`() = runTest {
        val expected = listOf(Track(id = 1, albumId = 1, title = "t", url = "u", thumbnailUrl = "tu"))
        every { getTracksUseCase() } returns MutableStateFlow(Result.success(expected))

        val viewModel = TracksViewModel(getTracksUseCase)

        viewModel.uiState.test {
            assertEquals(TracksUiState.Loading, awaitItem())
            assertEquals(TracksUiState.Success(expected), awaitItem())
        }
    }

    @Test
    fun `uiState is updated when the use case's flow emits again`() = runTest {
        val source = MutableStateFlow(Result.success(emptyList<Track>()))
        every { getTracksUseCase() } returns source

        val viewModel = TracksViewModel(getTracksUseCase)

        viewModel.uiState.test {
            assertEquals(TracksUiState.Loading, awaitItem())

            val refreshed = listOf(Track(id = 1, albumId = 1, title = "fresh", url = "u", thumbnailUrl = "tu"))
            source.value = Result.success(refreshed)

            assertEquals(TracksUiState.Success(refreshed), awaitItem())
        }
    }

    @Test
    fun `uiState becomes Error when the use case emits a failure`() = runTest {
        val source = MutableStateFlow<Result<List<Track>>>(Result.failure(TrackError.NetworkError()))
        every { getTracksUseCase() } returns source

        val viewModel = TracksViewModel(getTracksUseCase)

        viewModel.uiState.test {
            assertEquals(TracksUiState.Loading, awaitItem())
            assertTrue(awaitItem() is TracksUiState.Error)
        }
    }
}
