package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.network.models.Election
import java.lang.IllegalArgumentException

//COMPLETE: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(val app: Application, val election: Election): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(app, election) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }
}