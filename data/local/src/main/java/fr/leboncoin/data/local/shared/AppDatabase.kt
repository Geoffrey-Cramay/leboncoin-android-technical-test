package fr.leboncoin.data.local.shared

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.leboncoin.data.local.album.dao.AlbumDao
import fr.leboncoin.data.local.album.entity.AlbumEntity

@Database(entities = [AlbumEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao
}
