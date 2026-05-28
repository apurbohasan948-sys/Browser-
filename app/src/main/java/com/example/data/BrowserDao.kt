package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserDao {

    // --- Bookmarks ---
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE url = :url")
    suspend fun deleteBookmarkByUrl(url: String)

    @Query("SELECT EXISTS(SELECT * FROM bookmarks WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean


    // --- History ---
    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryEntry>>

    @Query("SELECT * FROM history WHERE title LIKE '%' || :query || '%' OR url LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchHistory(query: String): Flow<List<HistoryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entry: HistoryEntry)

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)

    @Query("DELETE FROM history")
    suspend fun clearAllHistory()


    // --- Passwords ---
    @Query("SELECT * FROM passwords ORDER BY site ASC")
    fun getAllPasswords(): Flow<List<PasswordEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(entry: PasswordEntry)

    @Delete
    suspend fun deletePassword(entry: PasswordEntry)


    // --- Rewards ---
    @Query("SELECT * FROM rewards ORDER BY timestamp DESC")
    fun getAllRewards(): Flow<List<RewardTransaction>>

    @Query("SELECT SUM(points) FROM rewards")
    fun getTotalPoints(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRewardTransaction(transaction: RewardTransaction)


    // --- Downloads ---
    @Query("SELECT * FROM downloads ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntry)

    @Query("UPDATE downloads SET progress = :progress, status = :status WHERE id = :id")
    suspend fun updateDownloadStatus(id: Int, progress: Int, status: String)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteDownloadById(id: Int)


    // --- Offline Pages ---
    @Query("SELECT * FROM offline_pages ORDER BY timestamp DESC")
    fun getAllOfflinePages(): Flow<List<OfflinePage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflinePage(page: OfflinePage)

    @Query("DELETE FROM offline_pages WHERE id = :id")
    suspend fun deleteOfflinePageById(id: Int)
}
