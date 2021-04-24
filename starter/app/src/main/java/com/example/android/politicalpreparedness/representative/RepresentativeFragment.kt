package com.example.android.politicalpreparedness.representative

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Binding
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Adapter
        val representativeAdapter = RepresentativeListAdapter()
        binding.representativeRecyclerView.adapter = representativeAdapter

        // Observe representatives
        viewModel.representative.observe(viewLifecycleOwner, Observer { representative ->
            representative?.let {
                representativeAdapter.submitList(it)
            }
        })

        // Search button click listener
        binding.buttonSearch.setOnClickListener {
            hideKeyboard()
            viewModel.getRepresentativesByAddress()
        }

        // Location button click listener
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }

        return binding.root
    }

    /**
     * Handling location permission result to get location on permission granted
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_LOCATION_PERMISSION &&
                grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        }
        else {
            showPermissionSnackBar()
        }
    }

    /**
     * Check if permission is already granted
     * If not, request location permission, and show snack bar
     * @return boolean value indicates if location permission is granted(true) or not(false)
     */
    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestLocationPermission()
            showPermissionSnackBar()
            false
        }
    }

    /**
     * Check if location permission is granted
     * @return boolean value indicates if location permission is granted(true) or not(false)
     */
    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get location from LocationServices
     * and then get Representative using retrieved location
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkLocationPermissions()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val location = task.result
                            if (location != null) viewModel.getRepresentativesByAddress(geoCodeLocation(location))
                            else {
                                showLocationNotAvailableSnackBar()
                            }
                        }
                    }
        }
    }

    /**
     * Request location permissions
     */
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION)
    }

    /**
     * Change the lat/long location to a human readable street address
     * @param location to be changed
     * @return location's readable address
     */
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    /**
     * Hides keyboard from the screen
     */
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    private fun showPermissionSnackBar() = Snackbar.make(view!!, getString(R.string.permission_denied), Snackbar.LENGTH_SHORT).show()
    private fun showLocationNotAvailableSnackBar() = Snackbar.make(view!!, getString(R.string.location_not_available), Snackbar.LENGTH_SHORT).show()

    //COMPLETE: Add Constant for Location request
    //COMPLETE: Declare ViewModel
    //COMPLETE: Establish bindings
    //COMPLETE: Define and assign Representative adapter
    //COMPLETE: Populate Representative adapter
    //COMPLETE: Establish button listeners for field and location search
    //COMPLETE: Handle location permission result to get location on permission granted
    //COMPLETE: Check if permission is already granted and return (true = granted, false = denied/other)
    //COMPLETE: Request Location permissions
    //COMPLETE: Get location from LocationServices
    //COMPLETE: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
}