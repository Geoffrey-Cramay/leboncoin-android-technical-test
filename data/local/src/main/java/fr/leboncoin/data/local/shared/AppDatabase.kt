package fr.leboncoin.data.local.shared

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.leboncoin.data.local.track.dao.TrackDao
import fr.leboncoin.data.local.track.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 2)
internal abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}
