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
    homeViewModel: HomeViewModel = viewModel()
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
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
            courses   = state.courses.filter { it.isSelected || it.isFree },
            language  = state.language,
            onDismiss = { homeViewModel.dismissOrderDialog() }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        
        TopAppBar(
            title = {
                Column {
                    Text("📚 EduMarket", fontWeight = FontWeight.Bold)
                    Text(
                        text  = "Digital Learning Marketplace",
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
                        text = if (cartCount > 0) "Place Order ($cartCount courses)" else "Place Order",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            
            item {
                Text(
                    text       = "📖 Featured Programming Books",
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
                            text  = "Could not load books. Check internet connection.",
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
                    text       = "🎓 Available Courses",
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
                            text  = "No courses found for the selected subject.",
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
    val remaining = HomeViewModel.COURSES_UNTIL_GIFT - (boughtCount % HomeViewModel.COURSES_UNTIL_GIFT)
    val message = when {
        boughtCount == 0 ->
            if (language == "en") "🎁 Buy ${HomeViewModel.COURSES_UNTIL_GIFT} courses, get 1 FREE!"
            else "🎁 Cumpara ${HomeViewModel.COURSES_UNTIL_GIFT} cursuri, primesti 1 GRATUIT!"
        boughtCount % HomeViewModel.COURSES_UNTIL_GIFT == 0 ->
            if (language == "en") "🎉 Choose your FREE course now!"
            else "🎉 Alege acum cursul tau GRATUIT!"
        else ->
            if (language == "en") "🎁 $remaining more course${if (remaining > 1) "s" else ""} for a FREE one!"
            else "🎁 Inca $remaining curs${if (remaining > 1) "uri" else ""} pentru unul GRATUIT!"
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
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text("🎉 Congratulations!", fontWeight = FontWeight.Bold) },
        text             = {
            Column {
                Text("You earned a FREE course! Select one from the list:")
                Spacer(Modifier.height(12.dp))
                availableCourses.take(5).forEach { course ->
                    TextButton(
                        onClick  = { onCourseSelected(course) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(course.name, fontWeight = FontWeight.Medium)
                    }
                }
            }
        },
        confirmButton    = {},
        dismissButton    = {
            TextButton(onClick = onDismiss) {
                Text("Decline")
            }
        }
    )
}



@Composable
private fun OrderDialog(
    courses: List<CourseUiModel>,
    language: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text("Order Summary", fontWeight = FontWeight.Bold) },
        text             = {
            Column {
                Text(
                    text  = if (language == "en") "You are ordering:" else "Comanda ta:",
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
                                text  = "FREE",
                                color = Color(0xFF4CAF50),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = if (language == "en") "Total: ${courses.count { !it.isFree }} paid + ${courses.count { it.isFree }} free"
                            else "Total: ${courses.count { !it.isFree }} platite + ${courses.count { it.isFree }} gratuite",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("OK") }
        }
    )
}
