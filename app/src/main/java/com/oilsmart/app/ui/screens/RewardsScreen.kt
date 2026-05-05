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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel
import java.time.LocalDate

@Composable
fun RewardsScreen(viewModel: OilSmartViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val weeklyStreak by viewModel.weeklyStreak.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBackground)
            .verticalScroll(scrollState)
    ) {
        // ── Header ──
        RewardsHeader(profile = profile)

        Spacer(modifier = Modifier.height(16.dp))

        // ── Weekly Streak ──
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Weekly Streak", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Next: 50 pts", color = GreenPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            WeeklyStreakCard(streak = weeklyStreak)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Available Rewards ──
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Available Rewards", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            achievements.forEach { achievement ->
                AchievementCard(achievement = achievement)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Redeem Points ──
        RedeemPointsCard(modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun RewardsHeader(profile: UserProfile) {
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
                    Text("Your Progress", color = GreenLight, fontSize = 13.sp)
                    Text("OilSmart Rewards", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
                // Tier badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GoldColor.copy(alpha = 0.25f),
                    border = BorderStroke(1.dp, GoldColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = GoldColor, modifier = Modifier.size(16.dp))
                        Text(profile.tier.label, color = GoldColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Points + Streak row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total Points
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenPrimary.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Points", color = GreenLight, fontSize = 12.sp)
                        Text(
                            "${profile.totalPoints}",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Current Streak
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AmberPrimary)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Current Streak", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Text("${profile.currentStreak} Days", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyStreakCard(streak: WeeklyStreak) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                streak.days.forEach { day ->
                    StreakDayItem(day = day)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GreenSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
                Text(
                    "Log your oil usage 3 more days to hit your 10-day milestone",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun StreakDayItem(day: StreakDay) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(day.dayLabel, fontSize = 11.sp, color = TextSecondary)
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = when {
                        day.isCompleted -> GreenPrimary
                        day.isToday -> NeutralSurface
                        else -> NeutralCard
                    },
                    shape = CircleShape
                )
                .border(
                    width = if (day.isToday) 2.dp else 0.dp,
                    color = if (day.isToday) GreenPrimary else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (day.isCompleted) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            } else {
                Text("${day.dayNumber}", fontSize = 13.sp, color = if (day.isToday) GreenPrimary else TextSecondary, fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        border = if (achievement.isUnlocked) BorderStroke(1.dp, GreenLight.copy(alpha = 0.5f)) else null
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (achievement.isUnlocked) GreenSurface else NeutralCard,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(achievement.iconEmoji, fontSize = 22.sp)
            }

            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(achievement.title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                Text(achievement.description, fontSize = 12.sp, color = TextSecondary)
                if (!achievement.isUnlocked && achievement.progress > 0f) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { achievement.progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                        color = GreenPrimary,
                        trackColor = NeutralBorder
                    )
                }
            }

            // Points + Status
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "+${achievement.points}",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (achievement.isUnlocked) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "Earned", tint = GreenPrimary, modifier = Modifier.size(20.dp))
                } else {
                    Icon(Icons.Outlined.Lock, contentDescription = "Locked", tint = TextHint, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun RedeemPointsCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        border = BorderStroke(1.dp, NeutralBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AmberLight.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎁", fontSize = 22.sp)
                }
                Column {
                    Text("Redeem Points", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    Text("Use points for health consultations", fontSize = 12.sp, color = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GreenPrimary)
            ) {
                Text("Browse Rewards Store", color = GreenPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
