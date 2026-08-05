package fr.leboncoin.presentation.tracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.leboncoin.domain.model.Track

@Composable
fun TrackList(
    tracks: List<Track>,
    onItemSelected: (Track) -> Unit,
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
            )
        }
    }
}
