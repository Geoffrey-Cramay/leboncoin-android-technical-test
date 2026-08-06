package fr.leboncoin.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.chips.ChipTinted

@OptIn(ExperimentalSparkApi::class)
@Composable
fun LabeledChipRow(
    albumId: Int,
    trackId: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ChipTinted(text = stringResource(R.string.label_album_number, albumId))
        ChipTinted(text = stringResource(R.string.label_track_number, trackId))
    }
}

@Preview(showBackground = true)
@Composable
private fun LabeledChipRowPreview() {
    AppTheme {
        LabeledChipRow(albumId = 2, trackId = 1)
    }
}
