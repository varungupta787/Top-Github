package com.topgithub.demo.data.network


import com.topgithub.demo.models.RepositoryItem

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/developers")
    fun getRepositoryList(@Query("language") language:String,
                          @Query("since") since:String): Single<List<RepositoryItem>>
}
