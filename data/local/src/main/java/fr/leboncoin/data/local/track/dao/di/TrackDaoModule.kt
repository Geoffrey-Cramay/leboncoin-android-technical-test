package fr.leboncoin.data.local.track.dao.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.local.track.dao.TrackDao
import fr.leboncoin.data.local.shared.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object TrackDaoModule {

    @Provides
    fun provideTrackDao(appDatabase: AppDatabase): TrackDao = appDatabase.trackDao()
}