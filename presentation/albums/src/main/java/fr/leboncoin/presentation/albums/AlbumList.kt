package fr.leboncoin.presentation.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.leboncoin.domain.model.Album

@Composable
fun AlbumList(
    albums: List<Album>,
    onItemSelected: (Album) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
    ) {
        items(
            items = albums,
            key = { album -> album.id },
        ) { album ->
            AlbumItem(
                album = album,
                onItemSelected = onItemSelected,
            )
        }
    }
}
