package com.edumarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.edumarket.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE courseId = :courseId")
    suspend fun deleteById(courseId: Int)

    @Query("SELECT * FROM cart_items ORDER BY courseId ASC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM cart_items WHERE isFree = 0")
    suspend fun getPaidCount(): Int

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
