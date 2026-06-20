package com.edumarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edumarket.data.local.AppDatabase
import com.edumarket.data.local.entity.CartItemEntity
import com.edumarket.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersViewModel : ViewModel() {

    private val orderDao = AppDatabase.getInstance().orderDao()

    val pastOrders: StateFlow<List<OrderEntity>> = orderDao
        .getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveOrder(items: List<CartItemEntity>, userEmail: String) {
        if (items.isEmpty()) return
        
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val summaryBuilder = java.lang.StringBuilder()
            var total = 0
            items.forEach { item ->
                if (item.isFree) {
                    summaryBuilder.append("- ${item.courseName} (Gratuit)\n")
                } else {
                    summaryBuilder.append("- ${item.courseName} (${item.price} RON)\n")
                    total += item.price
                }
            }

            val newOrder = OrderEntity(
                date = currentDate,
                coursesSummary = summaryBuilder.toString().trim(),
                totalPrice = total,
                userEmail = userEmail
            )
            
            orderDao.insert(newOrder)
        }
    }

    fun saveOrderFromUiModels(courses: List<com.edumarket.viewmodel.CourseUiModel>, userEmail: String) {
        if (courses.isEmpty()) return
        viewModelScope.launch {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val summaryBuilder = java.lang.StringBuilder()
            var total = 0
            courses.forEach { course ->
                if (course.isFree) {
                    summaryBuilder.append("- ${course.name} (Gratuit)\n")
                } else {
                    summaryBuilder.append("- ${course.name} (${course.price} RON)\n")
                    total += course.price
                }
            }

            val newOrder = OrderEntity(
                date = currentDate,
                coursesSummary = summaryBuilder.toString().trim(),
                totalPrice = total,
                userEmail = userEmail
            )
            
            orderDao.insert(newOrder)
        }
    }
}
