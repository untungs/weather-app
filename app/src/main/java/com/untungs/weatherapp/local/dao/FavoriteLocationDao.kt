package com.untungs.weatherapp.local.dao

import androidx.room.*
import com.untungs.weatherapp.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLocationDao {

    @Query(value = "SELECT * FROM favorite_location")
    fun getFavoriteLocations(): Flow<List<FavoriteLocationEntity>>

    @Query(value = "SELECT * FROM favorite_location WHERE lat = :lat AND lon = :lon")
    suspend fun getFavoriteLocation(lat: Float, lon: Float): FavoriteLocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteLocationEntity: FavoriteLocationEntity)

    @Update
    suspend fun update(entity: FavoriteLocationEntity)
}