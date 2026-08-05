package fr.leboncoin.data.local.track.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.leboncoin.data.local.shared.AppDatabase
import fr.leboncoin.data.local.track.entity.TrackEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TrackDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var trackDao: TrackDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        trackDao = database.trackDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTracks_thenGetTracks_returnsInsertedTracks() = runTest {
        val tracks = listOf(
            TrackEntity(id = 1, albumId = 1, title = "t1", url = "u1", thumbnailUrl = "tu1"),
            TrackEntity(id = 2, albumId = 1, title = "t2", url = "u2", thumbnailUrl = "tu2"),
        )

        trackDao.insertTracks(tracks)

        assertEquals(tracks, trackDao.getTracks().first())
    }

    @Test
    fun insertTracks_withExistingId_replacesTheExistingRow() = runTest {
        val original = TrackEntity(id = 1, albumId = 1, title = "original", url = "u1", thumbnailUrl = "tu1")
        trackDao.insertTracks(listOf(original))

        val updated = original.copy(title = "updated")
        trackDao.insertTracks(listOf(updated))

        assertEquals(listOf(updated), trackDao.getTracks().first())
    }

    @Test
    fun getTracks_afterSecondInsert_emitsUpdatedList() = runTest {
        val firstTracks = listOf(TrackEntity(id = 1, albumId = 1, title = "t1", url = "u1", thumbnailUrl = "tu1"))
        trackDao.insertTracks(firstTracks)

        val emissions = mutableListOf<List<TrackEntity>>()
        val job = launch { trackDao.getTracks().collect { emissions.add(it) } }
        advanceUntilIdle()

        val secondTracks = firstTracks + TrackEntity(id = 2, albumId = 1, title = "t2", url = "u2", thumbnailUrl = "tu2")
        trackDao.insertTracks(secondTracks)
        advanceUntilIdle()

        job.cancel()
        assertEquals(listOf(firstTracks, secondTracks), emissions)
    }

    @Test
    fun toggleFavorite_flipsIsFavorite() = runTest {
        trackDao.insertTracks(listOf(TrackEntity(id = 1, albumId = 1, title = "t1", url = "u1", thumbnailUrl = "tu1")))

        trackDao.toggleFavorite(1)
        assertEquals(true, trackDao.getTrackById(1).first()?.isFavorite)

        trackDao.toggleFavorite(1)
        assertEquals(false, trackDao.getTrackById(1).first()?.isFavorite)
    }

    @Test
    fun getFavoriteTrackIds_returnsOnlyFavoritedIds() = runTest {
        trackDao.insertTracks(
            listOf(
                TrackEntity(id = 1, albumId = 1, title = "t1", url = "u1", thumbnailUrl = "tu1"),
                TrackEntity(id = 2, albumId = 1, title = "t2", url = "u2", thumbnailUrl = "tu2"),
            ),
        )

        trackDao.toggleFavorite(1)

        assertEquals(listOf(1), trackDao.getFavoriteTrackIds())
    }
}
