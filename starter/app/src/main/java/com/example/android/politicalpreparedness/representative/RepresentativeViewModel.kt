package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(): ViewModel() {

    private var _address = MutableLiveData<Address>()

    /**
     * Address LiveData
     */
    val address: LiveData<Address>
        get() = _address

    private var _representatives = MutableLiveData<List<Representative>>()

    /**
     * Representative LiveData
     */
    val representative: LiveData<List<Representative>>
        get() = _representatives

    init {
        // Initiate address value
        _address.value = Address("", "", "", "Alabama", "")
        _representatives.value = null
    }

    /**
     * Get list of [Representative]
     * @param address given address to fetch representatives
     */
    private fun getRepresentatives(address: String) =
            viewModelScope.launch {
                val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address)
                _representatives.value = offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }
            }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives
     */

    /**
     * Set list of [Representative] by calling [getRepresentatives]
     * @param address given address -if available- for fetching representatives
     */
    fun getRepresentativesByAddress(address: Address? = null) {
        address?.let {
            _address.value = address
            _address.value?.toFormattedString()?.let { getRepresentatives(it) }
        }
        if (address == null) {
            _address.value?.toFormattedString()?.let { getRepresentatives(it) }
        }
    }

    //COMPLETE: Establish live data for representatives and address
    //COMPLETE: Create function to fetch representatives from API from a provided address
    //COMPLETE: Create function get address from geo location
    //COMPLETE: Create function to get address from individual fields -> Using two way data binding
}
