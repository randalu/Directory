package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceId: Int,
    val reviewerName: String,
    val rating: Double,
    val comment: String,
    val dateAdded: String
)
