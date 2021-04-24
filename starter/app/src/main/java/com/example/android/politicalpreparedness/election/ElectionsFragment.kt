package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this, ElectionsViewModelFactory(requireActivity().application)).get(ElectionsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Upcoming elections binding
        val upcomingAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onSelectedElectionClicked(election)
        })
        binding.upcomingElectionsRecycler.adapter = upcomingAdapter

        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer {
            it?.apply {
                upcomingAdapter.submitList(it)
            }
        })

        // Saved elections binding
        val savedAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onSelectedElectionClicked(election)
        })

        binding.savedElectionsRecycler.adapter = savedAdapter

        viewModel.savedElection.observe(viewLifecycleOwner, Observer {
            it?.apply {
                savedAdapter.submitList(it)
            }
        })

        viewModel.navigateToSelectedElection.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election))
                viewModel.onSelectedElectionNavigated()
            }
        })
        return binding.root
    }

    //COMPLETE: Declare ViewModel
    //COMPLETE: Add ViewModel values and create ViewModel
    //COMPLETE: Add binding values
    //COMPELET: Link elections to voter info
    //COMPLETE: Initiate recycler adapters
    //COMPLETE: Populate recycler adapters
    //COMPLETE: Refresh adapters when fragment loads
}