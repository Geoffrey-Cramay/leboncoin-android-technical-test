package fr.leboncoin.data.local.album.service.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.local.album.service.AlbumLocalService
import fr.leboncoin.data.local.album.service.AlbumLocalServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AlbumLocalServiceModule {

    @Binds
    @Singleton
    abstract fun bindAlbumLocalService(impl: AlbumLocalServiceImpl): AlbumLocalService
}