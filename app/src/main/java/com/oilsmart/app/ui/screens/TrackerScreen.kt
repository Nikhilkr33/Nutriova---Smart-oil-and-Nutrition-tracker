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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel

@Composable
fun TrackerScreen(viewModel: OilSmartViewModel) {
    val oilLogs by viewModel.oilLogs.collectAsState()
    val profile by viewModel.userProfile.collectAsState()
    val dailySummaries by viewModel.dailySummaries.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val todayTotal = oilLogs.filter { it.date == "2025-04-29" }.sumOf { it.oilAmountMl.toDouble() }.toFloat()
    val todayLogs = oilLogs.filter { it.date == "2025-04-29" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBackground)
    ) {
        // ── Header ──
        TrackerHeader()

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Today's summary
            item {
                TodaySummaryCard(
                    totalMl = todayTotal,
                    limitMl = profile.dailyOilLimitMl.toFloat(),
                    date = "Today, Apr 29"
                )
            }

            // Add Log Button
            item {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("Log Oil Intake", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
            }

            // Weekly overview
            item {
                WeeklyOilChart(summaries = dailySummaries)
            }

            // Today's logs
            item {
                Text("Today's Meals", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            items(todayLogs) { log ->
                OilLogCard(log = log)
            }

            // Health tip
            item {
                HealthTipBanner()
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showAddDialog) {
        AddOilLogDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { log ->
                viewModel.addOilLog(log)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TrackerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(colors = listOf(GreenDark, GreenPrimary))
            )
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Track & Improve,", color = GreenLight, fontSize = 14.sp)
                Text("Oil Consumption", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Outlined.BarChart, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun TodaySummaryCard(totalMl: Float, limitMl: Float, date: String) {
    val percentage = (totalMl / limitMl).coerceIn(0f, 1f)
    val animPct by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "summaryProgress"
    )
    val statusColor = when {
        percentage <= 0.7f -> HealthGood
        percentage <= 1f -> HealthWarning
        else -> HealthDanger
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Surface(shape = RoundedCornerShape(8.dp), color = statusColor.copy(alpha = 0.1f)) {
                    Text(
                        "${(percentage * 100).toInt()}% of limit",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OilStatColumn("${totalMl.toInt()} ml", "Consumed", GreenPrimary)
                OilStatColumn("${(limitMl - totalMl).coerceAtLeast(0f).toInt()} ml", "Remaining", HealthGood)
                OilStatColumn("${limitMl.toInt()} ml", "Daily Limit", TextSecondary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { animPct },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = statusColor,
                trackColor = NeutralBorder
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                when {
                    percentage <= 0.7f -> "🎉 Great job! You're well within your daily oil limit."
                    percentage <= 1f -> "⚠️ You're close to your daily limit. Plan meals carefully."
                    else -> "🚨 You've exceeded your daily oil limit today."
                },
                fontSize = 12.sp,
                color = statusColor
            )
        }
    }
}

@Composable
fun OilStatColumn(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
        Text(label, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
fun WeeklyOilChart(summaries: List<DailyOilSummary>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("7-Day Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            val maxOil = summaries.maxOfOrNull { it.totalOilMl } ?: 33f
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                days.forEachIndexed { index, day ->
                    val summary = summaries.getOrNull(index)
                    val heightFraction = summary?.let { (it.totalOilMl / 40f).coerceIn(0.1f, 1f) } ?: 0.15f
                    val barColor = summary?.let {
                        when (it.status) {
                            ConsumptionStatus.EXCELLENT -> HealthExcellent
                            ConsumptionStatus.GOOD -> GreenPrimary
                            ConsumptionStatus.WARNING -> HealthWarning
                            ConsumptionStatus.OVER_LIMIT -> HealthDanger
                        }
                    } ?: NeutralBorder
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        summary?.let {
                            Text("${it.totalOilMl.toInt()}", fontSize = 9.sp, color = TextSecondary)
                        }
                        val animHeight by animateFloatAsState(
                            targetValue = heightFraction * 80f,
                            animationSpec = tween(800, easing = FastOutSlowInEasing),
                            label = "barHeight"
                        )
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(animHeight.dp)
                                .background(barColor, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        )
                        Text(day, fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Legend
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                LegendItem(HealthGood, "On Track")
                LegendItem(HealthWarning, "Near Limit")
                LegendItem(HealthDanger, "Over Limit")
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Text(label, fontSize = 10.sp, color = TextSecondary)
    }
}

@Composable
fun OilLogCard(log: OilLog) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(GreenSurface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(log.mealType.emoji, fontSize = 20.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(log.mealName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(log.mealType.label, fontSize = 12.sp, color = TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${log.oilAmountMl.toInt()} ml",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GreenPrimary
                )
                Text("oil", fontSize = 11.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun HealthTipBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenSurface),
        border = BorderStroke(1.dp, GreenLight.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("💡", fontSize = 24.sp)
            Column {
                Text("ICMR Health Recommendation", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = GreenDark)
                Text(
                    "The recommended daily oil intake is 12 kg/year per person (≈33ml/day). India currently consumes 60% more than this!",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOilLogDialog(
    onDismiss: () -> Unit,
    onAdd: (OilLog) -> Unit
) {
    var mealName by remember { mutableStateOf("") }
    var oilAmount by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NeutralSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text("Log Oil Intake", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = mealName,
                    onValueChange = { mealName = it },
                    label = { Text("Meal Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = NeutralBorder
                    )
                )
                OutlinedTextField(
                    value = oilAmount,
                    onValueChange = { oilAmount = it },
                    label = { Text("Oil Amount (ml)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = NeutralBorder
                    )
                )
                Text("Meal Type", fontSize = 14.sp, color = TextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MealType.values().forEach { type ->
                        FilterChip(
                            selected = selectedMealType == type,
                            onClick = { selectedMealType = type },
                            label = { Text(type.label, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GreenPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (mealName.isNotEmpty() && oilAmount.isNotEmpty()) {
                        onAdd(
                            OilLog(
                                date = "2025-04-29",
                                mealName = mealName,
                                oilAmountMl = oilAmount.toFloatOrNull() ?: 0f,
                                mealType = selectedMealType
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add Log", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
