package com.topgithub.demo.dependencyInjection.modules

import com.topgithub.demo.dependencyInjection.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named


/**
 * Inject RxJava [Scheduler] so that we can replace the [Scheduler] when testing.
 */
@Module
open class RxSchedulerModule {
    companion object {
        const val ANDROID_MAIN_THREAD = "androidMainThread"
        const val IO = "io"
        const val COMPUTATION = "computation"
        const val THROTTLE = "throttle"
        const val INTERVAL = "interval"
    }

    @Provides
    @ApplicationScope
    @Named(ANDROID_MAIN_THREAD)
    open fun provideAndroidScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @ApplicationScope
    @Named(IO)
    open fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }


}