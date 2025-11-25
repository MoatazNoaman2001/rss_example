package com.example.rssexample.presentation.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssFeedScreen(
    viewModel: RssFeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "NBC News - World",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is FeedUiState.Initial -> {
                    // Show nothing or a welcome message
                }
                is FeedUiState.Loading -> {
                    LoadingView()
                }
                is FeedUiState.Success -> {
                    NewsListView(articles = state.articles)
                }
                is FeedUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.loadFeed() }
                    )
                }
            }
        }
    }
}
