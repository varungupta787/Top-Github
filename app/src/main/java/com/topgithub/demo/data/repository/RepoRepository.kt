package com.topgithub.demo.data.repository

import com.topgithub.demo.data.network.ApiService
import com.topgithub.demo.models.RepositoryItem
import io.reactivex.Single
import javax.inject.Inject

open class RepoRepository @Inject
constructor(val apiService: ApiService) {

    open fun getRepositoryList(): Single<List<RepositoryItem>> {
        return apiService.getRepositoryList("java", "weekely")
    }
}
