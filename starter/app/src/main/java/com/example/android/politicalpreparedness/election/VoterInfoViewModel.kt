package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

class VoterInfoViewModel(application: Application, val election: Election) : ViewModel() {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionRepository(database)

    /**
     * Voter info LiveData
     */
    var voterInfo = repository.voterInfo

    private val _votingLocationUrl = MutableLiveData<String>()

    /**
     * Voting locations URL LiveDatas
     */
    val votingLocationUrl: LiveData<String>
        get() = _votingLocationUrl

    private val _ballotInfoUrl = MutableLiveData<String>()

    /**
     * Ballot Information LiveData
     */
    val ballotInfoUrl: LiveData<String>
        get() = _ballotInfoUrl

    private val _electionSavedEvent = MutableLiveData<Boolean>()

    /**
     * Boolean event value determines whether [Election] is saved locally in DB
     */
    val electionSavedEvent: LiveData<Boolean>
        get() = _electionSavedEvent


    init {
        // Get initial state for the button
        _electionSavedEvent.value = election.saved


        // Check if state is available
        if (election.division.state.isNotEmpty()) {
            val address = "${election.division.state}, ${election.division.country}"
            getVoterInfo(address, election.id)
        }
    }

    /**
     * Get voter's information from Service API
     * @param address       address of the division (state, country)
     * @param electionId    Election ID
     */
    private fun getVoterInfo(address: String, electionId: Int) =
            viewModelScope.launch {
                repository.getVoterInfo(address, electionId)
            }

    /**
     * Called when click on *Voting Locations*
     */
    fun onClickVotingLocations() {
        _votingLocationUrl.value = voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    /**
     * Called when click on *Ballot Information*
     */
    fun onClickBallotInfo() {
        _ballotInfoUrl.value = voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    /**
     * Called when *Follow Election* button is clicked
     */
    fun onClickFollowButton() {
        _electionSavedEvent.value = !_electionSavedEvent.value!!

        viewModelScope.launch {
            repository.updateElection(election.id, _electionSavedEvent.value!!)
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

    //COMPLETE: Add live data to hold voter info
    //COMPLETE: Add var and methods to populate voter info
    //COMPLETE: Add var and methods to support loading URLs
    //COMPLETE: Add var and methods to save and remove elections to local database
    //COMPLETE: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
}