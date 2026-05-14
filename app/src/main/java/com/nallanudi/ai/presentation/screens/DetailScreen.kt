package com.nallanudi.ai.presentation.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nallanudi.ai.navigation.Screen
import com.nallanudi.ai.presentation.viewmodel.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(termId: Long, viewModel: MainViewModel, navController: NavController) {
    val terms by viewModel.terms.collectAsState()
    val term = terms.find { it.id == termId } ?: return
    val context = LocalContext.current
    
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Term Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleBookmark(term) }) {
                        Icon(
                            if (term.bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark"
                        )
                    }
                    IconButton(onClick = { /* Share logic */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = term.englishWord, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(text = term.pronunciation, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(modifier = Modifier.size(24.dp), onClick = {
                    tts?.speak(term.englishWord, TextToSpeech.QUEUE_FLUSH, null, null)
                }) {
                    Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Pronounce", tint = MaterialTheme.colorScheme.primary)
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            DetailSection("Kannada Meaning", term.kannadaMeaning)
            DetailSection("Kannada Explanation", term.kannadaExplanation)
            DetailSection("English Explanation", term.englishExplanation)
            DetailSection("Example Usage", term.example)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                onClick = { navController.navigate(Screen.Chat.createRoute(term.id)) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Ask AI Assistant", fontWeight = FontWeight.Bold)
                        Text("Get more detailed explanations and doubts cleared.", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Related Terms", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            // Implementation of related terms...
        }
    }
}

@Composable
fun DetailSection(title: String, content: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = content, style = MaterialTheme.typography.bodyLarge)
    }
}
