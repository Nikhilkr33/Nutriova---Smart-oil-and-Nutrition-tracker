package com.oilsmart.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel

@Composable
fun RecipeDetailScreen(
    viewModel: OilSmartViewModel,
    recipeId: String,
    onBack: () -> Unit
) {
    val recipe = viewModel.getRecipeById(recipeId)

    if (recipe == null) {
        EmptyRecipesState(onRetry = onBack)
        return
    }

    var isBookmarked by remember { mutableStateOf(recipe.isBookmarked) }
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(NeutralBackground)) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            // ── Hero Image ──
            Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Top nav overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 44.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                isBookmarked = !isBookmarked
                                viewModel.toggleBookmark(recipe.id)
                            },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = Color.White
                            )
                        }
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                        }
                    }
                }
            }

            // ── Content ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeutralBackground)
                    .padding(20.dp)
            ) {
                // Title + Oil badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(recipe.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (recipe.isLowOil) {
                                Surface(shape = RoundedCornerShape(8.dp), color = GreenSurface, border = BorderStroke(1.dp, GreenLight)) {
                                    Text("Low Oil", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color = GreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = NeutralCard, border = BorderStroke(1.dp, NeutralBorder)) {
                                Text(if (recipe.isVeg) "Veg" else "Non-Veg", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 12.sp, color = if (recipe.isVeg) GreenPrimary else HealthDanger)
                            }
                        }
                    }
                    // Oil indicator circle
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(GreenSurface, RoundedCornerShape(14.dp))
                            .border(2.dp, GreenLight, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${recipe.oilAmountMl}ml", fontWeight = FontWeight.Bold, color = GreenPrimary, fontSize = 14.sp)
                            Text("oil used", fontSize = 9.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Stats Row ──
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NeutralSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RecipeStatItem("${recipe.calories}", "Calories", Icons.Outlined.LocalFireDepartment)
                        VerticalDivider()
                        RecipeStatItem("${recipe.prepTimeMinutes}m", "Prep Time", Icons.Outlined.Schedule)
                        VerticalDivider()
                        RecipeStatItem(recipe.difficulty.label, "Level", Icons.Outlined.BarChart)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Ingredients ──
                Text("Ingredients", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = NeutralSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        recipe.ingredients.forEachIndexed { index, ingredient ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(modifier = Modifier.size(8.dp).background(GreenPrimary, CircleShape))
                                    Text(ingredient.name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                                }
                                Text(
                                    "${ingredient.quantity} ${ingredient.unit}".trim(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (index < recipe.ingredients.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = NeutralDivider)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Cooking Steps ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Cooking Steps", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("${recipe.steps.size} Steps", color = GreenPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))

                recipe.steps.forEach { step ->
                    CookingStepCard(step = step)
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // ── Bottom CTA ──
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            color = NeutralBackground,
            shadowElevation = 12.dp
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.AddCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text(
                        "Add to Today's Log (${recipe.oilAmountMl}ml)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeStatItem(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
fun VerticalDivider() {
    Box(modifier = Modifier.width(1.dp).height(40.dp).background(NeutralBorder))
}

@Composable
fun CookingStepCard(step: CookingStep) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Step number circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(GreenPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("${step.stepNumber}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Text(
                step.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }
        // Tip chip
        step.tip?.let { tip ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.padding(start = 44.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = GreenSurface,
                    border = BorderStroke(1.dp, GreenLight.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("💡", fontSize = 12.sp)
                        Text(tip, fontSize = 12.sp, color = GreenDark)
                    }
                }
            }
        }
    }
}
