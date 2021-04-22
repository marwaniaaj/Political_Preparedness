package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionApiStatus

@BindingAdapter("upcomingElectionsList")
fun bindUpcomingElectionsRecycler(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("savedElectionsList")
fun bindSavedElectionsRecycler(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("electionApiStatus")
fun bindElectionApiStatus(progressBar: ProgressBar, status: ElectionApiStatus?) {
    progressBar.visibility = when (status) {
        ElectionApiStatus.LOADING -> View.VISIBLE
        else -> View.GONE
    }
}