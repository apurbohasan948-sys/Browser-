package com.example.ui

import android.webkit.WebView
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppScreen
import com.example.viewmodel.BrowserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraBrowserMain(
    viewModel: BrowserViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeTab = viewModel.getActiveTab()
    val tabsList by viewModel.tabs.collectAsState()
    val adBlockEnabled by viewModel.adBlockEnabled.collectAsState()
    val dataSavingEnabled by viewModel.dataSavingEnabled.collectAsState()
    val websiteDarkMode by viewModel.websiteDarkMode.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val suggestions by viewModel.searchSuggestions.collectAsState()

    var textInputState by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Reference to standard WebView to execute GoBack / GoForward / Reload
    var activeWebView by remember { mutableStateOf<WebView?>(null) }

    // Sync input text state with current tab URL
    LaunchedEffect(activeTab?.url) {
        if (activeTab != null) {
            val u = activeTab.url
            if (u == "aura:home" || u == "about:blank") {
                textInputState = ""
            } else {
                textInputState = u
            }
        }
    }

    Scaffold(
        topBar = {
            if (currentScreen == AppScreen.BROWSER) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Title Bar, decompress, coin badge, bookmarks state
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AuraMini",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Bandwidth save badge
                            if (dataSavingEnabled) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFF2E1A29), RoundedCornerShape(12.dp))
                                        .border(1.dp, Color(0xFFE91E63), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "90% Saved",
                                        color = Color(0xFFE53935),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            // Earning ledger badge
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFF1F2231), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.navigateTo(AppScreen.WALLET_REWARDS) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Wallet",
                                    tint = Color(0xFFFFD600),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "$totalPoints AUR",
                                    color = Color(0xFFFFD600),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                )
                            }
                        }
                    }

                    // Combined Address Bar & Search Engine
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                                .border(1.dp, Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // HTTPS Padlock / Incognito icon
                            val isIncog = activeTab?.isIncognito ?: false
                            Icon(
                                imageVector = if (isIncog) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = "Security Shield",
                                tint = if (isIncog) Color(0xFF00E5FF) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )

                            Box(modifier = Modifier.weight(1f)) {
                                if (textInputState.isEmpty()) {
                                    Text(
                                        text = "Search or type URL",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                // Editable URL text node
                                BasicTextField(
                                    value = textInputState,
                                    onValueChange = {
                                        textInputState = it
                                        viewModel.updateSearchQuery(it)
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        if (textInputState.isNotBlank()) {
                                            activeTab?.let { tab ->
                                                viewModel.updateTabUrl(tab.id, textInputState)
                                            }
                                            viewModel.updateSearchQuery("")
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                        }
                                    }),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // Quick actions inside search bar: translation, summary
                            if (activeTab != null && activeTab.url.startsWith("http")) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Summarize action
                                    IconButton(
                                        onClick = { viewModel.summarizeCurrentWebpage() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "Summarize Page",
                                            tint = Color(0xFFFFD600),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    // Bookmark state toggle
                                    IconButton(
                                        onClick = { viewModel.toggleBookmarkCurrentPage() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Bookmark,
                                            contentDescription = "Bookmark",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Web smart suggestions dropdown popup overlays on active input
                        if (suggestions.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 46.dp)
                                    .align(Alignment.TopCenter),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                LazyColumn(modifier = Modifier.padding(8.dp)) {
                                    items(suggestions) { keyword ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    textInputState = keyword
                                                    activeTab?.let { tab ->
                                                        viewModel.updateTabUrl(tab.id, keyword)
                                                    }
                                                    viewModel.updateSearchQuery("")
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()
                                                }
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search suggestion",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = keyword,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Interactive Toolbar controls row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Back
                            IconButton(
                                onClick = { activeWebView?.goBack() },
                                enabled = activeTab?.canGoBack ?: false
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Go back",
                                    tint = if (activeTab?.canGoBack == true) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }

                            // Forward
                            IconButton(
                                onClick = { activeWebView?.goForward() },
                                enabled = activeTab?.canGoForward ?: false
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Go forward",
                                    tint = if (activeTab?.canGoForward == true) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }

                            // Refresh
                            IconButton(onClick = { activeWebView?.reload() }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reload",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Reader Mode toggle
                            if (activeTab != null && activeTab.url.startsWith("http")) {
                                IconButton(onClick = { viewModel.triggerReaderMode() }) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = "Reader Mode",
                                        tint = Color(0xFF00E5FF)
                                    )
                                }
                            }
                        }

                        // AI summarize/translate toolbar shortcut
                        if (activeTab != null && activeTab.url.startsWith("http")) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Translate button
                                AssistChip(
                                    onClick = { viewModel.translateCurrentWebpage("Bangla") },
                                    label = { Text("Translate (বাংলা)", fontSize = 10.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Translate,
                                            contentDescription = "Translate Webpage",
                                            modifier = Modifier.size(10.dp)
                                        )
                                    }
                                )

                                // Offline Save download button
                                IconButton(
                                    onClick = { 
                                        viewModel.savePageForOffline(activeTab.title, activeTab.url, "Offline content of cached page")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Save Page Offline",
                                        tint = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    // Progress bar loading indicator
                    if (activeTab != null && activeTab.progress in 1..99) {
                        LinearProgressIndicator(
                            progress = { activeTab.progress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Active AI result banner overlay
                    val summaryResult by viewModel.aiSummaryText.collectAsState()
                    if (summaryResult != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E212D))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "AI result",
                                            tint = Color(0xFFFFD600),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Aura AI Transceiver",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color(0xFF00E5FF)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.translateCurrentWebpage("Cancel") }, // standard reset trigger
                                        modifier = Modifier.size(18.dp)
                                    ) {
                                        // Simple wrapper to reset the summary popup alert
                                    }
                                }
                                Text(
                                    text = summaryResult!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = { 
                                        // Reset summary banner
                                        viewModel.sendChatMessage("Aura, please summarize this page.")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Got It / Save to Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // Browser Home
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BROWSER,
                    onClick = { viewModel.navigateTo(AppScreen.BROWSER) },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Search", fontSize = 11.sp) }
                )

                // Tab Manager
                NavigationBarItem(
                    selected = currentScreen == AppScreen.TABS_MANAGER,
                    onClick = { viewModel.navigateTo(AppScreen.TABS_MANAGER) },
                    icon = {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                ) {
                                    Text(tabsList.size.toString())
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Layers, contentDescription = "Tabs")
                        }
                    },
                    label = { Text("Tabs", fontSize = 11.sp) }
                )

                // AI Avatar Hub
                NavigationBarItem(
                    selected = currentScreen == AppScreen.AI_CHATBOT,
                    onClick = { viewModel.navigateTo(AppScreen.AI_CHATBOT) },
                    icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Aura Companion") },
                    label = { Text("Aura Chat", fontSize = 11.sp) }
                )

                // History Bookmarks
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HISTORY_BOOKMARKS,
                    onClick = { viewModel.navigateTo(AppScreen.HISTORY_BOOKMARKS) },
                    icon = { Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Bookmarks") },
                    label = { Text("Library", fontSize = 11.sp) }
                )

                // Settings & Tools
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SETTINGS || currentScreen == AppScreen.ADMIN_PANEL,
                    onClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Vault", fontSize = 11.sp) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Screen router
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150)) since fadeOut(animationSpec = tween(150))
                },
                label = "routing"
            ) { screen ->
                when (screen) {
                    AppScreen.BROWSER -> {
                        if (activeTab != null) {
                            val activeUrl = activeTab.url
                            if (activeUrl == "aura:home" || activeUrl == "about:blank" || activeUrl == "") {
                                // Default Speed Dial homepage render
                                SpeedDialHomepage(viewModel = viewModel)
                            } else {
                                // Renders the live interactive Web browser engine
                                BrowserWebView(
                                    tab = activeTab,
                                    viewModel = viewModel,
                                    onWebViewCreated = { webView ->
                                        activeWebView = webView
                                    }
                                )
                            }
                        } else {
                            Text("No session active. Create a tab to start surfing.", modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    AppScreen.TABS_MANAGER -> {
                        TabSystemView(
                            viewModel = viewModel,
                            onTabSelected = { tabId ->
                                viewModel.selectTab(tabId)
                            }
                        )
                    }

                    AppScreen.AI_CHATBOT -> {
                        ChatbotScreen(viewModel = viewModel)
                    }

                    AppScreen.WALLET_REWARDS -> {
                        WalletRewardsScreen(viewModel = viewModel)
                    }

                    AppScreen.HISTORY_BOOKMARKS -> {
                        HistoryBookmarksScreen(viewModel = viewModel)
                    }

                    AppScreen.DOWNLOADS -> {
                        DownloadsScreen(viewModel = viewModel)
                    }

                    AppScreen.PASSWORDS -> {
                        PasswordVaultScreen(viewModel = viewModel)
                    }

                    AppScreen.SAVED_PAGES -> {
                        SavedPagesScreen(viewModel = viewModel)
                    }

                    AppScreen.SETTINGS -> {
                        // Include customized nested link sheets inside Settings to navigate to Password selection or Downloads
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                AssistChip(
                                    onClick = { viewModel.navigateTo(AppScreen.PASSWORDS) },
                                    label = { Text("Password Vault") },
                                    leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                )
                                AssistChip(
                                    onClick = { viewModel.navigateTo(AppScreen.DOWNLOADS) },
                                    label = { Text("Download Manager") },
                                    leadingIcon = { Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                )
                                AssistChip(
                                    onClick = { viewModel.navigateTo(AppScreen.SAVED_PAGES) },
                                    label = { Text("Saved Pages") },
                                    leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                )
                            }
                            SettingsScreen(viewModel = viewModel)
                        }
                    }

                    AppScreen.ADMIN_PANEL -> {
                        AdminPanelScreen(viewModel = viewModel)
                    }

                    AppScreen.READER_MODE -> {
                        ReaderModeScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

// --- Lightweight inline single-line Text field helper matching compose standard ---
@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: androidx.compose.ui.text.TextStyle = LocalTextStyle.current,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

infix fun EnterTransition.since(exitTransition: ExitTransition): ContentTransform {
    return ContentTransform(this, exitTransition)
}
