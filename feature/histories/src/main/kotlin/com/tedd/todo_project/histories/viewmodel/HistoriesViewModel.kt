package com.tedd.todo_project.histories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.usecase.DeleteTodoUseCase
import com.tedd.todo_project.domain.usecase.DeleteTodosUseCase
import com.tedd.todo_project.domain.usecase.GetCompletedTodosUseCase
import com.tedd.todo_project.navigator.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoriesViewModel @Inject constructor(
    private val getCompletedTodosUseCase: GetCompletedTodosUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val deleteTodosUseCase: DeleteTodosUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoriesScreenState())
    val uiState: StateFlow<HistoriesScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCompletedTodosUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .distinctUntilChanged()
                .collectLatest { todos ->
                    _uiState.update {
                        it.copy(
                            histories = todos.toImmutableList(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onIntent(intent: HistoriesScreenIntent) {
        viewModelScope.launch {
            when (intent) {
                is HistoriesScreenIntent.OnNavigateBack -> navigator.navigateBack()
                is HistoriesScreenIntent.OnClearSelection -> clearSelection()
                is HistoriesScreenIntent.OnDeleteHistory -> deleteTodo(todo = intent.todo)
                is HistoriesScreenIntent.OnDeleteSelectedHistories -> deleteSelectedHistories()
                is HistoriesScreenIntent.OnSelectAllHistories -> selectAllHistories()
                is HistoriesScreenIntent.OnSelectHistory -> selectHistories(historyId = intent.historyId)
                is HistoriesScreenIntent.OnSwipeStateChange -> _uiState.update {
                    it.copy(
                        swipingHistoryId = if (intent.isSwiping) intent.historyId else null
                    )
                }
            }
        }
    }

    private fun clearSelection() {
        _uiState.update { it.copy(isSelectionMode = false, selectedHistoriesIds = emptySet()) }
    }

    private suspend fun deleteTodo(todo: Todo) {
        deleteTodoUseCase(todo = todo)
    }

    private suspend fun deleteSelectedHistories() {
        val historiesMap = _uiState.value.histories.associateBy { it.id }
        val completedTodosToDelete = _uiState.value.selectedHistoriesIds
            .mapNotNull { historiesMap[it] }

        if (completedTodosToDelete.isNotEmpty()) {
            deleteTodosUseCase(todos = completedTodosToDelete)
        }

        clearSelection()
    }

    private fun selectAllHistories() {
        _uiState.update { currentState ->
            val allHistoriesIds = currentState.histories.map { it.id }.toSet()
            currentState.copy(selectedHistoriesIds = allHistoriesIds)
            if (currentState.histories.isNotEmpty() && currentState.selectedHistoriesIds.size == allHistoriesIds.size) {
                currentState.copy(selectedHistoriesIds = emptySet())
            } else {
                currentState.copy(selectedHistoriesIds = allHistoriesIds)
            }
        }
    }

    private fun selectHistories(historyId: Long) {
        _uiState.update { currentState ->
            val selectedHistoriesIds = if (currentState.selectedHistoriesIds.contains(historyId)) {
                currentState.selectedHistoriesIds.minus(element = historyId)
            } else {
                currentState.selectedHistoriesIds.plus(element = historyId)
            }
            currentState.copy(
                isSelectionMode = selectedHistoriesIds.isNotEmpty(),
                selectedHistoriesIds = selectedHistoriesIds
            )
        }
    }
}