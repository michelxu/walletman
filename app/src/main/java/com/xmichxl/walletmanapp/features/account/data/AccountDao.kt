package com.xmichxl.walletmanapp.features.account.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Int): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account): Long // Returns new ID created

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(account: Account)
}