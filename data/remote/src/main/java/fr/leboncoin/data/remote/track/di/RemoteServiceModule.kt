package fr.leboncoin.data.remote.track.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.remote.track.service.TrackRemoteService
import fr.leboncoin.data.remote.track.service.TrackRemoteServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteServiceModule {

    @Binds
    @Singleton
    abstract fun bindTrackRemoteService(impl: TrackRemoteServiceImpl): TrackRemoteService
}
