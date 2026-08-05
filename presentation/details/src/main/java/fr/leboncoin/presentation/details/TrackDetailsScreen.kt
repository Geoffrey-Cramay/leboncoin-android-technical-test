package fr.leboncoin.presentation.details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import com.adevinta.spark.components.snackbars.SnackbarHost
import com.adevinta.spark.components.snackbars.SnackbarHostState
import fr.leboncoin.designsystem.ErrorView
import fr.leboncoin.designsystem.Loader

// stringResource() can't be called from the LaunchedEffect below (non-composable scope) since the
// message res id isn't known until favoriteToggleErrors emits it.
@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun TrackDetailsScreen(
    viewModel: TrackDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.favoriteToggleErrors.collect { messageRes ->
            snackbarHostState.showSnackbar(message = context.getString(messageRes))
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        when (val state = uiState) {
            is TrackDetailsUiState.Loading -> {
                Loader(modifier = Modifier.padding(contentPadding))
            }

            is TrackDetailsUiState.Error -> {
                ErrorView(
                    message = stringResource(id = state.messageRes),
                    modifier = Modifier.padding(contentPadding),
                )
            }

            is TrackDetailsUiState.Success -> {
                TrackDetailsContent(
                    track = state.track,
                    onFavoriteClicked = viewModel::onFavoriteClicked,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}
