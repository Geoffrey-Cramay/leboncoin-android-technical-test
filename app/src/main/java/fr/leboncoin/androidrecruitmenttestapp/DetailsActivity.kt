package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import fr.leboncoin.designsystem.AppTheme
import fr.leboncoin.presentation.details.TrackDetailsScreen
import fr.leboncoin.presentation.details.TrackDetailsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: TrackDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analyticsHelper.trackScreenView("Details")

        setContent {
            AppTheme {
                TrackDetailsScreen(viewModel = viewModel)
            }
        }
    }

    companion object {
        const val EXTRA_TRACK_ID = TrackDetailsViewModel.TRACK_ID_KEY
    }
}
