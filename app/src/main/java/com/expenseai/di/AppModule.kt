package com.expenseai.di

import android.content.Context
import androidx.room.Room
import com.expenseai.data.local.ExpenseDao
import com.expenseai.data.local.ExpenseDatabase
import com.expenseai.data.local.PendingExpenseDao
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
        @ApplicationContext context: Context,
        encryptedPreferences: com.expenseai.security.EncryptedPreferences
    ): ExpenseDatabase {
        val passphrase = getOrCreateDbPassphrase(context, encryptedPreferences)
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            DB_NAME
        )
            .openHelperFactory(factory)
            .addMigrations(ExpenseDatabase.MIGRATION_1_2, ExpenseDatabase.MIGRATION_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun provideFireModelDao(database: ExpenseDatabase): com.expenseai.data.local.FireModelDao =
        database.fireModelDao()


    @Provides
    @Singleton
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao =
        database.expenseDao()

    @Provides
    @Singleton
    fun providePendingExpenseDao(database: ExpenseDatabase): PendingExpenseDao =
        database.pendingExpenseDao()

    @Provides
    @Singleton
    fun provideFireEngine(): com.expenseai.domain.fire.FireEngine =
        com.expenseai.domain.fire.FireEngine()

    /**
     * Returns the SQLCipher passphrase: a random 32-byte value generated once
     * and stored in EncryptedSharedPreferences (Keystore-protected).
     *
     * The previous implementation derived the passphrase from an AndroidKeyStore
     * AES key's `encoded` bytes — but Keystore keys are non-extractable, so
     * `encoded` was always null and every install silently fell back to the
     * static string "expense_db_key". Databases created under that scheme are
     * rekeyed to the new random passphrase on first open.
     */
    private fun getOrCreateDbPassphrase(
        context: Context,
        encryptedPreferences: com.expenseai.security.EncryptedPreferences
    ): ByteArray {
        encryptedPreferences.getString(KEY_DB_PASSPHRASE)?.let {
            return it.toByteArray(Charsets.UTF_8)
        }

        val passphrase = ByteArray(32)
            .also { java.security.SecureRandom().nextBytes(it) }
            .joinToString("") { "%02x".format(it) }

        val dbFile = context.getDatabasePath(DB_NAME)
        if (dbFile.exists()) {
            net.sqlcipher.database.SQLiteDatabase.loadLibs(context)
            val db = net.sqlcipher.database.SQLiteDatabase.openDatabase(
                dbFile.absolutePath,
                LEGACY_PASSPHRASE,
                null,
                net.sqlcipher.database.SQLiteDatabase.OPEN_READWRITE
            )
            try {
                db.changePassword(passphrase)
            } finally {
                db.close()
            }
        }

        // Stored only after a successful rekey so a failed migration retries next launch
        encryptedPreferences.putString(KEY_DB_PASSPHRASE, passphrase)
        return passphrase.toByteArray(Charsets.UTF_8)
    }

    private const val DB_NAME = "expense_db"
    private const val KEY_DB_PASSPHRASE = "db_passphrase"

    // The passphrase every install effectively used under the old Keystore-derived scheme
    private const val LEGACY_PASSPHRASE = "expense_db_key"
}
