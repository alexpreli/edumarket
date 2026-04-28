package com.edumarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val courseId: Int,
    val courseName: String,
    val courseNumber: String,
    val isFree: Boolean = false
)
