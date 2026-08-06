package fr.leboncoin.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adevinta.spark.components.progress.Spinner

@Composable
fun Loader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Spinner()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoaderPreview() {
    AppTheme {
        Loader()
    }
}
