package com.edumarket.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.edumarket.ui.auth.LoginScreen
import com.edumarket.ui.auth.RegisterScreen
import com.edumarket.ui.cart.CartScreen
import com.edumarket.ui.components.BottomNavBar
import com.edumarket.ui.home.HomeScreen
import com.edumarket.ui.profile.ProfileScreen
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import com.edumarket.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    
    if (isLoggedIn == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination =
        if (isLoggedIn == true) Screen.MainGraph.route else Screen.AuthGraph.route

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route, Screen.Cart.route, Screen.Profile.route
    )

    val context = androidx.compose.ui.platform.LocalContext.current
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemClick  = { item ->
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                androidx.compose.material3.FloatingActionButton(
                    onClick = {
                        com.edumarket.utils.WhatsAppUtils.openWhatsApp(
                            context = context,
                            phoneNumber = "+40736321059",
                            message = com.edumarket.ui.theme.AppStrings.supportMessage(lang),
                            lang = lang
                        )
                    },
                    containerColor = androidx.compose.ui.graphics.Color(0xFF25D366),
                    contentColor = androidx.compose.ui.graphics.Color.White
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.edumarket.R.drawable.support_icon),
                        contentDescription = "WhatsApp Support",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = startDestination,
            modifier         = Modifier.padding(innerPadding)
        ) {
            
            navigation(
                startDestination = Screen.Login.route,
                route            = Screen.AuthGraph.route
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onLoginSuccess = {
                            navController.navigate(Screen.MainGraph.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onRegisterSuccess = {
                            navController.navigate(Screen.MainGraph.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        },
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            
            navigation(
                startDestination = Screen.Home.route,
                route            = Screen.MainGraph.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(authViewModel = authViewModel)
                }
                composable(Screen.Cart.route) {
                    CartScreen(authViewModel = authViewModel)
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        authViewModel = authViewModel,
                        onLogout = {
                            navController.navigate(Screen.AuthGraph.route) {
                                popUpTo(Screen.MainGraph.route) { inclusive = true }
                            }
                        },
                        onNavigateToOrders = {
                            navController.navigate(Screen.Orders.route)
                        }
                    )
                }
                composable(Screen.Orders.route) {
                    com.edumarket.ui.orders.OrdersScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
