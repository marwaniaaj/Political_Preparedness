package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    /**
     * Insert elections in the database. If the election already exists, ignore it.
     *
     * @param elections the election to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg elections: Election)

    /**
     * Update the saved status of an election
     *
     * @param electionId    id of the election
     * @param saved         status to be updated
     */
    @Query("UPDATE election_table SET saved = :saved WHERE id = :electionId")
    suspend fun updateSavedElection(electionId: Int, saved: Boolean): Int

    /**
     * Selects and returns all elections,
     * sorted by election date in descending order.
     * @return all elections
     */
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getUpcomingElections(): LiveData<List<Election>>

    /**
     * Selects and returns saved elections,
     * sorted by election date in descending order.
     * @returns saved elections
     */
    @Query("SELECT * FROM election_table WHERE saved = 1 ORDER BY electionDay DESC")
    fun getSavedElections(): LiveData<List<Election>>

    /**
     * Selects and returns election by supplied id, which is our key.
     * @param electionId election id to match
     * @return the election with electionId
     */
    @Query("SELECT * FROM ELECTION_TABLE WHERE id = :electionId")
    fun getElection(electionId: Int): LiveData<Election>

    /**
     * Delete all elections
     */
    @Delete
    suspend fun deleteElection(election: Election)

    /**
     * Deletes all values from the table.
     */
    @Query("DELETE FROM election_table")
    suspend fun clear()
}