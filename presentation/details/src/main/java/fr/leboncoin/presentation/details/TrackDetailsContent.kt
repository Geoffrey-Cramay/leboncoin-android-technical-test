package fr.leboncoin.presentation.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.iconbuttons.IconButtonSize
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.LikeOutline
import com.adevinta.spark.icons.SparkIcons
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.designsystem.LabeledChipRow
import fr.leboncoin.designsystem.RemoteImage
import fr.leboncoin.domain.model.Track

@OptIn(ExperimentalSparkApi::class)
@Composable
fun TrackDetailsContent(
    track: Track,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        RemoteImage(
            url = track.url,
            contentDescription = track.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(IMAGE_SIZE),
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = track.title,
                style = SparkTheme.typography.headline1,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            )

            IconToggleButtonGhost(
                checked = track.isFavorite,
                onCheckedChange = { onFavoriteClicked() },
                icons = IconToggleButtonIcons(
                    checked = SparkIcons.LikeFill,
                    unchecked = SparkIcons.LikeOutline,
                ),
                contentDescription = stringResource(R.string.details_favorite_content_description),
                size = IconButtonSize.Large,
            )
        }

        LabeledChipRow(
            albumId = track.albumId,
            trackId = track.id,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

private val IMAGE_SIZE = 200.dp

private val previewTrack = Track(
    id = 1,
    albumId = 1,
    title = "Bohemian Rhapsody",
    url = "https://example.com/track.jpg",
    thumbnailUrl = "https://example.com/thumbnail.jpg",
    isFavorite = false,
)

@Preview(showBackground = true)
@Composable
private fun TrackDetailsContentPreview() {
    AppTheme {
        TrackDetailsContent(
            track = previewTrack,
            onFavoriteClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackDetailsContentFavoritePreview() {
    AppTheme {
        TrackDetailsContent(
            track = previewTrack.copy(isFavorite = true),
            onFavoriteClicked = {},
        )
    }
}
