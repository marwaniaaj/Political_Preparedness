package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionApiStatus
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application): ViewModel() {

    private val database = getInstance(application)
    private val repository = ElectionRepository(database)

    private val _navigateToSelectedElection = MutableLiveData<Election>()

    /**
     * Navigation to selected [Election] LiveData
     */
    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection

    init {
        // Launch a coroutine
        viewModelScope.launch {
            // Refresh the elections using the repository.
            repository.refreshUpcomingElections()
        }
    }

    /**
     * Upcoming election LiveData
     */
    var upcomingElections = repository.upcomingElections

    /**
     * Saved election LiveData
     */
    var savedElection = repository.savedElections

    /**
     * Election API status LiveData
     */
    val status = repository.electionApiStatus

    /**
     * Called when election is selected
     */
    fun onSelectedElectionClicked(election: Election) {
        _navigateToSelectedElection.value = election
    }

    /**
     * Called when navigation to selected [Election] is finished
     */
    fun onSelectedElectionNavigated() {
        _navigateToSelectedElection.value = null
    }

    //COMPLETE: Construct ViewModel and provide election datasource
    //COMPLETE: Create live data val for upcoming elections
    //COMPLETE: Create live data val for saved elections
    //COMPLETE: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    //COMPLETE: Create functions to navigate to saved or upcoming election voter info
}