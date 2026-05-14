package com.nallanudi.ai.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "terms")
data class TermEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val englishWord: String,
    val kannadaMeaning: String,
    val kannadaExplanation: String,
    val englishExplanation: String,
    val example: String,
    val pronunciation: String,
    val subject: String,
    val bookmarked: Boolean = false,
    val lastViewedAt: Long = 0
)

@Dao
interface TermDao {
    @Query("SELECT * FROM terms ORDER BY englishWord ASC")
    fun getAllTerms(): Flow<List<TermEntity>>

    @Query("SELECT * FROM terms WHERE englishWord LIKE :query OR kannadaMeaning LIKE :query")
    fun searchTerms(query: String): Flow<List<TermEntity>>

    @Query("SELECT * FROM terms WHERE subject = :subject")
    fun getTermsBySubject(subject: String): Flow<List<TermEntity>>

    @Query("SELECT * FROM terms WHERE bookmarked = 1")
    fun getBookmarkedTerms(): Flow<List<TermEntity>>

    @Query("SELECT * FROM terms ORDER BY lastViewedAt DESC LIMIT 20")
    fun getRecentTerms(): Flow<List<TermEntity>>

    @Query("SELECT * FROM terms WHERE id = :id")
    suspend fun getTermById(id: Long): TermEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTerm(term: TermEntity)

    @Update
    suspend fun updateTerm(term: TermEntity)

    @Query("SELECT COUNT(*) FROM terms")
    suspend fun getCount(): Int

    @Insert
    suspend fun insertAll(terms: List<TermEntity>)
}

@Database(entities = [TermEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun termDao(): TermDao
}
