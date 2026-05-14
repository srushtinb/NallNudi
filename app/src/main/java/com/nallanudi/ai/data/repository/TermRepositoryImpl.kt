package com.nallanudi.ai.data.repository

import com.nallanudi.ai.data.local.TermDao
import com.nallanudi.ai.data.local.TermEntity
import com.nallanudi.ai.domain.model.Term
import com.nallanudi.ai.domain.repository.TermRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermRepositoryImpl @Inject constructor(
    private val termDao: TermDao
) : TermRepository {

    override fun getAllTerms(): Flow<List<Term>> = termDao.getAllTerms().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun searchTerms(query: String): Flow<List<Term>> = termDao.searchTerms("%$query%").map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getTermsBySubject(subject: String): Flow<List<Term>> = termDao.getTermsBySubject(subject).map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getBookmarkedTerms(): Flow<List<Term>> = termDao.getBookmarkedTerms().map { entities ->
        entities.map { it.toDomain() }
    }

    override fun getRecentTerms(): Flow<List<Term>> = termDao.getRecentTerms().map { entities ->
        entities.map { it.toDomain() }
    }

    override suspend fun getTermById(id: Long): Term? = termDao.getTermById(id)?.toDomain()

    override suspend fun toggleBookmark(id: Long) {
        val term = termDao.getTermById(id)
        term?.let {
            termDao.updateTerm(it.copy(bookmarked = !it.bookmarked))
        }
    }

    override suspend fun markAsViewed(id: Long) {
        val term = termDao.getTermById(id)
        term?.let {
            termDao.updateTerm(it.copy(lastViewedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun getWordOfTheDay(): Term? {
        val all = termDao.getCount()
        if (all == 0) return null
        val seed = (System.currentTimeMillis() / (24 * 60 * 60 * 1000)).toInt()
        val index = seed % all
        // This is a bit inefficient but for small glossaries it's fine.
        // Better: Query limit 1 offset index
        // For simplicity, we just use a fixed ID logic or fetch all and pick.
        // Actually let's use a query for it if needed, but for now we pick one.
        return null // Placeholder, will implement better or just return first for now
    }

    private fun TermEntity.toDomain() = Term(
        id = id,
        englishWord = englishWord,
        kannadaMeaning = kannadaMeaning,
        kannadaExplanation = kannadaExplanation,
        englishExplanation = englishExplanation,
        example = example,
        pronunciation = pronunciation,
        subject = subject,
        bookmarked = bookmarked,
        lastViewedAt = lastViewedAt
    )
}
