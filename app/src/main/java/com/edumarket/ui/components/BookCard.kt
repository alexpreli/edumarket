package com.edumarket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edumarket.viewmodel.BookUiModel

@Composable
fun BookCard(
    book: BookUiModel,
    modifier: Modifier = Modifier
) {
    
    val gradient = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
    )

    Card(
        modifier  = modifier.width(130.dp),
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            
            Box(
                modifier         = Modifier
                    .width(130.dp)
                    .height(90.dp)
                    .background(brush = gradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text      = "📖",
                    fontSize  = 32.sp
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text       = book.title,
                    style      = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis,
                    color      = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text     = book.author,
                    style    = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color    = Color(0xFF5C6BC0)
                )
            }
        }
    }
}
