package com.oilsmart.app.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

@Composable
fun HomeScreen(
    viewModel: OilSmartViewModel,
    onNavigateToRecipes: () -> Unit,
    onNavigateToTracker: () -> Unit,
    onNavigateToCampaign: () -> Unit,
    onNavigateToRecipeDetail: (String) -> Unit,
    onNavigateToChatbot: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val metrics by viewModel.healthMetrics.collectAsState()
    val tips by viewModel.campaignTips.collectAsState()
    val recommended = viewModel.getRecommendedRecipes()
    val showLimitAlert by viewModel.showLimitAlert.collectAsState()

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(NeutralBackground)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ── Hero Header ──
            HomeHeader(profile = profile, todayOilMl = viewModel.todayOilMl)

            Spacer(modifier = Modifier.height(16.dp))

            // ── Personalized Instruction ──
            PersonalizedInstructionCard(profile = profile, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            // ── Oil Gauge Card ──
            OilGaugeCard(
                usedMl = viewModel.todayOilMl,
                limitMl = profile.dailyOilLimitMl.toFloat(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Quick Actions ──
            QuickActionsRow(
                onTrackOil = onNavigateToTracker,
                onBrowseRecipes = onNavigateToRecipes,
                onCampaign = onNavigateToCampaign,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Health Metrics ──
            SectionHeader("Health Overview", modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(metrics) { metric ->
                    HealthMetricCard(metric = metric)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Campaign Tip ──
            if (tips.isNotEmpty()) {
                CampaignTipCard(
                    tip = tips.first(),
                    onClick = onNavigateToCampaign,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Recommended Recipes ──
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SectionHeader("Recommended for You")
                TextButton(onClick = onNavigateToRecipes) {
                    Text("See All", color = GreenPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommended) { recipe ->
                    SmallRecipeCard(recipe = recipe, onClick = { onNavigateToRecipeDetail(recipe.id) })
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Policy Nudges ──
            PolicyNudgeSection(
                nudges = viewModel.policyNudges.collectAsState().value,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))
        }

        // ── Chatbot FAB ──
        FloatingActionButton(
            onClick = onNavigateToChatbot,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = GreenPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.AutoAwesome, contentDescription = "AI Chatbot")
        }

        // ── Limit Exceeded Alert ──
        if (showLimitAlert) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissLimitAlert() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Warning, contentDescription = null, tint = HealthDanger)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Limit Exceeded!", color = HealthDanger, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Text("You have exceeded your safe daily oil limit. Consider choosing low-oil or boiled meals for the rest of the day.")
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.dismissLimitAlert() }) {
                        Text("I Understand", color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                    }
                },
                containerColor = NeutralSurface,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
fun HomeHeader(profile: UserProfile, todayOilMl: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(GreenDark, GreenPrimary)
                )
            )
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    // App name branding
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "🫒",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Nutriova",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenLight,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Good Morning! 🌿",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreenLight.copy(alpha = 0.8f)
                    )
                    Text(
                        text = profile.name.split(" ").first(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Tier badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = GoldColor.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = GoldColor, modifier = Modifier.size(14.dp))
                            Text(text = "Gold", color = GoldColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mann Ki Baat Banner
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("🇮🇳", fontSize = 20.sp)
                    Column {
                        Text(
                            text = "Mann Ki Baat Challenge",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Reduce oil by 10% — Join the national mission",
                            color = GreenLight,
                            fontSize = 11.sp
                        )
                    }
                    Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, tint = GreenLight, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun OilGaugeCard(usedMl: Float, limitMl: Float, modifier: Modifier = Modifier) {
    val percentage = (usedMl / limitMl).coerceIn(0f, 1f)
    val animatedPercentage by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "oilGauge"
    )
    val statusColor = when {
        percentage <= 0.7f -> HealthGood
        percentage <= 0.9f -> HealthWarning
        else -> HealthDanger
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Today's Oil Intake", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Surface(shape = RoundedCornerShape(8.dp), color = statusColor.copy(alpha = 0.12f)) {
                    Text(
                        text = if (percentage <= 0.7f) "On Track ✓" else if (percentage <= 1f) "Near Limit" else "Over Limit!",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Arc gauge
                Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.size(100.dp)) {
                        val strokeWidth = 12.dp.toPx()
                        val startAngle = 150f
                        val sweepAngle = 240f
                        // Background arc
                        drawArc(
                            color = NeutralBorder,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        // Filled arc
                        drawArc(
                            color = statusColor,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle * animatedPercentage,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${usedMl.toInt()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(text = "ml", fontSize = 12.sp, color = TextSecondary)
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Used", style = MaterialTheme.typography.bodySmall)
                        Text("${usedMl.toInt()} ml", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = statusColor)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { animatedPercentage },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = statusColor,
                        trackColor = NeutralBorder
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Remaining", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "${(limitMl - usedMl).coerceAtLeast(0f).toInt()} ml",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Limit", style = MaterialTheme.typography.bodySmall)
                        Text("${limitMl.toInt()} ml", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionsRow(
    onTrackOil: () -> Unit,
    onBrowseRecipes: () -> Unit,
    onCampaign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        QuickActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.AddCircle,
            label = "Log Oil",
            color = GreenPrimary,
            onClick = onTrackOil
        )
        QuickActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.MenuBook,
            label = "Recipes",
            color = GreenMedium,
            onClick = onBrowseRecipes
        )
        QuickActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Campaign,
            label = "Campaign",
            color = AmberPrimary,
            onClick = onCampaign
        )
        QuickActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.School,
            label = "Learn",
            color = GreenAccent,
            onClick = {}
        )
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
            }
            Text(label, fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Medium, maxLines = 1)
        }
    }
}

@Composable
fun HealthMetricCard(metric: HealthMetric) {
    val statusColor = when (metric.status) {
        ConsumptionStatus.EXCELLENT -> HealthExcellent
        ConsumptionStatus.GOOD -> HealthGood
        ConsumptionStatus.WARNING -> HealthWarning
        ConsumptionStatus.OVER_LIMIT -> HealthDanger
    }

    Card(
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(statusColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (metric.trend > 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(metric.value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.width(2.dp))
                Text(metric.unit, fontSize = 11.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 3.dp))
            }
            Text(metric.title, fontSize = 12.sp, color = TextSecondary, maxLines = 1)
        }
    }
}

@Composable
fun CampaignTipCard(tip: CampaignTip, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("📢", fontSize = 32.sp)
            Column(modifier = Modifier.weight(1f)) {
                Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(alpha = 0.2f)) {
                    Text(
                        tip.category.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = GreenSurface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    tip.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    tip.description,
                    color = GreenLight,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(Icons.Filled.ArrowForwardIos, contentDescription = null, tint = GreenLight, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SmallRecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(155.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onClick
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                // Oil badge
                Surface(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = GreenPrimary
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text("🫒", fontSize = 10.sp)
                        Text("${recipe.oilAmountMl}ml", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${recipe.calories} kcal", fontSize = 11.sp, color = TextSecondary)
                    Text("•", fontSize = 11.sp, color = TextHint)
                    Text("${recipe.prepTimeMinutes}m", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun PolicyNudgeSection(nudges: List<PolicyNudge>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SectionHeader("Policy Nudges & Benefits")
        Spacer(modifier = Modifier.height(10.dp))
        nudges.forEach { nudge ->
            PolicyNudgeCard(nudge = nudge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PolicyNudgeCard(nudge: PolicyNudge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GreenSurface),
        border = BorderStroke(1.dp, GreenLight.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                when (nudge.type) {
                    NudgeType.GST_BENEFIT -> "💰"
                    NudgeType.REWARD -> "🏆"
                    NudgeType.CERTIFICATION -> "✅"
                    NudgeType.RESTAURANT_LABEL -> "🍽️"
                },
                fontSize = 24.sp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(nudge.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = GreenDark)
                Text(nudge.description, fontSize = 12.sp, color = TextSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = TextPrimary
    )
}

@Composable
fun PersonalizedInstructionCard(profile: UserProfile, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenSurface),
        border = BorderStroke(1.dp, GreenLight.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Info, contentDescription = null, tint = GreenDark, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Your Personalized Limit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = GreenDark)
            }
            Spacer(modifier = Modifier.height(8.dp))
            val baseLimit = (profile.weight * 0.4).toInt()
            var reason = "Base limit for ${profile.weight}kg is ${baseLimit}ml."
            if (profile.hasHeartProblem) reason += " We reduced it by 10ml to protect your heart."
            if (profile.hasDiabetes) reason += " We reduced it by 5ml to manage diabetes."
            
            Text(
                text = "Based on your profile, your daily limit is set to ${profile.dailyOilLimitMl}ml. $reason",
                fontSize = 13.sp,
                color = TextPrimary
            )
        }
    }
}
