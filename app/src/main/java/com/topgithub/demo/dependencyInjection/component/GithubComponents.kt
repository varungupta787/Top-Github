package com.topgithub.demo.dependencyInjection.component


import com.topgithub.demo.dependencyInjection.modules.NetworkModule
import com.topgithub.demo.dependencyInjection.modules.ViewModelModule
import com.topgithub.demo.dependencyInjection.scope.ApplicationScope
import com.topgithub.demo.ui.activity.RepositoryListActivity

import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, ViewModelModule::class])
interface GithubComponents {
    fun injectRepositoryListActivity(activity: RepositoryListActivity)
}
