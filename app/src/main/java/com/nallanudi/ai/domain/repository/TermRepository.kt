package com.nallanudi.ai.domain.repository

import com.nallanudi.ai.domain.model.Term
import kotlinx.coroutines.flow.Flow

interface TermRepository {
    fun getAllTerms(): Flow<List<Term>>
    fun searchTerms(query: String): Flow<List<Term>>
    fun getTermsBySubject(subject: String): Flow<List<Term>>
    fun getBookmarkedTerms(): Flow<List<Term>>
    fun getRecentTerms(): Flow<List<Term>>
    suspend fun getTermById(id: Long): Term?
    suspend fun toggleBookmark(id: Long)
    suspend fun markAsViewed(id: Long)
    suspend fun getWordOfTheDay(): Term?
}
