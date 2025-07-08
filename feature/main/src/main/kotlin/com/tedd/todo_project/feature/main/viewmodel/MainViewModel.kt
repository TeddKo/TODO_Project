package com.tedd.todo_project.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.usecase.DeleteTodoUseCase
import com.tedd.todo_project.domain.usecase.GetTodosUseCase
import com.tedd.todo_project.domain.usecase.InsertTodoUseCase
import com.tedd.todo_project.domain.usecase.UpdateTodoUseCase
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
                        .sortedByDescending { it.addedTime }
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
                    completedTime = null
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


}

