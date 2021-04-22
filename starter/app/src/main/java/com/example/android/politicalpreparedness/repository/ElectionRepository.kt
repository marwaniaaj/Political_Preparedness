package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

enum class ElectionApiStatus { LOADING, ERROR, DONE }

class ElectionRepository(private val database: ElectionDatabase) {

    /**
     * Get upcoming list of [Election]
     */
    val upcomingElections: LiveData<List<Election>> =
            Transformations.map(database.electionDao.getUpcomingElections()) {
                it
            }

    /**
     * Get saved list of [Election]
     */
    val savedElections: LiveData<List<Election>> =
            Transformations.map(database.electionDao.getSavedElections()) {
                it
            }

    /**
     * Elections API status
     */
    private val _electionApiStatus = MutableLiveData(ElectionApiStatus.DONE)
    val electionApiStatus: LiveData<ElectionApiStatus>
        get() = _electionApiStatus

    private var _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    /**
     * Refresh the upcoming elections stored in database
     */
    suspend fun refreshUpcomingElections() {

        withContext(Dispatchers.IO) {

            val isDatabaseDataAvailable = upcomingElections.value?.count() ?: 0 > 0
            if (!isDatabaseDataAvailable) {
                _electionApiStatus.postValue(ElectionApiStatus.LOADING)
            }

            try {
                val response = CivicsApi.retrofitService.getElections()
                database.electionDao.insertAll(*response.elections.toTypedArray())
                _electionApiStatus.postValue(ElectionApiStatus.DONE)
            }
            catch (e: Exception) {
                if (!isDatabaseDataAvailable) {
                    _electionApiStatus.postValue(ElectionApiStatus.ERROR)
                }
                //Timber.e(e.message.toString())
                Log.e(TAG, e.message.toString())
            }
        }
    }

    /**
     * Looks up information relevant to a voter based on the voter's registered address
     */
    suspend fun getVoterInfo(address: String, electionId: Int) {

        withContext(Dispatchers.IO) {
            try {
                val response = CivicsApi.retrofitService.getVoterInfo(address, electionId)
                _voterInfo.postValue(response)
            }
            catch (e: Exception) {
                //Timber.e(e.message.toString())
                Log.e(TAG, e.message.toString())
            }
        }
    }

    /**
     * Update current saved state for given [Election]
     * @param electionId ID value for given election
     * @param saved      boolean value determines saved statue
     */
    suspend fun updateElection(electionId: Int, saved: Boolean) {

        withContext(Dispatchers.IO) {
            try {
                database.electionDao.updateSavedElection(electionId, saved)
            }
            catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }
    }

    companion object {
        val TAG = ElectionRepository::class.java.simpleName
    }
}