package fr.leboncoin.presentation.details

import app.cash.paparazzi.Paparazzi
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.domain.model.Track
import org.junit.Rule
import org.junit.Test

class TrackDetailsContentSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun `track not favorite`() {
        paparazzi.snapshot {
            AppTheme {
                TrackDetailsContent(
                    track = Track(
                        id = 1,
                        albumId = 1,
                        title = "Bohemian Rhapsody",
                        url = "https://example.com/track.jpg",
                        thumbnailUrl = "https://example.com/thumbnail.jpg",
                        isFavorite = false,
                    ),
                    onFavoriteClicked = {},
                )
            }
        }
    }

    @Test
    fun `track favorite`() {
        paparazzi.snapshot {
            AppTheme {
                TrackDetailsContent(
                    track = Track(
                        id = 1,
                        albumId = 1,
                        title = "Bohemian Rhapsody",
                        url = "https://example.com/track.jpg",
                        thumbnailUrl = "https://example.com/thumbnail.jpg",
                        isFavorite = true,
                    ),
                    onFavoriteClicked = {},
                )
            }
        }
    }
}
