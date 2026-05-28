package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Bookmark
import com.example.data.HistoryEntry
import com.example.data.PasswordEntry
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BrowserTab
import com.example.viewmodel.BrowserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Glowing Celestial AI Animated Avatar Assistant ---
@Composable
fun AIAnimatedAvatar(
    isGenerating: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    
    // Scale pulsing
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Inner rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isGenerating) 1500 else 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Vibrant glowing color
    val pulseColor = if (isGenerating) Color(0xFF00E5FF) else Color(0xFFE91E63)

    Box(
        modifier = modifier
            .size(76.dp)
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Aura ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(pulseColor, Color.Transparent, pulseColor.copy(alpha = 0.4f), pulseColor)
                        ),
                        startAngle = rotation,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = if (isGenerating) 8f else 4f)
                    )
                }
        )

        // Sphere Core
        Box(
            modifier = Modifier
                .size(54.dp)
                .shadow(elevation = 12.dp, shape = CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(pulseColor, pulseColor.copy(alpha = 0.6f), Color(0xFF12141C))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "Aura AI Assistant",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

// --- Speed Dial / Homepage Screen ---
@Composable
fun SpeedDialHomepage(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val totalCoins by viewModel.totalPoints.collectAsState()
    val newsItems by viewModel.newsList.collectAsState()

    val quickLinks = listOf(
        Triple("Google", "https://www.google.com", Icons.Default.Search),
        Triple("YouTube", "https://www.youtube.com", Icons.Default.PlayArrow),
        Triple("Wikipedia", "https://www.wikipedia.org", Icons.Default.Info),
        Triple("Y Combinator", "https://news.ycombinator.com", Icons.Default.TrendingUp),
        Triple("Amazon", "https://www.amazon.com", Icons.Default.ShoppingCart),
        Triple("Tech News", "https://techcrunch.com", Icons.Default.Newspaper),
        Triple("AI Lounge", "Aura:Chat", Icons.Default.Face),
        Triple("My Wallet", "Aura:Wallet", Icons.Default.AccountBalanceWallet)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming Card with current coins balance
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AuraMini Browser",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Turbospeed Web Proxy",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    
                    // Coins counter pill
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF2E241F), RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFFFFD600), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Reward Points",
                            tint = Color(0xFFFFD600),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$totalCoins AUR",
                            color = Color(0xFFFFD600),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Quick Links Grid
        item {
            Text(
                text = "SPEED DIAL",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val chunks = quickLinks.chunked(4)
                for (chunk in chunks) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (link in chunk) {
                            Column(
                                modifier = Modifier
                                    .width(72.dp)
                                    .clickable {
                                        if (link.second == "Aura:Chat") {
                                            viewModel.navigateTo(AppScreen.AI_CHATBOT)
                                        } else if (link.second == "Aura:Wallet") {
                                            viewModel.navigateTo(AppScreen.WALLET_REWARDS)
                                        } else {
                                            viewModel.addNewTab(link.second)
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color(0xFF1E212E), CircleShape)
                                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = link.third,
                                        contentDescription = link.first,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = link.first,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }

        // Curated News Feed with coin awards
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "WORLD NEWS FEED",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp),
                color = Color.Gray
            )
        }

        items(newsItems) { news ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        // Open in new tab + reward points
                        viewModel.addNewTab(news.url)
                        viewModel.earnPoints(news.pointsAward, "Read Article: ${news.title}")
                    },
                shape = RoundedCornerShape(12.dp),
                border = if (news.isSponsored) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) else null,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Feature Image Placeholder
                    AsyncImage(
                        model = news.imageUrl,
                        contentDescription = "News Image",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = news.category.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (news.isSponsored) MaterialTheme.colorScheme.primary else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // Sponsored Card Indicator
                            if (news.isSponsored) {
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "SPONSORED",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        Text(
                            text = news.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = news.summary,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = Color.LightGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = "Points",
                                tint = Color(0xFFFFD600),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+${news.pointsAward} AUR Coins",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFFFD600)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Wallet & Rewards Screen ---
@Composable
fun WalletRewardsScreen(viewModel: BrowserViewModel) {
    val totalCoins by viewModel.totalPoints.collectAsState()
    val rewardTransactions by viewModel.rewards.collectAsState()

    var dailyClaimed by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balances Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFE91E63), Color(0xFF673AB7))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text("Total Native Currency Balance", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$totalCoins AUR", color = Color.White, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("≈ \$${String.format(Locale.getDefault(), "%.4f", (totalCoins ?: 0) * 0.0012)} USDT (Cosmic Spot Exchange)", color = Color(0xFF00E5FF), style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // Daily Check-in Claim
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Daily Reward Boost", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Earn 50 AUR coins instantly", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            if (!dailyClaimed) {
                                viewModel.performDailyCheckIn()
                                dailyClaimed = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (dailyClaimed) Color.Gray else MaterialTheme.colorScheme.primary),
                        enabled = !dailyClaimed
                    ) {
                        Text(if (dailyClaimed) "CLAIMED" else "CLAIM")
                    }
                }
            }
        }

        // Watch Ads for Crypto VPN Mode
        item {
            Card(
                border = BorderStroke(1.dp, Color(0xFF00E5FF)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Turbo VPS Mode Unlocker", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF00E5FF))
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked", tint = Color(0xFF00E5FF))
                    }
                    Text("Unlock 24 hours of extreme AdBlock + Premium Security Tunneling proxy. Compensate creators by viewing a diagnostic reward stream.", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.watchRewardedAdSimulation()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Watch Rewarded Stream (Award 100 AUR)", color = Color.Black)
                    }
                }
            }
        }

        // Coin Ledger Transaction History
        item {
            Text(
                text = "REWARD LEDGER HISTORIC TRANSACTIONS",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp),
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (rewardTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions logged. Start surfing the web to accumulate AUR coins!", color = Color.Gray, textAlign = TextAlign.Center)
                }
            }
        } else {
            items(rewardTransactions) { ledger ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(ledger.description, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                        val date = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(Date(ledger.timestamp))
                        Text(date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Text(
                        text = "+${ledger.points} AUR",
                        color = Color(0xFFFFD600),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                }
            }
        }
    }
}

// --- Multi tab System View ---
@Composable
fun TabSystemView(
    viewModel: BrowserViewModel,
    onTabSelected: (String) -> Unit
) {
    val tabsList by viewModel.tabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Active Browser Tab Manager", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
            IconButton(
                onClick = { viewModel.addNewTab() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Tab", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(tabsList) { tab ->
                val isActive = tab.id == activeTabId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clickable { onTabSelected(tab.id) }
                        .border(
                            width = if (isActive) 2.dp else 1.dp,
                            color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = if (isActive) Color(0xFF1F2231) else MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (tab.isIncognito) "Incognito" else "Secure Session",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tab.isIncognito) Color(0xFF00E5FF) else MaterialTheme.colorScheme.primary
                            )
                            IconButton(
                                onClick = { viewModel.closeTab(tab.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close tab",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = tab.url,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// --- History & Bookmarks Screen ---
@Composable
fun HistoryBookmarksScreen(viewModel: BrowserViewModel) {
    var selectedIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Bookmarks", "History")

    val bookmarksList by viewModel.bookmarks.collectAsState()
    val historyList by viewModel.history.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    text = { Text(title) }
                )
            }
        }

        if (selectedIndex == 0) {
            // Bookmarks
            if (bookmarksList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No bookmarks saved. Tap the bookmark icon in the browser toolbar!", color = Color.Gray, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(bookmarksList) { bookmark ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                .clickable { viewModel.addNewTab(bookmark.url) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bookmark.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(bookmark.url, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            IconButton(onClick = { viewModel.deleteBookmark(bookmark) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        } else {
            // History
            if (historyList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your browsing history is clean.", color = Color.Gray)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Button(
                        onClick = { viewModel.clearHistory() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Text("Clear Browsing History")
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(historyList) { element ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.addNewTab(element.url) }
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(element.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                    Text(element.url, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                IconButton(onClick = { viewModel.deleteHistoryId(element.id) }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Remove entry", tint = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Chatbot Screen with interactive voice command command animation ---
@Composable
fun ChatbotScreen(viewModel: BrowserViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    var userText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Floating animated avatar on top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AIAnimatedAvatar(isGenerating = isGenerating)
            Column {
                Text("Aura Companion", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black))
                Text(if (isGenerating) "Processing holographic thoughts..." else "AI Sandbox Listening", style = MaterialTheme.typography.labelSmall, color = if (isGenerating) Color(0xFF00E5FF) else Color.Gray)
            }
        }

        Divider(color = Color.DarkGray)

        // Messages column
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "user"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) MaterialTheme.colorScheme.primary else Color(0xFF1E212D)
                        ),
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (isUser) 12.dp else 0.dp,
                            bottomEnd = if (isUser) 0.dp else 12.dp
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(msg.text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        // Input row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userText,
                onValueChange = { userText = it },
                placeholder = { Text("Ask Aura anything...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (userText.isNotBlank()) {
                        viewModel.sendChatMessage(userText)
                        userText = ""
                    }
                })
            )
            IconButton(
                onClick = {
                    if (userText.isNotBlank()) {
                        viewModel.sendChatMessage(userText)
                        userText = ""
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message", tint = Color.White)
            }
        }
    }
}

// --- Password Vault Screen ---
@Composable
fun PasswordVaultScreen(viewModel: BrowserViewModel) {
    val passwordsList by viewModel.passwords.collectAsState()
    var siteInput by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var passInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Secure Password Vault", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
        Text("Save your site credentials locally inside an encrypted AES Room storage container.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Add New Secret", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                OutlinedTextField(value = siteInput, onValueChange = { siteInput = it }, label = { Text("Website / Domain") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = userInput, onValueChange = { userInput = it }, label = { Text("Username / Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = passInput, onValueChange = { passInput = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        if (siteInput.isNotBlank() && userInput.isNotBlank() && passInput.isNotBlank()) {
                            viewModel.savePasswordSecret(siteInput, userInput, passInput)
                            siteInput = ""
                            userInput = ""
                            passInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Encrypt and Save Credentials")
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(passwordsList) { secret ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(secret.site, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        Text("User: ${secret.username}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text("Pass: •••••••• (Encrypted)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    IconButton(onClick = { viewModel.deletePasswordSecret(secret) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                    }
                }
            }
        }
    }
}

// --- Downloads Screen ---
@Composable
fun DownloadsScreen(viewModel: BrowserViewModel) {
    val downloadsList by viewModel.downloads.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Offline Downloads Controller", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
        
        if (downloadsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your downloaded files list is empty.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(downloadsList) { file ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(file.fileName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(file.url, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(progress = { file.progress / 100f }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Status: ${file.status}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                Text("${file.progress}% Completed (${file.totalSize})", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = { viewModel.deleteDownloadFile(file.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete entry", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// --- Saved Pages for Offline ---
@Composable
fun SavedPagesScreen(viewModel: BrowserViewModel) {
    val pages by viewModel.offlinePages.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Saved Offline Webpages", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
        Text("Access complete website copies loaded directly from your device when you don't have cellular internet.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        if (pages.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No saved webpages cataloged.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pages) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .clickable {
                                // Simulate loading locally cached html
                                viewModel.addNewTab(item.url)
                            }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(item.url, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                        IconButton(onClick = { viewModel.deleteOfflinePageEntry(item.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove copy", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// --- Reader Mode Slate ---
@Composable
fun ReaderModeScreen(viewModel: BrowserViewModel) {
    val readerText by viewModel.readerModeText.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF231F1C)) // Eyesafe sepia tint
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("READER STAGE", color = Color(0xFFE5A93C), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp))
            IconButton(onClick = { viewModel.navigateTo(AppScreen.BROWSER) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Mode", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    text = readerText ?: "Loading clean document details...",
                    color = Color(0xFFECE5D8),
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp, fontSize = 18.sp)
                )
            }
        }
    }
}

// --- Customizable Settings Screen ---
@Composable
fun SettingsScreen(viewModel: BrowserViewModel) {
    val adBlockEnabled by viewModel.adBlockEnabled.collectAsState()
    val dataSavingEnabled by viewModel.dataSavingEnabled.collectAsState()
    val websiteDarkMode by viewModel.websiteDarkMode.collectAsState()
    val selectedSearchEngine by viewModel.selectedSearchEngine.collectAsState()
    var lowPowerMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("System Preferences", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("OPTIMIZATIONS & ENGINE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Active Search Engine", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text("Default query routing for terms & search actions", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Google", "Bing", "Yahoo", "DuckDuckGo").forEach { engine ->
                        val isSelected = selectedSearchEngine == engine
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF1E212E),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.setSearchEngine(engine) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = engine,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Divider(color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Cloud AdBlocker", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text("Filters 99% malicious tracking networks", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = adBlockEnabled, onCheckedChange = { viewModel.toggleAdBlock() })
                }

                Divider(color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Extreme Data Saving (Lite mode)", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text("Minimizes media payloads & compiles text priority", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = dataSavingEnabled, onCheckedChange = { viewModel.toggleDataSaving() })
                }

                Divider(color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Force Web Dark theme", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text("Injects custom CSS to paint body dark", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = websiteDarkMode, onCheckedChange = { viewModel.toggleWebsiteDarkMode() })
                }

                Divider(color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Low Battery Saving Mode", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text("Stops animated avatars & limits background polling", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Switch(checked = lowPowerMode, onCheckedChange = { lowPowerMode = it })
                }
            }
        }

        // About card
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("AuraMini Pro Browser v1.0.4", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Powered by custom Android engine optimizations, integrated local SQLite database, Google AI Studio, and extreme cloud compression proxy logic.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.ADMIN_PANEL) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("Developer / Admin Panel Dashboard")
                }
            }
        }
    }
}

// --- Admin Panel Analytics Screen ---
@Composable
fun AdminPanelScreen(viewModel: BrowserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Admin Analytics Node", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
            IconButton(onClick = { viewModel.navigateTo(AppScreen.SETTINGS) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Active Handsets", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("402.1K", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ad Click Rate", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("6.84%", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF00E5FF))
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Decompress Ratio", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("1:9.2", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFFFFD600))
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Synced Files", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("1.25M", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Diagnostics & Server Sync", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("Simulate dispatch of Firebase Push notification to all synchronized handsets.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Button(
                    onClick = { viewModel.earnPoints(100, "Admin Server Synced bonus check") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dispatch Global Push notification token")
                }
            }
        }
    }
}
