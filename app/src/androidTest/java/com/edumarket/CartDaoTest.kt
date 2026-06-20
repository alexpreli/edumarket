package com.edumarket

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edumarket.data.local.AppDatabase
import com.edumarket.data.local.dao.CartDao
import com.edumarket.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CartDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var cartDao: CartDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // In-memory database setup (wiped when testing is complete)
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cartDao = db.cartDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCartItem() = runBlocking {
        val cartItem = CartItemEntity(
            courseId = 1,
            courseName = "Android Development",
            courseNumber = "CS-202",
            isFree = false
        )
        cartDao.insert(cartItem)
        val allItems = cartDao.getAllCartItems().first()
        assertEquals(1, allItems.size)
        assertEquals("Android Development", allItems[0].courseName)
    }

    @Test
    @Throws(Exception::class)
    fun deleteCartItem() = runBlocking {
        val cartItem = CartItemEntity(
            courseId = 1,
            courseName = "Android Development",
            courseNumber = "CS-202",
            isFree = false
        )
        cartDao.insert(cartItem)
        cartDao.deleteById(1)
        val allItems = cartDao.getAllCartItems().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun clearCart() = runBlocking {
        val item1 = CartItemEntity(1, "Course 1", "N-01", false)
        val item2 = CartItemEntity(2, "Course 2", "N-02", true)
        cartDao.insert(item1)
        cartDao.insert(item2)
        assertEquals(2, cartDao.count())

        cartDao.clearCart()
        assertEquals(0, cartDao.count())
    }
}
