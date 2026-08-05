package fr.leboncoin.presentation.details

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.designsystem.ErrorView
import fr.leboncoin.designsystem.Loader

@Composable
fun AlbumDetailsScreen(
    viewModel: AlbumDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) { contentPadding ->
        when (val state = uiState) {
            is AlbumDetailsUiState.Loading -> {
                Loader(modifier = Modifier.padding(contentPadding))
            }

            is AlbumDetailsUiState.Error -> {
                ErrorView(
                    message = stringResource(id = state.messageRes),
                    modifier = Modifier.padding(contentPadding),
                )
            }

            is AlbumDetailsUiState.Success -> {
                AlbumDetailsContent(
                    album = state.album,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}
