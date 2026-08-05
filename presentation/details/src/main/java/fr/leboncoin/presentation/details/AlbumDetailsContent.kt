package fr.leboncoin.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import fr.leboncoin.designsystem.LabeledChipRow
import fr.leboncoin.designsystem.RemoteImage
import fr.leboncoin.domain.model.Album

@Composable
fun AlbumDetailsContent(
    album: Album,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(modifier = modifier.fillMaxSize().padding(contentPadding)) {
        RemoteImage(
            url = album.url,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )

        Text(
            text = album.title,
            style = SparkTheme.typography.headline1,
            modifier = Modifier.padding(16.dp),
        )

        LabeledChipRow(
            albumId = album.albumId,
            trackId = album.id,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
