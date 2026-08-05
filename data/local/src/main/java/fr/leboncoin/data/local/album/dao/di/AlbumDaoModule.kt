package fr.leboncoin.data.local.album.dao.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.leboncoin.data.local.album.dao.AlbumDao
import fr.leboncoin.data.local.shared.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
internal object AlbumDaoModule {

    @Provides
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao = appDatabase.albumDao()
}