package com.oilsmart.app.ui.screens

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel

@Composable
fun CampaignScreen(viewModel: OilSmartViewModel) {
    val tips by viewModel.campaignTips.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBackground)
            .verticalScroll(scrollState)
    ) {
        // ── Header ──
        CampaignHeader()

        // ── Stats Ribbon ──
        NationalStatsRibbon(modifier = Modifier.padding(16.dp))

        // ── PM Mann Ki Baat Banner ──
        MannKiBaatBanner(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(20.dp))

        // ── Tips ──
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Awareness & Tips", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            tips.forEach { tip ->
                TipCard(tip = tip)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Initiatives ──
        InitiativesSection(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun CampaignHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(colors = listOf(GreenDark, GreenPrimary))
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
                    Text("National Mission,", color = GreenLight, fontSize = 14.sp)
                    Text("Healthy India 🇮🇳", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                Icon(Icons.Outlined.Campaign, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Reducing edible oil consumption for a healthier, economically stronger India.",
                color = GreenLight,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun NationalStatsRibbon(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("National Oil Consumption Facts", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatFact("19.3 kg", "Per capita/year", HealthDanger)
                StatFact("12 kg", "ICMR Recommended", HealthGood)
                StatFact("56%", "Imported", HealthWarning)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.6f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = HealthDanger,
                trackColor = GreenSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Current consumption is ~60% above the safe limit",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun StatFact(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color)
        Text(label, fontSize = 10.sp, color = TextSecondary, maxLines = 2)
    }
}

@Composable
fun MannKiBaatBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GreenPrimary)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🎙️", fontSize = 28.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Surface(shape = RoundedCornerShape(6.dp), color = Color.White.copy(alpha = 0.2f)) {
                    Text(
                        "Mann Ki Baat Ep. 119",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = GreenSurface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "PM Modi's Call: 10% Oil Reduction",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Feb 2025 — A national mission for health, economy, and Atmanirbhar Bharat",
                    color = GreenLight,
                    fontSize = 12.sp
                )
            }
        }
        // Challenge button
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Accept the 10% Challenge", color = GreenPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TipCard(tip: CampaignTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        when (tip.category) {
                            TipCategory.POLICY -> AmberLight.copy(alpha = 0.15f)
                            TipCategory.HEALTH -> HealthDanger.copy(alpha = 0.1f)
                            TipCategory.COOKING -> GreenSurface
                            TipCategory.NUTRITION -> GreenLight.copy(alpha = 0.2f)
                            TipCategory.REGIONAL -> AmberPrimary.copy(alpha = 0.15f)
                        },
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    when (tip.category) {
                        TipCategory.POLICY -> "📜"
                        TipCategory.HEALTH -> "❤️"
                        TipCategory.COOKING -> "👨‍🍳"
                        TipCategory.NUTRITION -> "🥗"
                        TipCategory.REGIONAL -> "🍛"
                    },
                    fontSize = 22.sp
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GreenSurface
                ) {
                    Text(
                        tip.category.label,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = GreenPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(tip.title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(tip.description, fontSize = 12.sp, color = TextSecondary, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
fun InitiativesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Key Initiatives", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        val initiatives = listOf(
            Triple("🏫", "School Programs", "E-learning modules on balanced diet for students in Mid-Day Meal scheme schools"),
            Triple("🏥", "Hospital Canteens", "Low-oil certified menus in 500+ hospitals and institutional kitchens"),
            Triple("📱", "Restaurant APIs", "Digital oil-content labels on partnered food delivery apps"),
            Triple("⛓️", "Blockchain Verification", "Transparent certification for restaurants and food manufacturers"),
            Triple("📊", "Policy Dashboard", "Real-time data for policymakers at district and state levels")
        )
        initiatives.forEach { (emoji, title, desc) ->
            InitiativeCard(emoji = emoji, title = title, description = desc)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun InitiativeCard(emoji: String, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        border = BorderStroke(1.dp, NeutralBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(emoji, fontSize = 24.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(description, fontSize = 12.sp, color = TextSecondary, maxLines = 2)
            }
        }
    }
}
