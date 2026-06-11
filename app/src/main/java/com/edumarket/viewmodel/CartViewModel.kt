package com.edumarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edumarket.data.local.AppDatabase
import com.edumarket.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val cartDao = AppDatabase.getInstance().cartDao()

    val cartItems: StateFlow<List<CartItemEntity>> = cartDao
        .getAllCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeItem(courseId: Int) {
        viewModelScope.launch {
            cartDao.deleteById(courseId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }
}
