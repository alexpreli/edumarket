package com.edumarket.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edumarket.data.local.entity.CartItemEntity
import com.edumarket.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    val context = androidx.compose.ui.platform.LocalContext.current
    var showOrderDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("🛒 ${com.edumarket.ui.theme.AppStrings.cartTitle(lang)}", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor    = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text  = com.edumarket.ui.theme.AppStrings.emptyCart(lang),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding      = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier            = Modifier.weight(1f)
            ) {
                item {
                    Text(
                        text       = if (lang == "ro") "${cartItems.size} cursuri ${com.edumarket.ui.theme.AppStrings.inCart(lang)}" else "${cartItems.size} courses ${com.edumarket.ui.theme.AppStrings.inCart(lang)}",
                        style      = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                items(items = cartItems, key = { it.courseId }) { item ->
                    CartItemRow(
                        item       = item,
                        onRemove   = { cartViewModel.removeItem(item.courseId) }
                    )
                }
            }

            
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick  = { showOrderDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(com.edumarket.ui.theme.AppStrings.checkout(lang), fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick  = { cartViewModel.clearCart() },
                    modifier = Modifier.fillMaxWidth(),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(if (lang == "ro") "Golește coșul" else "Clear Cart", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showOrderDialog) {
        CartOrderDialog(
            items = cartItems,
            onDismiss = { showOrderDialog = false },
            onConfirm = {
                cartViewModel.clearCart()
                showOrderDialog = false
            }
        )
    }
}

@Composable
private fun CartItemRow(
    item: CartItemEntity,
    onRemove: () -> Unit
) {
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = if (item.isFree)
            CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        else
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = item.courseName,
                        style      = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (item.isFree) {
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text       = com.edumarket.ui.theme.AppStrings.freeCourseBtn(lang).uppercase(),
                            style      = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF388E3C)
                        )
                    }
                }
                Text(
                    text  = "ID #${item.courseId}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = com.edumarket.ui.theme.AppStrings.removeCourse(lang),
                    tint               = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CartOrderDialog(
    items: List<CartItemEntity>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text(com.edumarket.ui.theme.AppStrings.cartOrderTitle(lang), fontWeight = FontWeight.Bold) },
        text             = {
            Column {
                Text(
                    text  = com.edumarket.ui.theme.AppStrings.cartOrderContent(lang),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                items.forEach { item ->
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text     = item.courseName,
                            modifier = Modifier.weight(1f),
                            style    = MaterialTheme.typography.bodySmall
                        )
                        if (item.isFree) {
                            Text(
                                text  = com.edumarket.ui.theme.AppStrings.freeCourseBtn(lang).uppercase(),
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = if (lang == "en") "Total: ${items.count { !it.isFree }} paid + ${items.count { it.isFree }} free"
                            else "Total: ${items.count { !it.isFree }} plătite + ${items.count { it.isFree }} gratuite",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val paidCourses = items.filter { !it.isFree }.joinToString("\n") { "- ${it.courseName}" }
                val freeCourses = items.filter { it.isFree }.joinToString("\n") { "- ${it.courseName} (${com.edumarket.ui.theme.AppStrings.freeCourseBtn(lang)})" }
                
                val messageBuilder = java.lang.StringBuilder()
                messageBuilder.append(com.edumarket.ui.theme.AppStrings.orderMessagePart1(lang)).append("\n\n")
                if (paidCourses.isNotEmpty()) messageBuilder.append(paidCourses).append("\n\n")
                if (freeCourses.isNotEmpty()) messageBuilder.append(freeCourses).append("\n\n")
                messageBuilder.append(com.edumarket.ui.theme.AppStrings.orderMessagePart2(lang))
                
                com.edumarket.utils.WhatsAppUtils.openWhatsApp(
                    context = context,
                    phoneNumber = "+40736321059",
                    message = messageBuilder.toString(),
                    lang = lang
                )
                onConfirm()
            }) { Text(com.edumarket.ui.theme.AppStrings.confirm(lang)) }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(com.edumarket.ui.theme.AppStrings.cancel(lang))
            }
        }
    )
}
