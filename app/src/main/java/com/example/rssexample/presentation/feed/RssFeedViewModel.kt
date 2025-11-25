package com.example.rssexample.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rssexample.domain.usecase.GetRssFeedUseCase
import com.example.rssexample.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RssFeedViewModel @Inject constructor(
    private val getRssFeedUseCase: GetRssFeedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.Initial)
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _uiState.value = FeedUiState.Loading
            
            when (val result = getRssFeedUseCase()) {
                is Result.Success -> {
                    _uiState.value = FeedUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = FeedUiState.Error(result.message)
                }
            }
        }
    }
}

