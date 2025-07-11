package com.tedd.todo_project.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.usecase.DeleteTodoUseCase
import com.tedd.todo_project.domain.usecase.GetTodosUseCase
import com.tedd.todo_project.domain.usecase.InsertTodoUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodoUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodosUseCase
import com.tedd.todo_project.navigation.Route
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val insertTodoUseCase: InsertTodoUseCase,
    private val updateTodosUseCase: UpdateTodosUseCase,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTodosUseCase()
                .distinctUntilChanged()
                .collectLatest { todos ->
                    val filteredAndSortedTodos = todos
                        .filter { !it.isCompleted }
                        .sortedBy { it.position } // position으로 정렬
                        .toImmutableList()
                    _uiState.update { it.copy(todos = filteredAndSortedTodos) }
                }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is MainScreenEvent.UpdateTodoInput -> {
                    _uiState.update { it.copy(todoInput = event.newInput) }
                }

                is MainScreenEvent.AddTodo -> addTodo()

                is MainScreenEvent.ToggleTodoComplete -> toggleTodoComplete(event.todo)

                is MainScreenEvent.DeleteTodo -> deleteTodo(event.todo)

                is MainScreenEvent.OnNavigate -> navigator.navigate(Route.History)

                is MainScreenEvent.OnTodoSelectionClick -> onTodoSelectionClick(event.todoId)

                is MainScreenEvent.ClearSelection -> clearSelection()

                is MainScreenEvent.DeleteSelectedTodos -> deleteSelectedTodos()

                is MainScreenEvent.OnMoveTodo -> onMoveTodo(event.fromIndex, event.toIndex)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun addTodo() {
        viewModelScope.launch {
            val currentInput = _uiState.value.todoInput
            if (currentInput.isNotBlank()) {
                val newTodo = Todo(
                    work = currentInput,
                    isCompleted = false,
                    addedTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    completedTime = null,
                    position = _uiState.value.todos.size // 새 항목은 마지막 position
                )
                insertTodoUseCase(newTodo)
                _uiState.update { it.copy(todoInput = "") }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun toggleTodoComplete(todo: Todo) {
        viewModelScope.launch {
            val isCompleted = todo.isCompleted
            val completedTime = if (isCompleted) Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()) else null
            updateTodoUseCase(todo.id, isCompleted, completedTime)
        }
    }

    private fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            deleteTodoUseCase(todo)
        }
    }

    private fun onTodoSelectionClick(todoId: Long) {
        _uiState.update { currentState ->
            val newSelectedTodoIds = if (currentState.selectedTodoIds.contains(todoId)) {
                currentState.selectedTodoIds.minus(todoId)
            } else {
                currentState.selectedTodoIds.plus(todoId)
            }
            currentState.copy(
                isSelectionMode = newSelectedTodoIds.isNotEmpty(),
                selectedTodoIds = newSelectedTodoIds
            )
        }
    }

    private fun clearSelection() {
        _uiState.update { it.copy(isSelectionMode = false, selectedTodoIds = emptySet()) }
    }

    private fun deleteSelectedTodos() {
        viewModelScope.launch {
            _uiState.value.selectedTodoIds.forEach { todoId ->
                _uiState.value.todos.find { it.id == todoId }?.let { todo ->
                    deleteTodoUseCase(todo)
                }
            }
            clearSelection()
        }
    }

    private fun onMoveTodo(fromIndex: Int, toIndex: Int) {
        _uiState.update { currentState ->
            val mutableTodos = currentState.todos.toMutableList()
            val movedTodo = mutableTodos.removeAt(fromIndex)
            mutableTodos.add(toIndex, movedTodo)

            // position 업데이트
            val updatedTodos = mutableTodos.mapIndexed { index, todo ->
                todo.copy(position = index)
            }

            viewModelScope.launch {
                updateTodosUseCase(updatedTodos)
            }
            currentState.copy(todos = updatedTodos.toImmutableList())
        }
    }
}