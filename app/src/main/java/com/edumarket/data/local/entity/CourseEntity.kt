package com.edumarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val number: String,
    val date: String,         
    val nameEn: String,
    val nameRo: String,
    val descriptionEn: String,
    val descriptionRo: String,
    val subjectsEn: String,   
    val subjectsRo: String,
    val languageEn: String,   
    val languageRo: String,   
    val backgroundSrc: String 
)
