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
    object Orders    : Screen("orders")
}


enum class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val getLabel: (String) -> String
) {
    HOME(Screen.Home,    Icons.Filled.Home,         { lang -> com.edumarket.ui.theme.AppStrings.navHome(lang) }),
    CART(Screen.Cart,    Icons.Filled.ShoppingCart, { lang -> com.edumarket.ui.theme.AppStrings.navCart(lang) }),
    PROFILE(Screen.Profile, Icons.Filled.Person,    { lang -> com.edumarket.ui.theme.AppStrings.navProfile(lang) })
}
