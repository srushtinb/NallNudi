package com.nallanudi.ai.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nallanudi.ai.domain.model.Term
import com.nallanudi.ai.navigation.Screen
import com.nallanudi.ai.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, navController: NavController) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val terms by viewModel.terms.collectAsState()
    val wordOfTheDay by viewModel.wordOfTheDay.collectAsState()
    val recentTerms by viewModel.recentTerms.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ನಲ್ಲ-ನುಡಿ (Nalla-Nudi)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.Favorites.route) },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                    label = { Text("Saved") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.Chat.route) },
                    icon = { Icon(Icons.Default.Chat, contentDescription = "AI Chat") },
                    label = { Text("AI Assist") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.Quiz.route) }) {
                Icon(Icons.Default.QuestionAnswer, contentDescription = "Quiz")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (searchQuery.isEmpty()) {
                item {
                    WordOfTheDayCard(wordOfTheDay) {
                        wordOfTheDay?.let {
                            viewModel.markAsViewed(it)
                            navController.navigate(Screen.Detail.createRoute(it.id))
                        }
                    }
                }

                item {
                    SubjectFilters(selectedSubject) { viewModel.onSubjectSelected(it) }
                }

                if (recentTerms.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recently Viewed",
                            modifier = Modifier.padding(16.dp, 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                            items(recentTerms) { term ->
                                RecentTermItem(term) {
                                    viewModel.markAsViewed(term)
                                    navController.navigate(Screen.Detail.createRoute(term.id))
                                }
                            }
                        }
                    }
                }
                
                item {
                    QuickActionsSection(navController)
                }
            }

            items(terms) { term ->
                TermListItem(term) {
                    viewModel.markAsViewed(term)
                    navController.navigate(Screen.Detail.createRoute(term.id))
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        placeholder = { Text("Search English Technical terms...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun WordOfTheDayCard(term: Term?, onClick: () -> Unit) {
    if (term == null) return
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Word of the Day",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = term.englishWord, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = term.kannadaMeaning, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = term.kannadaExplanation, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SubjectFilters(selected: String, onSelect: (String) -> Unit) {
    val subjects = listOf("Science", "Mathematics", "Commerce", "Computer Science")
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(subjects) { subject ->
            FilterChip(
                selected = selected == subject,
                onClick = { onSelect(subject) },
                label = { Text(subject) }
            )
        }
    }
}

@Composable
fun RecentTermItem(term: Term, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = term.englishWord, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = term.kannadaMeaning, fontSize = 12.sp, maxLines = 1)
        }
    }
}

@Composable
fun TermListItem(term: Term, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = term.englishWord, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = term.kannadaMeaning, style = MaterialTheme.typography.bodyLarge)
            }
            Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(term.subject, modifier = Modifier.padding(4.dp), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f).clickable { navController.navigate(Screen.Flashcards.route) },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Style, contentDescription = null, tint = Color(0xFF1976D2))
                Text("Flashcards", fontWeight = FontWeight.Medium)
            }
        }
        Card(
            modifier = Modifier.weight(1f).clickable { navController.navigate(Screen.Quiz.route) },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Assignment, contentDescription = null, tint = Color(0xFFF57C00))
                Text("Practice Quiz", fontWeight = FontWeight.Medium)
            }
        }
    }
}
