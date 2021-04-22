package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.Constants
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

// COMPLETE: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter())
        .add(ElectionAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(Constants.BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    /**
     * Get Elections
     * @return [ElectionResponse] object containing JSON result
     */
    @GET(Constants.ELECTION_QUERY)
    suspend fun getElections(): ElectionResponse

    /**
     * Get Voter info
     * @param address       The registered address of the voter to look up.
     * @param election_id   The unique ID of the election to look up
     * @return [VoterInfoResponse] object containing JSON result
     */
    @GET(Constants.VOTER_INFO_QUERY)
    suspend fun getVoterInfo(
            @Query("address") address: String,
            @Query("electionId") election_id: Int
    ): VoterInfoResponse

    //TODO: Add representatives API Call
}

/**
 * Google Civic api access
 */
object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}