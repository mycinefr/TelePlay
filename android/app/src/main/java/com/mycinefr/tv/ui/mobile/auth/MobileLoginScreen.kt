package com.mycinefr.tv.ui.mobile.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.mycinefr.tv.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mycinefr.tv.ui.auth.LoginViewModel
import com.mycinefr.tv.ui.theme.*

@Composable
fun MobileLoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    // Redirection immédiate dès que l'utilisateur est connecté (via Bot ou via Code)
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            android.util.Log.d("MobileLogin", "Connexion réussie, redirection...")
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MobileBackground,
                        Color(0xFF101420),
                        MobileBackground
                    )
                )
            )
    ) {
        // Settings Button (Top Right) — zIndex ensures it stays above the scroll layer
        IconButton(
            onClick = { viewModel.toggleServerConfig() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(1f)
                .padding(16.dp)
                .statusBarsPadding()
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Paramètres du serveur",
                tint = if (uiState.showServerConfig) MobilePrimary else Color.White.copy(alpha = 0.6f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MobilePrimary.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MobilePrimary.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "MyCinéFR TV",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Service sécurisé de streaming média",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Server Configuration (Collapsible)
            AnimatedVisibility(
                visible = uiState.showServerConfig,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                GlassmorphismSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Paramètres du serveur",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.serverUrl,
                            onValueChange = { viewModel.updateServerUrl(it) },
                            label = { Text("URL du serveur", color = Color.White.copy(alpha = 0.6f)) },
                            placeholder = { Text("http://192.168.1.x:8000") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = MobilePrimary,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Bot info is now fetched automatically
                        if (uiState.botUsername.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Connecté à @${uiState.botUsername}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MobilePrimary.copy(alpha = 0.8f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = { viewModel.saveAndRestart() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MobilePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sauvegarder & redémarrer", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(color = MobilePrimary)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Authentification au cours...", color = Color.White.copy(alpha = 0.7f))
            } else if (uiState.loginCode != null) {
                // On sépare le mode normal (origine) et le mode testeur Google
                var showGoogleInput by remember { mutableStateOf(false) }
                var manualCode by remember { mutableStateOf("") }

                if (!showGoogleInput) {
                    // 1. LE SYSTÈME ORIGINEL (Restauration complète)
                    Text(
                        text = "Confirmer dans Telegram",
                         style = MaterialTheme.typography.titleLarge,
                         color = Color.White,
                         fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GlassmorphismSurface(
                        shape = RoundedCornerShape(24.dp),
                                         borderColor = MobilePrimary.copy(alpha = 0.3f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 48.dp, vertical = 24.dp),
                               horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.loginCode!!,
                                 style = MaterialTheme.typography.displayLarge.copy(
                                     fontSize = 42.sp,
                                     letterSpacing = 4.sp,
                                     fontWeight = FontWeight.Black
                                 ),
                                 color = MobilePrimary,
                                 maxLines = 1,
                                 softWrap = false
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Expire dans  minutes",
                                 style = MaterialTheme.typography.labelMedium,
                                 color = Color.White.copy(alpha = 0.4f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Bouton d'origine pour ouvrir Telegram
                    // Bouton optimisé anti-crash mémoire
                    Button(
                        onClick = {
                            val bot = uiState.botUsername.ifBlank { "mycfrsurfTGBot" }
                            try {
                                // 1. Ouvre l'appli Telegram directement (évite le navigateur web)
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("tg://resolve?domain=$bot&start=${uiState.loginCode}")
                                ).apply {
                                    // Protège notre application en forçant l'ouverture dans une nouvelle tâche
                                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // 2. Fallback sur le navigateur uniquement si l'appli Telegram n'est pas installée
                                try {
                                    val fallbackIntent = android.content.Intent(
                                        android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse("https://t.me/$bot?start=${uiState.loginCode}")
                                    ).apply {
                                        addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(fallbackIntent)
                                } catch (e2: Exception) {
                                    android.widget.Toast.makeText(context, "Impossible d'ouvrir Telegram", android.widget.Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                           colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                           shape = RoundedCornerShape(16.dp),
                           border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                             contentDescription = null,
                             tint = Color.White,
                             modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Ouvrir @${uiState.botUsername.ifBlank { "mycfrsurfTGBot" }}", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        if (uiState.isPolling) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                                          strokeWidth = 2.dp,
                                                          color = MobileSecondary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "En attente de la confirmation...",
                                     style = MaterialTheme.typography.bodyMedium,
                                     color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                    } else {
                        // 2. LE MODE CONTOURNEMENT GOOGLE (Isolé pour ne pas gêner l'origine)
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = manualCode,
                            onValueChange = { manualCode = it },
                            label = { Text("Code de test Google Review", color = Color.White.copy(alpha = 0.6f)) },
                                          placeholder = { Text("Ex: GOOGLE") },
                                          singleLine = true,
                                          colors = OutlinedTextFieldDefaults.colors(
                                              focusedTextColor = Color.White,
                                              unfocusedTextColor = Color.White,
                                              focusedBorderColor = MobilePrimary,
                                              unfocusedBorderColor = Color.White.copy(alpha = 0.2f)
                                          ),
                                          shape = RoundedCornerShape(12.dp),
                                          modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.verifyManualCode(manualCode) },
                               modifier = Modifier.fillMaxWidth().height(50.dp),
                               colors = ButtonDefaults.buttonColors(containerColor = MobilePrimary),
                               shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Se connecter (Mode Test)", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Menu d'actions tout en bas
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TextButton(onClick = { showGoogleInput = !showGoogleInput }) {
                            Text(
                                text = if (showGoogleInput) "Retour à l'authentification Telegram" else "Options de test (Google Review)",
                                 color = Color.White.copy(alpha = 0.3f),
                                 fontSize = 12.sp
                            )
                        }
                        if (!showGoogleInput) {
                            TextButton(onClick = { viewModel.generateLoginCode() }) {
                                Text("Générez un nouveau code", color = Color.White.copy(alpha = 0.4f))
                            }
                        }
                    }
                }
        
        }
    }
}
