package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_listings")
data class ServiceListing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val providerName: String,
    val phoneNumber: String,
    val whatsappNumber: String,
    val views: Int = 100,
    val rating: Double = 5.0,
    val reviewCount: Int = 1,
    val dateAdded: String = "Jul 14, 2026",
    val isBookmarked: Boolean = false,
    val isCustom: Boolean = false
)
