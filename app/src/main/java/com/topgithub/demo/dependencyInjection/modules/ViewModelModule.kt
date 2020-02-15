package com.topgithub.demo.dependencyInjection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


import com.topgithub.demo.viewmodel.GithubViewModel
import com.topgithub.demo.viewmodel.ViewModelsFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelsFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GithubViewModel::class)
    internal abstract fun getGithubRepoViewModel(viewModel: GithubViewModel): ViewModel
}