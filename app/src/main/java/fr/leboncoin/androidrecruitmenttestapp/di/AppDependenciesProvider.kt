package fr.leboncoin.androidrecruitmenttestapp.di

import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper
import fr.leboncoin.data.remote.di.RemoteDependencies
import fr.leboncoin.data.repository.AlbumRepositoryImpl
import fr.leboncoin.domain.repository.AlbumRepository
import java.util.logging.Logger

interface AppDependenciesProvider {
    val dependencies: AppDependencies
}

class AppDependencies {
    val logger: Logger by lazy { Logger.getGlobal() }
    val analyticsHelper: AnalyticsHelper by lazy { AnalyticsHelper() }
    val albumRepository: AlbumRepository by lazy { AlbumRepositoryImpl(remoteDependencies.albumApiService) }

    private val remoteDependencies: RemoteDependencies by lazy { RemoteDependencies() }
}
