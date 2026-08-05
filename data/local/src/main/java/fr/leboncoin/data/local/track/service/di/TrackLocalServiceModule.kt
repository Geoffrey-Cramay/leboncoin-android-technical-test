package fr.leboncoin.data.local.track.service.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.local.track.service.TrackLocalService
import fr.leboncoin.data.local.track.service.TrackLocalServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TrackLocalServiceModule {

    @Binds
    @Singleton
    abstract fun bindTrackLocalService(impl: TrackLocalServiceImpl): TrackLocalService
}