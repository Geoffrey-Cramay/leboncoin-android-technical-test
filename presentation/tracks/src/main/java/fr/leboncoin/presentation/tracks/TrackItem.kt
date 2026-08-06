package fr.leboncoin.presentation.tracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.card.Card
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonGhost
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.icons.SparkIcons
import com.adevinta.spark.icons.LikeFill
import com.adevinta.spark.icons.LikeOutline
import fr.leboncoin.domain.model.Track
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.designsystem.LabeledChipRow
import fr.leboncoin.designsystem.RemoteImage

@OptIn(ExperimentalSparkApi::class)
@Composable
fun TrackItem(
    track: Track,
    onItemSelected: (Track) -> Unit,
    onFavoriteClicked: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 16.dp),
        onClick = { onItemSelected(track) },
    ) {
        Row {
            RemoteImage(
                url = track.thumbnailUrl,
                contentDescription = track.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp),
            ) {
                Text(
                    text = track.title,
                    style = SparkTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.weight(1f))

                LabeledChipRow(
                    albumId = track.albumId,
                    trackId = track.id,
                )
            }

            IconToggleButtonGhost(
                checked = track.isFavorite,
                onCheckedChange = { onFavoriteClicked(track) },
                icons = IconToggleButtonIcons(
                    checked = SparkIcons.LikeFill,
                    unchecked = SparkIcons.LikeOutline,
                ),
                contentDescription = stringResource(R.string.tracks_favorite_content_description),
            )
        }
    }
}

private val PREVIEW_TRACK = Track(
    id = 1,
    albumId = 1,
    title = "Bohemian Rhapsody",
    url = "https://example.com/track.jpg",
    thumbnailUrl = "https://example.com/thumbnail.jpg",
    isFavorite = false,
)

@Preview(showBackground = true)
@Composable
private fun TrackItemPreview() {
    AppTheme {
        TrackItem(
            track = PREVIEW_TRACK,
            onItemSelected = {},
            onFavoriteClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackItemFavoritePreview() {
    AppTheme {
        TrackItem(
            track = PREVIEW_TRACK.copy(isFavorite = true),
            onItemSelected = {},
            onFavoriteClicked = {},
        )
    }
}
