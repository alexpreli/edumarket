package com.edumarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val coursesSummary: String,
    val status: String = "Sent to WhatsApp",
    val totalPrice: Int = 0
)
