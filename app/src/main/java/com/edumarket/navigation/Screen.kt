package com.edumarket.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.edumarket.R

sealed class Screen(val route: String) {

    
    object AuthGraph : Screen("auth_graph")
    object Login    : Screen("login")
    object Register : Screen("register")

    
    object MainGraph : Screen("main_graph")
    object Home      : Screen("home")
    object Cart      : Screen("cart")
    object Profile   : Screen("profile")
}


enum class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    @StringRes val labelRes: Int
) {
    HOME(Screen.Home,    Icons.Filled.Home,         R.string.nav_home),
    CART(Screen.Cart,    Icons.Filled.ShoppingCart,  R.string.nav_cart),
    PROFILE(Screen.Profile, Icons.Filled.Person,    R.string.nav_profile)
}
