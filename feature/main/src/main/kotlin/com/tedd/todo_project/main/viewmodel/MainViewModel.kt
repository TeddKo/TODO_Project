package com.tedd.todo_project.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.usecase.DeleteTodoUseCase
import com.tedd.todo_project.domain.usecase.DeleteTodosUseCase
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
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
    private val deleteTodosUseCase: DeleteTodosUseCase,
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
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .distinctUntilChanged()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collectLatest { todos ->
                    val filteredAndSortedTodos = todos
                        .filter { !it.isCompleted }
                        .toImmutableList()
                    _uiState.update { it.copy(todos = filteredAndSortedTodos, isLoading = false) }
                }
        }
    }

    fun onIntent(intent: MainScreenIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainScreenIntent.UpdateTodoInput -> {
                    _uiState.update { it.copy(todoInput = intent.newInput) }
                }

                is MainScreenIntent.AddTodo -> addTodo()

                is MainScreenIntent.ToggleTodoComplete -> toggleTodoComplete(intent.todo)

                is MainScreenIntent.DeleteTodo -> deleteTodo(intent.todo)

                is MainScreenIntent.OnNavigate -> navigator.navigate(Route.History)

                is MainScreenIntent.OnTodoSelectionClick -> onTodoSelectionClick(intent.todoId)

                is MainScreenIntent.ClearSelection -> clearSelection()

                is MainScreenIntent.DeleteSelectedTodos -> deleteSelectedTodos()

                is MainScreenIntent.OnMoveTodo -> onMoveTodo(intent.fromIndex, intent.toIndex)

                is MainScreenIntent.OnUpdateTodos -> onUpdateTodos()

                is MainScreenIntent.OnSelectAllTodos -> onSelectAllTodos()

                is MainScreenIntent.OnSwipeStateChange -> _uiState.update { it.copy(swipingTodoId = if (intent.isSwiping) intent.todoId else null) }

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
                    position = _uiState.value.todos.size
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
            val todosToDelete = _uiState.value.selectedTodoIds.mapNotNull { todoId ->
                _uiState.value.todos.find { it.id == todoId }
            }

            if (todosToDelete.isNotEmpty()) {
                deleteTodosUseCase(todosToDelete)
            }

            clearSelection()
        }
    }

    private fun onMoveTodo(fromIndex: Int, toIndex: Int) {
        _uiState.update { currentState ->
            val mutableTodos = currentState.todos.toMutableList()
            val movedTodo = mutableTodos.removeAt(fromIndex)
            mutableTodos.add(toIndex, movedTodo)
            currentState.copy(todos = mutableTodos.toImmutableList())
        }
    }

    private fun onUpdateTodos() {
        viewModelScope.launch {
            val currentTodos = _uiState.value.todos
            if (currentTodos.isNotEmpty()) {
                val size = currentTodos.size
                val updateTodos = currentTodos.mapIndexed { index, todo ->
                    todo.copy(position = size - 1 - index)
                }
                updateTodosUseCase(updateTodos)
            }
        }
    }

    private fun onSelectAllTodos() {
        _uiState.update { currentState ->
            val allTodoIds = currentState.todos.map { it.id }.toSet()
            currentState.copy(selectedTodoIds = allTodoIds)
            if (currentState.todos.isNotEmpty() && currentState.selectedTodoIds.size == allTodoIds.size) {
                currentState.copy(selectedTodoIds = emptySet())
            } else {
                currentState.copy(selectedTodoIds = allTodoIds)
            }
        }
    }
}