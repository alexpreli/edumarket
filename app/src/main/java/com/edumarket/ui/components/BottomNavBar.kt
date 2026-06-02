package com.edumarket.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.edumarket.navigation.BottomNavItem

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        BottomNavItem.entries.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick  = { onItemClick(item) },
                icon     = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = stringResource(item.labelRes)
                    )
                },
                label = { Text(text = stringResource(item.labelRes)) }
            )
        }
    }
}
