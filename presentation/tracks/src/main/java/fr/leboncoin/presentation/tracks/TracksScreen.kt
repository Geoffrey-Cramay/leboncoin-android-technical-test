package fr.leboncoin.presentation.tracks

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.designsystem.ErrorView
import fr.leboncoin.designsystem.Loader
import fr.leboncoin.domain.model.Track

@Composable
fun TracksScreen(
    viewModel: TracksViewModel,
    onItemSelected: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) { contentPadding ->
        when (val state = uiState) {
            is TracksUiState.Loading -> {
                Loader(modifier = Modifier.padding(contentPadding))
            }

            is TracksUiState.Error -> {
                ErrorView(
                    message = stringResource(id = state.messageRes),
                    modifier = Modifier.padding(contentPadding),
                )
            }

            is TracksUiState.Success -> {
                TrackList(
                    tracks = state.tracks,
                    onItemSelected = onItemSelected,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}
