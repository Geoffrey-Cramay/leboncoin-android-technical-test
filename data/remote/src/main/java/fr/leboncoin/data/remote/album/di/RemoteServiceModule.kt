package fr.leboncoin.data.remote.album.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.remote.album.service.AlbumRemoteService
import fr.leboncoin.data.remote.album.service.AlbumRemoteServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteServiceModule {

    @Binds
    @Singleton
    abstract fun bindAlbumRemoteService(impl: AlbumRemoteServiceImpl): AlbumRemoteService
}
