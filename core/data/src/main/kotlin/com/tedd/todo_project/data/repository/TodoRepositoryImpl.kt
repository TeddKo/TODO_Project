package com.tedd.todo_project.data.repository

import com.tedd.todo_project.data.model.toDomain
import com.tedd.todo_project.data.model.toEntity
import com.tedd.todo_project.database.TodoDatabase
import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.repository.TodoRepository
import com.tedd.todo_project.security.CryptoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDatabase: TodoDatabase,
    private val cryptoManager: CryptoManager
) : TodoRepository {

    private val todoDao = todoDatabase.todoDao()

    override fun getTodos(): Flow<List<Todo>> {
        return todoDao.getAllTodos().map { list ->
            list.map { entity ->
                val decryptedWork = cryptoManager.decrypt(entity.work)
                entity.toDomain().copy(work = decryptedWork)
            }
        }.flowOn(Dispatchers.Default)
    }

    override suspend fun getTodoById(id: Long): Todo? = withContext(Dispatchers.Default) {
        val todoEntity = todoDao.getTodoById(id) ?: return@withContext null
        val decryptedWork = cryptoManager.decrypt(todoEntity.work)
        todoEntity.toDomain().copy(work = decryptedWork)
    }

    override suspend fun insertTodo(todo: Todo) = withContext(Dispatchers.Default) {
        val encryptedWork = cryptoManager.encrypt(todo.work)
        todoDao.insertTodo(todo.copy(work = encryptedWork).toEntity())
    }

    override suspend fun updateTodoCompletion(
        id: Long,
        isCompleted: Boolean,
        completedTime: LocalDateTime?
    ) {
        todoDao.updateTodoCompletion(id, isCompleted, completedTime)
    }

    override suspend fun updateTodoWork(
        id: Long,
        work: String,
        updatedTime: LocalDateTime?
    ) = withContext(Dispatchers.Default) {
        val encryptedWork = cryptoManager.encrypt(work)
        todoDao.updateTodoWork(id, encryptedWork, updatedTime)
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo = todo.toEntity())
    }

    override suspend fun deleteTodos(todos: List<Todo>) {
        todoDao.deleteTodos(todos = todos.map { it.toEntity() })
    }

    override suspend fun updateTodos(todos: List<Todo>) = withContext(Dispatchers.Default) {
        todoDao.updateTodos(todos.map {
            val encryptedWork = cryptoManager.encrypt(it.work)
            it.copy(work = encryptedWork).toEntity()
        })
    }
}
