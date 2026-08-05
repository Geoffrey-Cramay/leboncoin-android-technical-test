package fr.leboncoin.data.local.album.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.leboncoin.data.local.album.entity.AlbumEntity
import fr.leboncoin.data.local.shared.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var albumDao: AlbumDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        albumDao = database.albumDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAlbums_thenGetAlbums_returnsInsertedAlbums() = runTest {
        val albums = listOf(
            AlbumEntity(id = 1, albumId = 1, title = "t1", url = "u1", thumbnailUrl = "tu1"),
            AlbumEntity(id = 2, albumId = 1, title = "t2", url = "u2", thumbnailUrl = "tu2"),
        )

        albumDao.insertAlbums(albums)

        assertEquals(albums, albumDao.getAlbums().first())
    }
}
