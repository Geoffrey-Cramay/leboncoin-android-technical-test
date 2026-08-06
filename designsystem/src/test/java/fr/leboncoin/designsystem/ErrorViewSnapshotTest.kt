package fr.leboncoin.designsystem

import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class ErrorViewSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun `network error message`() {
        paparazzi.snapshot {
            AppTheme {
                ErrorView(message = "Check your internet connection.")
            }
        }
    }

    @Test
    fun `long error message wraps`() {
        paparazzi.snapshot {
            AppTheme {
                ErrorView(
                    message = "Something went wrong while loading this content. " +
                        "Please try again in a few moments.",
                )
            }
        }
    }
}
