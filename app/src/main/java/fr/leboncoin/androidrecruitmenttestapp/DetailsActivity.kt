package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.presentation.details.AlbumDetailsScreen
import fr.leboncoin.presentation.details.AlbumDetailsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: AlbumDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analyticsHelper.trackScreenView("Details")

        setContent {
            AppTheme {
                AlbumDetailsScreen(viewModel = viewModel)
            }
        }
    }

    companion object {
        const val EXTRA_ALBUM_ID = AlbumDetailsViewModel.ALBUM_ID_KEY
    }
}
