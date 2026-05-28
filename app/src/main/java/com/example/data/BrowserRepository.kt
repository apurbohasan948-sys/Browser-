package com.example.data

import kotlinx.coroutines.flow.Flow

class BrowserRepository(private val dao: BrowserDao) {

    // --- Bookmarks ---
    val allBookmarks: Flow<List<Bookmark>> = dao.getAllBookmarks()

    suspend fun addBookmark(bookmark: Bookmark) = dao.insertBookmark(bookmark)

    suspend fun removeBookmark(bookmark: Bookmark) = dao.deleteBookmark(bookmark)

    suspend fun removeBookmarkByUrl(url: String) = dao.deleteBookmarkByUrl(url)

    suspend fun isBookmarked(url: String): Boolean = dao.isBookmarked(url)


    // --- History ---
    val allHistory: Flow<List<HistoryEntry>> = dao.getAllHistory()

    fun searchHistory(query: String): Flow<List<HistoryEntry>> = dao.searchHistory(query)

    suspend fun addHistory(entry: HistoryEntry) = dao.insertHistory(entry)

    suspend fun removeHistoryById(id: Int) = dao.deleteHistoryById(id)

    suspend fun clearHistory() = dao.clearAllHistory()


    // --- Passwords ---
    val allPasswords: Flow<List<PasswordEntry>> = dao.getAllPasswords()

    suspend fun addPassword(entry: PasswordEntry) = dao.insertPassword(entry)

    suspend fun removePassword(entry: PasswordEntry) = dao.deletePassword(entry)


    // --- Rewards ---
    val allRewards: Flow<List<RewardTransaction>> = dao.getAllRewards()
    val totalPoints: Flow<Int?> = dao.getTotalPoints()

    suspend fun addRewardTransaction(transaction: RewardTransaction) = dao.insertRewardTransaction(transaction)


    // --- Downloads ---
    val allDownloads: Flow<List<DownloadEntry>> = dao.getAllDownloads()

    suspend fun addDownload(download: DownloadEntry) = dao.insertDownload(download)

    suspend fun updateDownloadProgress(id: Int, progress: Int, status: String) = dao.updateDownloadStatus(id, progress, status)

    suspend fun removeDownloadById(id: Int) = dao.deleteDownloadById(id)


    // --- Offline Pages ---
    val allOfflinePages: Flow<List<OfflinePage>> = dao.getAllOfflinePages()

    suspend fun addOfflinePage(page: OfflinePage) = dao.insertOfflinePage(page)

    suspend fun removeOfflinePageById(id: Int) = dao.deleteOfflinePageById(id)
}
