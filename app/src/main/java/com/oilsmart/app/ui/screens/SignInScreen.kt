package com.oilsmart.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oilsmart.app.ui.theme.*
import com.oilsmart.app.viewmodel.OilSmartViewModel
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(viewModel: OilSmartViewModel, onSignInSuccess: (Boolean) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isSignUpMode by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Floating circle animation
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeutralBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Hero Section ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GreenDark, GreenPrimary, GreenMedium)
                        )
                    )
            ) {
                // Decorative background circles
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .offset(x = (-40).dp, y = (-40).dp + floatOffset.dp)
                        .alpha(0.15f)
                        .background(Color.White, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 30.dp, y = 20.dp + (-floatOffset).dp)
                        .alpha(0.10f)
                        .background(Color.White, CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = 20.dp, y = 20.dp)
                        .alpha(0.12f)
                        .background(GoldColor, CircleShape)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo circle
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🫒", fontSize = 36.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Nutriova",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your smart nutrition companion 🌿",
                        fontSize = 14.sp,
                        color = GreenLight,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Card Form ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-28).dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = NeutralSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = if (isSignUpMode) "Create Account 🌿" else "Welcome Back! 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isSignUpMode) "Sign up to start your healthy journey" else "Sign in to continue your healthy journey",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = ""
                        },
                        label = { Text("Email Address") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = null,
                                tint = if (emailError.isNotEmpty()) MaterialTheme.colorScheme.error else GreenPrimary
                            )
                        },
                        isError = emailError.isNotEmpty(),
                        supportingText = if (emailError.isNotEmpty()) {
                            { Text(emailError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = ""
                        },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = if (passwordError.isNotEmpty()) MaterialTheme.colorScheme.error else GreenPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        supportingText = if (passwordError.isNotEmpty()) {
                            { Text(passwordError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                authError = ""
                                handleAuth(email, password, isSignUpMode, viewModel, coroutineScope, { emailError = it }, { passwordError = it }, { authError = it }, { isLoading = it }, onSignInSuccess)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            focusedLabelColor = GreenPrimary,
                            cursorColor = GreenPrimary
                        ),
                        singleLine = true
                    )

                    if (!isSignUpMode) {
                        // Forgot Password
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {}) {
                                Text(
                                    "Forgot Password?",
                                    color = GreenPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (authError.isNotEmpty()) {
                        Text(
                            text = authError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Sign In / Up Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            authError = ""
                            handleAuth(email, password, isSignUpMode, viewModel, coroutineScope, { emailError = it }, { passwordError = it }, { authError = it }, { isLoading = it }, onSignInSuccess)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Icon(
                                if (isSignUpMode) Icons.Filled.PersonAdd else Icons.Filled.Login,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (isSignUpMode) "Create Account" else "Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Google Sign In removed per request
                }
            }

            // Sign Up prompt
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isSignUpMode) "Already have an account? " else "New to Nutriova? ",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                TextButton(onClick = { isSignUpMode = !isSignUpMode }) {
                    Text(
                        if (isSignUpMode) "Sign In" else "Create Account",
                        fontSize = 14.sp,
                        color = GreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun handleAuth(
    email: String,
    password: String,
    isSignUp: Boolean,
    viewModel: OilSmartViewModel,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onEmailError: (String) -> Unit,
    onPasswordError: (String) -> Unit,
    onAuthError: (String) -> Unit,
    setLoading: (Boolean) -> Unit,
    onSuccess: (Boolean) -> Unit
) {
    var valid = true
    if (email.isBlank()) {
        onEmailError("Email is required")
        valid = false
    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onEmailError("Enter a valid email address")
        valid = false
    }
    if (password.isBlank()) {
        onPasswordError("Password is required")
        valid = false
    } else if (password.length < 6) {
        onPasswordError("Password must be at least 6 characters")
        valid = false
    }
    if (valid) {
        setLoading(true)
        coroutineScope.launch {
            val result = if (isSignUp) viewModel.signUp(email, password) else viewModel.signIn(email, password)
            setLoading(false)
            result.onSuccess {
                onSuccess(isSignUp)
            }.onFailure {
                onAuthError(it.message ?: "Authentication failed")
            }
        }
    }
}
