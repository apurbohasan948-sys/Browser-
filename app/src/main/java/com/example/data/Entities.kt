package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "passwords")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val site: String,
    val username: String,
    val passwordEncoded: String, // Base64 or encrypted for security features
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "rewards")
data class RewardTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val points: Int,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloads")
data class DownloadEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val url: String,
    val filePath: String,
    val progress: Int, // 0 to 100
    val status: String, // "Downloading", "Completed", "Failed"
    val totalSize: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "offline_pages")
data class OfflinePage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val contentHtml: String,
    val timestamp: Long = System.currentTimeMillis()
)
