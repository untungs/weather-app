package com.untungs.weatherapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.untungs.weatherapp.local.dao.FavoriteLocationDao
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity

@Database(
    entities = [FavoriteLocationEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteLocationDao(): FavoriteLocationDao
}