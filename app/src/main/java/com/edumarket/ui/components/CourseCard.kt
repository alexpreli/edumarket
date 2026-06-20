package com.edumarket.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edumarket.R
import com.edumarket.viewmodel.CourseUiModel

@Composable
fun CourseCard(
    course: CourseUiModel,
    onBuyClick: (CourseUiModel) -> Unit,
    onRemoveClick: (CourseUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            
            Box {
                Image(
                    painter            = painterResource(
                        id = courseImageRes(course.backgroundSrc)
                    ),
                    contentDescription = course.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                
                if (course.isFree) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                color  = Color(0xFF4CAF50),
                                shape  = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text  = com.edumarket.ui.theme.AppStrings.freeCourseBtn(com.edumarket.ui.theme.LocalAppLanguage.current),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 12.sp
                        )
                    }
                }
            }

            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text       = course.name,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text  = course.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = "• ${course.language}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }

                Spacer(Modifier.height(4.dp))
                val lang = com.edumarket.ui.theme.LocalAppLanguage.current
                Text(
                    text = com.edumarket.ui.theme.AppStrings.courseDuration(lang, course.duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = com.edumarket.ui.theme.AppStrings.courseTrainingCentre(lang, course.trainingCentre),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = com.edumarket.ui.theme.AppStrings.courseTeacher(lang, course.teacherName),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text     = course.description,
                    style    = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                
                if (course.subjects.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        course.subjects.take(4).forEach { subject ->
                            SuggestionChip(
                                onClick = {},
                                label   = { Text(subject, fontSize = 10.sp) },
                                colors  = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    }
                }

                
                Spacer(Modifier.height(10.dp))

                if (course.isSelected || course.isFree) {
                    OutlinedButton(
                        onClick  = { onRemoveClick(course) },
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(com.edumarket.ui.theme.AppStrings.removeCourse(com.edumarket.ui.theme.LocalAppLanguage.current))
                    }
                } else {
                    Button(
                        onClick  = { onBuyClick(course) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(com.edumarket.ui.theme.AppStrings.addToCart(com.edumarket.ui.theme.LocalAppLanguage.current))
                    }
                }
            }
        }
    }
}


fun courseImageRes(backgroundSrc: String): Int = when (backgroundSrc) {
    "csharp"     -> R.drawable.csharp
    "cpp"        -> R.drawable.cpp
    "java"       -> R.drawable.java_lang
    "javascript" -> R.drawable.javascript
    "spring"     -> R.drawable.spring
    "html"       -> R.drawable.html
    "css"        -> R.drawable.css
    "php"        -> R.drawable.php
    "python"     -> R.drawable.python
    "rcode"      -> R.drawable.rcode
    else         -> R.drawable.defaultpicture
}
