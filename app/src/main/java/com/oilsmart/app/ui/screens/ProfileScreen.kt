package com.oilsmart.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.data.models.*
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel

@Composable
fun ProfileScreen(
    viewModel: OilSmartViewModel,
    onSignOut: () -> Unit = {}
) {
    val profile by viewModel.userProfile.collectAsState()
    val scrollState = rememberScrollState()
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBackground)
            .verticalScroll(scrollState)
    ) {
        // ── Green Header ──
        ProfileHeader(profile = profile, onEditClick = { showEditDialog = true })

        // ── Stats Row ──
        ProfileStatsRow(profile = profile, modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))

        // ── Daily Oil Limit Card ──
        OilLimitCard(profile = profile, modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        // ── Account Settings ──
        SettingsSection(
            title = "Account Settings",
            items = listOf(
                SettingsItem(Icons.Outlined.Person, "Personal Information", "Diet, Region, and Bio"),
                SettingsItem(Icons.Outlined.Notifications, "Notifications", "Daily reminders & alerts"),
                SettingsItem(Icons.Outlined.Security, "Privacy & Security", "Manage your data")
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Health Preferences ──
        SettingsSection(
            title = "Health Preferences",
            items = listOf(
                SettingsItem(Icons.Outlined.Restaurant, "Dietary Preference", profile.dietaryPreference.label),
                SettingsItem(Icons.Outlined.EmojiEvents, "My Achievements", "${profile.badgesEarned} Badges earned")
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ── Sign Out ──
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NeutralSurface),
            elevation = CardDefaults.cardElevation(1.dp),
            onClick = { showSignOutDialog = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color(0xFFFFEDED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Sign Out",
                    color = Color(0xFFD32F2F),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    // ── Sign Out Confirmation Dialog ──
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            shape = RoundedCornerShape(24.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFFFEDED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Logout,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(26.dp)
                    )
                }
            },
            title = {
                Text(
                    "Sign Out?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    "Are you sure you want to sign out of Nutriova? Your progress will be saved.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutDialog = false
                        onSignOut()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    )
                ) {
                    Text("Yes, Sign Out", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showSignOutDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NeutralBorder)
                ) {
                    Text("Cancel", color = TextPrimary)
                }
            }
        )
    }

    // ── Edit Profile Dialog ──
    if (showEditDialog) {
        EditProfileDialog(
            profile = profile,
            onDismiss = { showEditDialog = false },
            onSave = { name, location, age, weight, height ->
                viewModel.updateProfileDetails(name, location, age, weight, height)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun ProfileHeader(profile: UserProfile, onEditClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(colors = listOf(GreenDark, GreenPrimary))
            )
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(24.dp))
            Text("Profile", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile image circle
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(Color.White, CircleShape)
                    .padding(3.dp)
                    .background(GreenSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("AS", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(profile.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = GreenLight, modifier = Modifier.size(14.dp))
                Text(profile.location, color = GreenLight, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun ProfileStatsRow(profile: UserProfile, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileStatCard(
            value = "${profile.weight}",
            unit = "kg",
            label = "Weight",
            modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
            value = "${profile.height}",
            unit = "cm",
            label = "Height",
            modifier = Modifier.weight(1f)
        )
        ProfileStatCard(
            value = "${profile.age}",
            unit = "yrs",
            label = "Age",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ProfileStatCard(value: String, unit: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(label, fontSize = 12.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextPrimary)
            }
            Text(unit, fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun OilLimitCard(profile: UserProfile, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = NeutralSurface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(GreenSurface, CircleShape)
                    .border(2.dp, GreenLight.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.WaterDrop, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Daily Oil Limit", fontSize = 12.sp, color = TextSecondary)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("${profile.dailyOilLimitMl}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextPrimary)
                    Text(" ml / day", fontSize = 14.sp, color = TextSecondary, modifier = Modifier.padding(bottom = 2.dp))
                }
            }
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, NeutralBorder)
            ) {
                Text("Adjust", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 13.sp)
            }
        }
    }
}

data class SettingsItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(title, fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = NeutralSurface),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsRow(item = item)
                    if (index < items.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(start = 64.dp), color = NeutralDivider)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(item: SettingsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(GreenSurface.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextPrimary)
            Text(item.subtitle, fontSize = 12.sp, color = TextSecondary)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextHint, modifier = Modifier.size(20.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (String, String, Int, Int, Int) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var location by remember { mutableStateOf(profile.location) }
    var age by remember { mutableStateOf(profile.age.toString()) }
    var weight by remember { mutableStateOf(profile.weight.toString()) }
    var height by remember { mutableStateOf(profile.height.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (kg)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        name,
                        location,
                        age.toIntOrNull() ?: profile.age,
                        weight.toIntOrNull() ?: profile.weight,
                        height.toIntOrNull() ?: profile.height
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextPrimary)
            }
        }
    )
}
