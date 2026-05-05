package com.oilsmart.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(onBack: () -> Unit) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    "Hello! I am your AI Nutrition Assistant 🌿. I can help you find ways to reduce oil in your cooking or manage your health conditions. How can I help today?",
                    isUser = false
                )
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val hints = listOf(
        "How to reduce oil in curries?",
        "Best oil for heart problems?",
        "Oil limit for diabetes?",
        "Zero-oil snacks recipe?"
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        messages = messages + ChatMessage(text, isUser = true)
        inputText = ""
        isTyping = true
        
        coroutineScope.launch {
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }

        // Mock AI Response Logic
        coroutineScope.launch {
            delay(1200) // Simulate network delay
            val responseText = when {
                text.contains("curries", ignoreCase = true) -> 
                    "To reduce oil in Indian curries, try dry-roasting your spices first! You can also use a non-stick pan and substitute oil with a splash of water, vegetable broth, or tomato puree to prevent sticking."
                text.contains("heart", ignoreCase = true) -> 
                    "For heart health, olive oil and mustard oil are better choices in moderation because they have higher unsaturated fats. Remember, your personalized limit already reduces your daily intake by 10ml to protect your heart!"
                text.contains("diabetes", ignoreCase = true) -> 
                    "Managing diabetes means choosing healthy fats and strictly adhering to your limit. Avoiding deep-fried foods prevents insulin spikes. Your profile limit includes a 5ml reduction specifically for diabetes management."
                text.contains("zero-oil", ignoreCase = true) -> 
                    "Try baked chickpea chaat, steamed dhokla, or roasted makhana (fox nuts). These are completely oil-free, highly nutritious, and delicious!"
                else -> 
                    "That's a great question! While I am currently a mock AI in this prototype, in the future I'll connect to a live AI engine to give you exact recipes and scientific nutritional advice."
            }
            messages = messages + ChatMessage(responseText, isUser = false)
            isTyping = false
            delay(100)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = GoldColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Nutrition Assistant", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = NeutralBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Chat List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                }
                if (isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Hint Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeutralSurface)
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(hints) { hint ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = GreenSurface,
                        border = BorderStroke(1.dp, GreenPrimary),
                        onClick = { sendMessage(hint) }
                    ) {
                        Text(
                            text = hint,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = GreenDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeutralSurface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask for health tips...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = NeutralBorder
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { sendMessage(inputText) })
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = { sendMessage(inputText) },
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(GoldColor, CircleShape)
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isUser) GreenPrimary else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 16.dp
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.text,
                color = if (isUser) Color.White else TextPrimary,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(GoldColor, CircleShape)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text("Typing...", color = TextSecondary, fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}
