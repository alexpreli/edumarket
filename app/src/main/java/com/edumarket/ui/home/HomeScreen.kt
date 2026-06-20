package com.edumarket.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edumarket.ui.components.BookCard
import com.edumarket.ui.components.CourseCard
import com.edumarket.viewmodel.CourseUiModel
import com.edumarket.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: com.edumarket.viewmodel.AuthViewModel,
    homeViewModel: HomeViewModel = viewModel(),
    ordersViewModel: com.edumarket.viewmodel.OrdersViewModel = viewModel()
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    val userEmail by authViewModel.userEmail.collectAsStateWithLifecycle()
    var showSubjectDropdown by remember { mutableStateOf(false) }

    
    if (state.showFreeDialog) {
        FreeCourseDialog(
            availableCourses = state.courses.filter { !it.isSelected && !it.isFree },
            onCourseSelected = { homeViewModel.chooseFreeCourse(it) },
            onDismiss        = { homeViewModel.declineFreeCourse() }
        )
    }

    
    if (state.showOrderDialog) {
        OrderDialog(
            courses     = state.courses.filter { it.isSelected || it.isFree },
            language    = state.language,
            onDismiss   = { homeViewModel.dismissOrderDialog() },
            onClearCart = { homeViewModel.clearCart() },
            onConfirmOrder = { courses -> 
                ordersViewModel.saveOrderFromUiModels(courses, userEmail ?: "")
                homeViewModel.clearCart() 
            }
        )
    }

    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    Column(modifier = Modifier.fillMaxSize()) {
        
        TopAppBar(
            title = {
                Column {
                    Text("📚 ${com.edumarket.ui.theme.AppStrings.appName(lang)}", fontWeight = FontWeight.Bold)
                    Text(
                        text  = com.edumarket.ui.theme.AppStrings.appSubtitle(lang),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor    = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        LazyColumn(
            contentPadding   = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier         = Modifier.fillMaxSize()
        ) {
            
            item {
                PromoBanner(boughtCount = state.boughtCount, language = state.language)
            }

            
            item {
                FilterRow(
                    subjects        = state.allSubjects,
                    selectedSubject = state.selectedSubject,
                    onSubjectSelect = { homeViewModel.selectSubject(it) }
                )
            }

            
            item {
                val cartCount = state.courses.count { it.isSelected || it.isFree }
                Button(
                    onClick  = { homeViewModel.showOrderDialog() },
                    enabled  = cartCount > 0,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (cartCount > 0) "${com.edumarket.ui.theme.AppStrings.checkout(lang)} ($cartCount)" else com.edumarket.ui.theme.AppStrings.checkout(lang),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            
            item {
                Text(
                    text       = "📖 ${com.edumarket.ui.theme.AppStrings.booksTitle(lang)}",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            
            item {
                when {
                    state.booksLoading -> {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.height(40.dp))
                        }
                    }
                    state.books.isEmpty() -> {
                        Text(
                            text  = com.edumarket.ui.theme.AppStrings.booksError(lang),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    else -> {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.books) { book ->
                                BookCard(book = book)
                            }
                        }
                    }
                }
            }

            
            item {
                Text(
                    text       = "🎓 ${com.edumarket.ui.theme.AppStrings.homeTitle(lang)}",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            
            when {
                state.isLoading -> {
                    item {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                state.error != null -> {
                    item {
                        Text(
                            text  = state.error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                state.courses.isEmpty() -> {
                    item {
                        Text(
                            text  = "...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                else -> {
                    items(items = state.courses, key = { it.id }) { course ->
                        CourseCard(
                            course        = course,
                            onBuyClick    = { homeViewModel.addToCart(it) },
                            onRemoveClick = { homeViewModel.removeFromCart(it.id, it.isFree) }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}



@Composable
private fun PromoBanner(boughtCount: Int, language: String) {
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    val remaining = HomeViewModel.COURSES_UNTIL_GIFT - (boughtCount % HomeViewModel.COURSES_UNTIL_GIFT)
    val message = when {
        boughtCount == 0 ->
            if (lang == "ro") "🎁 Cumpără ${HomeViewModel.COURSES_UNTIL_GIFT} cursuri, primești 1 GRATUIT!"
            else "🎁 Buy ${HomeViewModel.COURSES_UNTIL_GIFT} courses, get 1 FREE!"
        boughtCount % HomeViewModel.COURSES_UNTIL_GIFT == 0 ->
            if (lang == "ro") "🎉 Alege acum cursul tău GRATUIT!"
            else "🎉 Choose your FREE course now!"
        else ->
            if (lang == "ro") "🎁 Încă $remaining curs${if (remaining > 1) "uri" else ""} pentru unul GRATUIT!"
            else "🎁 $remaining more course${if (remaining > 1) "s" else ""} for a FREE one!"
    }

    val bgColor = if (boughtCount % HomeViewModel.COURSES_UNTIL_GIFT == 0 && boughtCount > 0)
        Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(14.dp)
    ) {
        Text(
            text       = message,
            color      = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp
        )
    }
}



@Composable
private fun FilterRow(
    subjects: List<String>,
    selectedSubject: String,
    onSubjectSelect: (String) -> Unit
) {
    Row(
        modifier              = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        subjects.forEach { subject ->
            FilterChip(
                selected = subject == selectedSubject,
                onClick  = { onSubjectSelect(subject) },
                label    = { Text(subject) }
            )
        }
    }
}



@Composable
private fun FreeCourseDialog(
    availableCourses: List<CourseUiModel>,
    onCourseSelected: (CourseUiModel) -> Unit,
    onDismiss: () -> Unit
) {
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text(com.edumarket.ui.theme.AppStrings.freeCourseDialogTitle(lang), fontWeight = FontWeight.Bold) },
        text             = {
            Column {
                Text(com.edumarket.ui.theme.AppStrings.freeCourseDialogText(lang))
                Spacer(Modifier.height(12.dp))
                if (availableCourses.isEmpty()) {
                    Text(com.edumarket.ui.theme.AppStrings.noFreeCoursesAvailable(lang))
                } else {
                    availableCourses.take(5).forEach { course ->
                        TextButton(
                            onClick  = { onCourseSelected(course) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(course.name, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        },
        confirmButton    = {},
        dismissButton    = {
            TextButton(onClick = onDismiss) {
                Text(com.edumarket.ui.theme.AppStrings.cancel(lang))
            }
        }
    )
}



@Composable
private fun OrderDialog(
    courses: List<CourseUiModel>,
    language: String,
    onDismiss: () -> Unit,
    onClearCart: () -> Unit,
    onConfirmOrder: (List<CourseUiModel>) -> Unit
) {
    val lang = com.edumarket.ui.theme.LocalAppLanguage.current
    val context = androidx.compose.ui.platform.LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text(com.edumarket.ui.theme.AppStrings.cartOrderTitle(lang), fontWeight = FontWeight.Bold) },
        text             = {
            Column {
                Text(
                    text  = com.edumarket.ui.theme.AppStrings.cartOrderContent(lang),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                courses.forEach { course ->
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text     = course.name,
                            modifier = Modifier.weight(1f),
                            style    = MaterialTheme.typography.bodySmall
                        )
                        if (course.isFree) {
                            Text(
                                text  = com.edumarket.ui.theme.AppStrings.freeCourseBtn(lang).uppercase(),
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text  = "${course.price} RON",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                val totalPrice = courses.filter { !it.isFree }.sumOf { it.price }
                Text(
                    text  = if (lang == "en") "Total: ${courses.count { !it.isFree }} paid + ${courses.count { it.isFree }} free\nTotal Price: $totalPrice RON"
                            else "Total: ${courses.count { !it.isFree }} plătite + ${courses.count { it.isFree }} gratuite\nPreț Total: $totalPrice RON",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(com.edumarket.ui.theme.AppStrings.cancel(lang))
                    }
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                        val paidCourses = courses.filter { !it.isFree }.joinToString("\n") { "- ${it.name} (${it.price} RON)" }
                        val freeCourses = courses.filter { it.isFree }.joinToString("\n") { "- ${it.name} (${com.edumarket.ui.theme.AppStrings.freeCourseBtn(lang)})" }
                        val totalPrice = courses.filter { !it.isFree }.sumOf { it.price }
                        
                        val messageBuilder = StringBuilder()
                        messageBuilder.append(com.edumarket.ui.theme.AppStrings.orderMessagePart1(lang)).append("\n\n")
                        if (paidCourses.isNotEmpty()) messageBuilder.append(paidCourses).append("\n\n")
                        if (freeCourses.isNotEmpty()) messageBuilder.append(freeCourses).append("\n\n")
                        messageBuilder.append("Total: $totalPrice RON\n\n")
                        messageBuilder.append(com.edumarket.ui.theme.AppStrings.orderMessagePart2(lang))
                        
                        com.edumarket.utils.WhatsAppUtils.openWhatsApp(
                            context = context,
                            phoneNumber = "+40736321059",
                            message = messageBuilder.toString(),
                            lang = lang
                        )
                        onConfirmOrder(courses)
                        onDismiss()
                    }) { Text(com.edumarket.ui.theme.AppStrings.confirm(lang)) }
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onClearCart,
                    modifier = Modifier.width(150.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(if (lang == "ro") "Golește coșul" else "Clear Cart")
                }
            }
        },
        dismissButton = {}
    )
}
