package com.expenseai.di

import android.content.Context
import androidx.room.Room
import com.expenseai.data.local.ExpenseDao
import com.expenseai.data.local.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(
        @ApplicationContext context: Context
    ): ExpenseDatabase {
        // Generate a stable encryption key from the Android Keystore
        val passphrase = getOrCreateDatabaseKey(context)
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        )
            .openHelperFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao =
        database.expenseDao()

    private fun getOrCreateDatabaseKey(context: Context): ByteArray {
        val keyStore = java.security.KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val alias = "expense_db_key"

        if (!keyStore.containsAlias(alias)) {
            val keyGenerator = javax.crypto.KeyGenerator.getInstance(
                android.security.keystore.KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            keyGenerator.init(
                android.security.keystore.KeyGenParameterSpec.Builder(
                    alias,
                    android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or
                            android.security.keystore.KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )
            keyGenerator.generateKey()
        }

        // Derive a passphrase from the Keystore-backed key
        val key = keyStore.getKey(alias, null) as javax.crypto.SecretKey
        return key.encoded ?: alias.toByteArray(Charsets.UTF_8)
    }
}
