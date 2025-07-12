package com.tedd.todo_project.histories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.usecase.GetCompletedTodosUseCase
import com.tedd.todo_project.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoriesViewModel @Inject constructor(
    private val getCompletedTodosUseCase: GetCompletedTodosUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoriesScreenState())
    val uiState: StateFlow<HistoriesScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCompletedTodosUseCase()
                .distinctUntilChanged()
                .collectLatest { todos ->
                    _uiState.update { it.copy(completedTodos = todos.toImmutableList()) }
                }
        }
    }

    fun onIntent(intent: HistoriesScreenIntent) {
        viewModelScope.launch {
            when (intent) {
                is HistoriesScreenIntent.OnNavigateBack -> navigator.navigateBack()
            }
        }
    }
}