package com.edumarket.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edumarket.navigation.BottomNavItem
import com.edumarket.ui.theme.LocalAppLanguage

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick  = { onItemClick(item) },
                icon     = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = item.getLabel(lang)
                    )
                },
                label = { Text(text = item.getLabel(lang)) }
            )
        }
    }
}
