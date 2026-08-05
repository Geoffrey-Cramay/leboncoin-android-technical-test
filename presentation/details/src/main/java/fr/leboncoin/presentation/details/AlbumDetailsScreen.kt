package fr.leboncoin.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.designsystem.LabeledChipRow
import fr.leboncoin.designsystem.RemoteImage

@Composable
fun AlbumDetailsScreen(
    viewModel: AlbumDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val album by viewModel.album.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) { contentPadding ->
        album?.let {
            Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                RemoteImage(
                    url = it.url,
                    contentDescription = it.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )

                Text(
                    text = it.title,
                    style = SparkTheme.typography.headline1,
                    modifier = Modifier.padding(16.dp),
                )

                LabeledChipRow(
                    labels = listOf("Album #${it.albumId}", "Track #${it.id}"),
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}
