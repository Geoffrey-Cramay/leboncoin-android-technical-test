package fr.leboncoin.androidrecruitmenttestapp

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp
import fr.leboncoin.androidrecruitmenttestapp.di.ImageOkHttpClient
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltAndroidApp
class TracksApp : Application() {

    @Inject
    @ImageOkHttpClient
    lateinit var imageOkHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()

        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components { add(OkHttpNetworkFetcherFactory(callFactory = { imageOkHttpClient })) }
                .crossfade(true)
                .build()
        }
    }
}
