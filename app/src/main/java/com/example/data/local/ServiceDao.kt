package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ServiceListing
import kotlinx.coroutines.flow.Flow

import com.example.data.model.Review

@Dao
interface ServiceDao {
    @Query("SELECT * FROM reviews WHERE serviceId = :serviceId ORDER BY id DESC")
    fun getReviewsForService(serviceId: Int): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE serviceId = :serviceId")
    suspend fun getReviewsListForService(serviceId: Int): List<Review>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Query("SELECT * FROM service_listings ORDER BY id DESC")
    fun getAllServices(): Flow<List<ServiceListing>>

    @Query("SELECT * FROM service_listings WHERE id = :id LIMIT 1")
    fun getServiceById(id: Int): Flow<ServiceListing?>

    @Query("SELECT * FROM service_listings WHERE id = :id LIMIT 1")
    suspend fun getServiceByIdSync(id: Int): ServiceListing?

    @Query("SELECT * FROM service_listings WHERE isBookmarked = 1 ORDER BY id DESC")
    fun getBookmarkedServices(): Flow<List<ServiceListing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceListing): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(services: List<ServiceListing>)

    @Update
    suspend fun updateService(service: ServiceListing)

    @Delete
    suspend fun deleteService(service: ServiceListing)

    @Query("SELECT COUNT(*) FROM service_listings")
    suspend fun getServiceCount(): Int
}
