package com.tedd.todo_project.database.di

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import androidx.room.Room
import com.tedd.todo_project.database.TodoDatabase
import com.tedd.todo_project.security.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import java.nio.charset.Charset
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val SQLCIPHER_KEY_PREF = "sqlcipher_key_pref"
    private const val SQLCIPHER_KEY_IV = "sqlcipher_key_iv"
    private const val SQLCIPHER_KEY_ENCRYPTED = "sqlcipher_key_encrypted"

    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context,
        cryptoManager: CryptoManager
    ): TodoDatabase {
        val sharedPrefs = context.getSharedPreferences(SQLCIPHER_KEY_PREF, Context.MODE_PRIVATE)

        val passphrase = if (sharedPrefs.contains(SQLCIPHER_KEY_ENCRYPTED)) {
            val iv = Base64.decode(sharedPrefs.getString(SQLCIPHER_KEY_IV, null), Base64.NO_WRAP)
            val encryptedKey =
                Base64.decode(sharedPrefs.getString(SQLCIPHER_KEY_ENCRYPTED, null), Base64.NO_WRAP)
            cryptoManager.decryptBytes(iv, encryptedKey)
        } else {
            val newKey = UUID.randomUUID().toString().toByteArray(Charset.forName("UTF-8"))
            val (iv, encryptedKey) = cryptoManager.encryptBytes(newKey)

            sharedPrefs.edit {
                putString(SQLCIPHER_KEY_IV, Base64.encodeToString(iv, Base64.NO_WRAP))
                    .putString(
                        SQLCIPHER_KEY_ENCRYPTED,
                        Base64.encodeToString(encryptedKey, Base64.NO_WRAP)
                    )
            }
            newKey
        }

        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo-database"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideTodoDao(database: TodoDatabase) = database.todoDao()
}
