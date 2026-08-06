package fr.leboncoin.presentation.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.domain.model.Track

@Composable
fun TrackList(
    tracks: List<Track>,
    onItemSelected: (Track) -> Unit,
    onFavoriteClicked: (Track) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
    ) {
        items(
            items = tracks,
            key = { track -> track.id },
        ) { track ->
            TrackItem(
                track = track,
                onItemSelected = onItemSelected,
                onFavoriteClicked = onFavoriteClicked,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackListPreview() {
    AppTheme {
        TrackList(
            tracks = listOf(
                Track(
                    id = 1,
                    albumId = 1,
                    title = "Bohemian Rhapsody",
                    url = "https://example.com/track.jpg",
                    thumbnailUrl = "https://example.com/thumbnail.jpg",
                    isFavorite = true,
                ),
                Track(
                    id = 2,
                    albumId = 1,
                    title = "Don't Stop Me Now",
                    url = "https://example.com/track.jpg",
                    thumbnailUrl = "https://example.com/thumbnail.jpg",
                    isFavorite = false,
                ),
            ),
            onItemSelected = {},
            onFavoriteClicked = {},
        )
    }
}
