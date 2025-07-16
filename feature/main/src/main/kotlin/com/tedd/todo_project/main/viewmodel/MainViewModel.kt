package com.tedd.todo_project.main.viewmodel

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.usecase.DeleteTodoUseCase
import com.tedd.todo_project.domain.usecase.DeleteTodosUseCase
import com.tedd.todo_project.domain.usecase.GetTodosUseCase
import com.tedd.todo_project.domain.usecase.InsertTodoUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodoUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodoWorkUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodosUseCase
import com.tedd.todo_project.navigator.Navigator
import com.tedd.todo_project.route.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val updateTodoWorkUseCase: UpdateTodoWorkUseCase,
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
                .collectLatest { todos ->
                    val filteredAndSortedTodos = todos
                        .filter { !it.isCompleted }
                        .toImmutableList()
                    _uiState.update {
                        it.copy(
                            todos = filteredAndSortedTodos,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onIntent(intent: MainScreenIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainScreenIntent.OnUpdateTodoInput -> {
                    _uiState.update { it.copy(todoInput = intent.newInput) }
                }

                is MainScreenIntent.OnAddTodo -> addTodo()

                is MainScreenIntent.OnToggleTodoComplete -> toggleTodoComplete(intent.todo)

                is MainScreenIntent.OnUpdateTodoWork -> updateTodoWork(newInput = intent.newInput)

                is MainScreenIntent.OnDeleteTodo -> deleteTodo(intent.todo)

                is MainScreenIntent.OnNavigate -> navigator.navigate(Route.History)

                is MainScreenIntent.OnSelectTodo -> selectTodo(intent.todoId)

                is MainScreenIntent.OnEditTodo -> editTodo()

                is MainScreenIntent.OnEditCancel -> clearSelection()

                is MainScreenIntent.OnClearSelection -> clearSelection()

                is MainScreenIntent.OnDeleteSelectedTodos -> deleteSelectedTodos()

                is MainScreenIntent.OnMoveTodo -> moveTodo(intent.fromIndex, intent.toIndex)

                is MainScreenIntent.OnUpdateTodoIndex -> updateTodoIndex()

                is MainScreenIntent.OnSelectAllTodos -> selectAllTodos()

                is MainScreenIntent.OnSwipeStateChange -> _uiState.update { it.copy(swipingTodoId = if (intent.isSwiping) intent.todoId else null) }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun addTodo() {
        val currentInput = _uiState.value.todoInput
        if (currentInput.text.isNotBlank()) {
            val newPosition = (_uiState.value.todos.maxOfOrNull { it.position } ?: -1) + 1
            val newTodo = Todo(
                work = currentInput.text,
                isCompleted = false,
                addedTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                position = newPosition
            )
            insertTodoUseCase(newTodo)
            _uiState.update { it.copy(todoInput = TextFieldValue("")) }
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun toggleTodoComplete(todo: Todo) {
        val isCompleted = todo.isCompleted
        val completedTime = if (isCompleted) Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()) else null
        updateTodoUseCase(todo.id, isCompleted, completedTime)
        if (_uiState.value.swipingTodoId == todo.id) {
            _uiState.update { it.copy(swipingTodoId = null) }
        }
    }

    private suspend fun deleteTodo(todo: Todo) {
        deleteTodoUseCase(todo)
        if (_uiState.value.swipingTodoId == todo.id) {
            _uiState.update { it.copy(swipingTodoId = null) }
        }
    }

    private fun selectTodo(todoId: Long) {
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

    private fun editTodo() {
        val selectedTodo = _uiState
            .value
            .selectedTodoIds.firstNotNullOf { todoId ->
                _uiState.value.todos.find { it.id == todoId }
            }

        _uiState.update {
            it.copy(
                isUpdatableWork = true,
                todoInput = TextFieldValue(
                    text = selectedTodo.work,
                    selection = TextRange(selectedTodo.work.length)
                )
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun updateTodoWork(newInput: String) {
        val selectedTodoId = _uiState.value.selectedTodoIds.first()
        val updateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val newPosition = (_uiState.value.todos.maxOfOrNull { it.position } ?: -1) + 1

        updateTodoWorkUseCase(
            id = selectedTodoId,
            work = newInput,
            updatedTime = updateTime,
            position = newPosition
        )
        clearSelection()
    }

    private fun clearSelection() {
        _uiState.update {
            it.copy(
                isSelectionMode = false,
                selectedTodoIds = emptySet(),
                todoInput = TextFieldValue(""),
                isUpdatableWork = false
            )
        }
    }

    private suspend fun deleteSelectedTodos() {
        val todosToDelete = _uiState
            .value
            .selectedTodoIds
            .mapNotNull { todoId ->
                _uiState.value.todos.find { it.id == todoId }
            }

        if (todosToDelete.isNotEmpty()) {
            deleteTodosUseCase(todosToDelete)
        }

        clearSelection()
    }

    private fun moveTodo(fromIndex: Int, toIndex: Int) {
        _uiState.update { currentState ->
            val mutableTodos = currentState.todos.toMutableList()
            val movedTodo = mutableTodos.removeAt(fromIndex)
            mutableTodos.add(toIndex, movedTodo)
            currentState.copy(todos = mutableTodos.toImmutableList())
        }
    }

    private suspend fun updateTodoIndex() {
        val currentTodos = _uiState.value.todos
        if (currentTodos.isNotEmpty()) {
            val size = currentTodos.size
            val updateTodos = withContext(Dispatchers.Default) {
                currentTodos.mapIndexed { index, todo ->
                    todo.copy(position = size - 1 - index)
                }
            }
            if (currentTodos != updateTodos) {
                _uiState.update { it.copy(isLoading = true) }
            }
            updateTodosUseCase(updateTodos)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun selectAllTodos() {
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