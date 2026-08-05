package fr.leboncoin.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage
import com.adevinta.spark.components.image.Illustration
import com.adevinta.spark.components.progress.Spinner
import com.adevinta.spark.icons.ErrorPhoto
import com.adevinta.spark.icons.SparkIcons

@Composable
fun RemoteImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Spinner()
            }
        },
        error = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Illustration(
                    sparkIcon = SparkIcons.ErrorPhoto,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.3f),
                )
            }
        },
    )
}
