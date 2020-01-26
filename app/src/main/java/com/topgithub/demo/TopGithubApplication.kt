package com.topgithub.demo

import android.app.Application
import android.content.Context
import com.topgithub.demo.cache.ImageLoader


class TopGithubApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {

        fun getAppContext():Context {
            return context
        }

        fun getImageLoader() : ImageLoader {
            return ImageLoader(getAppContext())
        }

        lateinit var context: Context
    }
}
