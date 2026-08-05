package fr.leboncoin.androidrecruitmenttestapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import fr.leboncoin.presentation.albums.AlbumsScreen
import fr.leboncoin.presentation.albums.AlbumsViewModel
import fr.leboncoin.designsystem.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AlbumsViewModel by viewModels()

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                AlbumsScreen(
                    viewModel = viewModel,
                    onItemSelected = {
                        analyticsHelper.trackSelection(it.id.toString())
                        startActivity(
                            Intent(this, DetailsActivity::class.java).apply {
                                putExtra(DetailsActivity.EXTRA_ALBUM_ID, it.id)
                            }
                        )
                    }
                )
            }
        }
    }
}
