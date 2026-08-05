package fr.leboncoin.data.local.album.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.leboncoin.data.local.album.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AlbumDao {

    @Query("SELECT * FROM albums")
    fun getAlbums(): Flow<List<AlbumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
}
