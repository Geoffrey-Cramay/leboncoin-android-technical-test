package fr.leboncoin.data.local.album.service

import fr.leboncoin.data.local.album.dao.AlbumDao
import fr.leboncoin.data.local.album.entity.AlbumEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class AlbumLocalServiceImpl @Inject constructor(
    private val albumDao: AlbumDao,
) : AlbumLocalService {

    override fun getAlbums(): Flow<List<AlbumEntity>> = albumDao.getAlbums()

    override fun getAlbumById(id: Int): Flow<AlbumEntity?> = albumDao.getAlbumById(id)

    override suspend fun saveAlbums(albums: List<AlbumEntity>) = albumDao.insertAlbums(albums)
}
