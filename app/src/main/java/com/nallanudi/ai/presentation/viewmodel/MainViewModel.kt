package com.nallanudi.ai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.ai.domain.model.Term
import com.nallanudi.ai.domain.repository.TermRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TermRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject = _selectedSubject.asStateFlow()

    val terms = combine(_searchQuery, _selectedSubject) { query, subject ->
        Pair(query, subject)
    }.flatMapLatest { (query, subject) ->
        if (query.isNotEmpty()) {
            repository.searchTerms(query)
        } else if (subject != "All") {
            repository.getTermsBySubject(subject)
        } else {
            repository.getAllTerms()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedTerms = repository.getBookmarkedTerms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentTerms = repository.getRecentTerms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _wordOfTheDay = MutableStateFlow<Term?>(null)
    val wordOfTheDay = _wordOfTheDay.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTerms().firstOrNull()?.let { list ->
                if (list.isNotEmpty()) {
                    _wordOfTheDay.value = list.random()
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSubjectSelected(subject: String) {
        _selectedSubject.value = if (_selectedSubject.value == subject) "All" else subject
    }

    fun toggleBookmark(term: Term) {
        viewModelScope.launch {
            repository.toggleBookmark(term.id)
        }
    }

    fun markAsViewed(term: Term) {
        viewModelScope.launch {
            repository.markAsViewed(term.id)
        }
    }

    // --- Local Mock AI Chat Logic ---
    
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading = _isChatLoading.asStateFlow()

    fun sendChatMessage(message: String, contextTerm: Term? = null) {
        val userMessage = ChatMessage(message, true)
        _chatMessages.value += userMessage
        _isChatLoading.value = true

        viewModelScope.launch {
            delay(1000) // Simulate processing time offline
            val responseText = generateLocalResponse(message, contextTerm)
            val aiMessage = ChatMessage(responseText, false)
            _chatMessages.value += aiMessage
            _isChatLoading.value = false
        }
    }

    private fun generateLocalResponse(message: String, contextTerm: Term?): String {
        val msg = message.lowercase()
        
        // Contextual responses if a term is provided
        if (contextTerm != null) {
            if (msg.contains("explain") || msg.contains("vivarisu") || msg.contains("viva")) {
                return "'${contextTerm.englishWord}' ಕುರಿತು ಹೆಚ್ಚಿನ ಮಾಹಿತಿ: ಇದು ${contextTerm.subject} ವಿಷಯಕ್ಕೆ ಸಂಬಂಧಿಸಿದೆ. ಕನ್ನಡದಲ್ಲಿ ಇದರ ಅರ್ಥ '${contextTerm.kannadaMeaning}'. ${contextTerm.kannadaExplanation}"
            }
            if (msg.contains("example") || msg.contains("udaharane")) {
                return "'${contextTerm.englishWord}' ಗೆ ಉದಾಹರಣೆ ಇಲ್ಲಿದೆ: ${contextTerm.example}"
            }
        }

        // Generic technical learning responses
        return when {
            msg.contains("hello") || msg.contains("hi") || msg.contains("namaste") -> 
                "ನಮಸ್ತೆ! ನಾನು ನಲ್ಲನುಡಿ AI ಸಹಾಯಕ. ನಿಮಗೆ ಯಾವ ತಾಂತ್ರಿಕ ಪದದ ಬಗ್ಗೆ ತಿಳಿಯಬೇಕಿದೆ? (Namaste! I'm NallaNudi AI helper. Which technical term do you want to learn about?)"
            
            msg.contains("science") -> 
                "ವಿಜ್ಞಾನ (Science) ವಿಭಾಗದಲ್ಲಿ ನೀವು ಗುರುತ್ವಾಕರ್ಷಣೆ (Gravity), ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ (Photosynthesis) ಮುಂತಾದ ಪದಗಳನ್ನು ಕಲಿಯಬಹುದು."
            
            msg.contains("math") || msg.contains("ganitha") -> 
                "ಗಣಿತದಲ್ಲಿ (Mathematics) ನಾವು ತ್ರಿಕೋನಮಿತಿ (Trigonometry), ವ್ಯುತ್ಪನ್ನ (Derivative) ಮುಂತಾದ ಪದಗಳ ಅರ್ಥವನ್ನು ನೀಡಿದ್ದೇವೆ."
            
            msg.contains("how to use") -> 
                "ನೀವು ಸರ್ಚ್ ಬಾರ್ ನಲ್ಲಿ ಇಂಗ್ಲಿಷ್ ಪದಗಳನ್ನು ಟೈಪ್ ಮಾಡಿ ಅವುಗಳ ಕನ್ನಡ ಅರ್ಥ ಮತ್ತು ವಿವರಣೆಯನ್ನು ಪಡೆಯಬಹುದು."
            
            else -> "ಸದ್ಯಕ್ಕೆ ನಾನು ಈ ಬಗ್ಗೆ ಹೆಚ್ಚಿನ ವಿವರಗಳನ್ನು ಹೊಂದಿಲ್ಲ. ದಯವಿಟ್ಟು ಸರ್ಚ್ ಬಾರ್ ಅನ್ನು ಬಳಸಿ ಅಥವಾ ವಿಷಯಗಳ ಆಧಾರದ ಮೇಲೆ ಫಿಲ್ಟರ್ ಮಾಡಿ. (I don't have much details on this right now. Please use search or filter by subjects.)"
        }
    }
}

data class ChatMessage(val text: String, val isUser: Boolean)
