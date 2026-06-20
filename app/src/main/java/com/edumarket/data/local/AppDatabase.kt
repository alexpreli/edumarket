package com.edumarket.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edumarket.EduMarketApp
import com.edumarket.data.local.dao.CartDao
import com.edumarket.data.local.dao.CourseDao
import com.edumarket.data.local.dao.UserDao
import com.edumarket.data.local.dao.OrderDao
import com.edumarket.data.local.entity.CartItemEntity
import com.edumarket.data.local.entity.CourseEntity
import com.edumarket.data.local.entity.OrderEntity
import com.edumarket.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CourseEntity::class, CartItemEntity::class, OrderEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    EduMarketApp.appContext,
                    AppDatabase::class.java,
                    "edumarket.db"
                ).fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}
