package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_voter_info.*

class VoterInfoFragment : Fragment() {

//    private val viewModel: VoterInfoViewModel by lazy {
//        ViewModelProvider(this, VoterInfoViewModelFactory(requireActivity().application)).get(VoterInfoViewModel::class.java)
//    }

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Get selected Election object from the Safe Args.
        val election = VoterInfoFragmentArgs.fromBundle(requireArguments()).selectedElection

        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application, election)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.viewModel = viewModel

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */

        viewModel.votingLocationUrl.observe(viewLifecycleOwner, Observer {
            it?.let { url -> loadUrl(url) }
            if (it == null) Snackbar.make(view!!, getString(R.string.url_not_available), Snackbar.LENGTH_SHORT).show()
        })

        viewModel.ballotInfoUrl.observe(viewLifecycleOwner, Observer {
            it?.let { url -> loadUrl(url) }
            if (it == null) Snackbar.make(view!!, getString(R.string.url_not_available), Snackbar.LENGTH_SHORT).show()
        })

        viewModel.electionSavedEvent.observe(viewLifecycleOwner, Observer { saved ->
            binding.followButton.text = getString(when (saved) {
                false -> R.string.follow_election
                else -> R.string.unfollow_election
            })
        })

        return binding.root
    }

    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    companion object {
        val TAG = VoterInfoFragment::class.java.simpleName
    }

    //COMPLETE: Add ViewModel values and create ViewModel
    //COMPLETE: Add binding values
    //TODO: Populate voter info -- hide views without provided data.
    //COMPLETE: Handle loading of URLs
    //COMPLETE: Handle save button UI state
    //COMPETE: cont'd Handle save button clicks
    //COMPLETE: Create method to load URL intents
}
