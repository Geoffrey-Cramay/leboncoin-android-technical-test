package fr.leboncoin.data.local.track.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.leboncoin.data.local.track.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TrackDao {

    @Query("SELECT * FROM tracks")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE id = :id")
    fun getTrackById(id: Int): Flow<TrackEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)
}
