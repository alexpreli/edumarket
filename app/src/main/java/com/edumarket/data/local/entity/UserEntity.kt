package com.edumarket.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String
) {
    init {
        val namePattern = "^[\\p{L} ]{3,}\$"
        require(name.matches(namePattern.toRegex())) {
            "UserEntity validation failed: Full Name must be at least 3 characters and contain only letters and spaces."
        }
    }
}
