package com.nallanudi.ai.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nallanudi.ai.domain.model.Term
import com.nallanudi.ai.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(viewModel: MainViewModel, navController: NavController) {
    val allTerms by viewModel.terms.collectAsState()
    var currentTermIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    // Simple quiz: 5 random terms
    val quizTerms = remember(allTerms) {
        if (allTerms.size >= 5) allTerms.shuffled().take(5) else allTerms
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vocabulary Quiz") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (quizTerms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Loading questions...")
            }
        } else if (quizFinished) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Quiz Completed!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Score: $score / ${quizTerms.size}", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Go Back")
                }
            }
        } else {
            val currentTerm = quizTerms[currentTermIndex]
            val options = remember(currentTermIndex) {
                val incorrect = allTerms.filter { it.id != currentTerm.id }.shuffled().take(3)
                (incorrect + currentTerm).shuffled()
            }

            Column(modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp)) {
                Text("Question ${currentTermIndex + 1} of ${quizTerms.size}", style = MaterialTheme.typography.labelLarge)
                LinearProgressIndicator(
                    progress = { (currentTermIndex + 1).toFloat() / quizTerms.size },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                Text("What is the Kannada meaning of:", style = MaterialTheme.typography.titleMedium)
                Text(text = currentTerm.englishWord, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                options.forEach { option ->
                    QuizOption(text = option.kannadaMeaning) {
                        if (option.id == currentTerm.id) {
                            score++
                        }
                        if (currentTermIndex < quizTerms.size - 1) {
                            currentTermIndex++
                        } else {
                            quizFinished = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOption(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp), fontSize = 18.sp)
    }
}
