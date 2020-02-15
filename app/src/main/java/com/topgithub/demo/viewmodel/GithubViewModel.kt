package com.topgithub.demo.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.topgithub.demo.data.repository.RepoRepository
import com.topgithub.demo.dependencyInjection.modules.RxSchedulerModule
import com.topgithub.demo.models.RepositoryItem
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject
import javax.inject.Named


open class GithubViewModel @Inject
constructor(internal var repoRepository: RepoRepository,
            @Named(RxSchedulerModule.IO) private val schedulerIO:Scheduler,
            @Named(RxSchedulerModule.ANDROID_MAIN_THREAD) private val mainScheduler:Scheduler) : ViewModel() {
    var repoList = MutableLiveData<List<RepositoryItem>>()
    var repoError = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()
    var disposable = CompositeDisposable()

    fun getRepositoryListData() {
        loading.value = true
        disposable.add(
            repoRepository.getRepositoryList().subscribeOn(schedulerIO).observeOn(
               mainScheduler
            ).subscribeWith(object : DisposableSingleObserver<List<RepositoryItem>>() {
                override fun onSuccess(value: List<RepositoryItem>?) {
                    repoList.value = value
                    repoError.value = false
                    loading.value = false
                }

                override fun onError(e: Throwable?) {
                    repoError.value = true
                    loading.value = false
                }
            })
        )
    }

    val githubRepositoryList: LiveData<List<RepositoryItem>>
        get() = repoList

    val githubRepositoryError: LiveData<Boolean>
        get() = repoError

    val loadingState: LiveData<Boolean>
        get() = loading


}
