package com.topgithub.demo.dependencyInjection.modules

import androidx.lifecycle.ViewModel


import com.topgithub.demo.viewmodel.GithubViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(GithubViewModel::class)
    internal abstract fun getGithubRepoViewModel(viewModel: GithubViewModel): ViewModel
}