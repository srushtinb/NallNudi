package com.nallanudi.ai.domain.model

data class Term(
    val id: Long,
    val englishWord: String,
    val kannadaMeaning: String,
    val kannadaExplanation: String,
    val englishExplanation: String,
    val example: String,
    val pronunciation: String,
    val subject: String,
    val bookmarked: Boolean,
    val lastViewedAt: Long
)
