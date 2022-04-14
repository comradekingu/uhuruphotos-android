package com.savvasdalkitsis.librephotos.app

import android.app.Application
import android.webkit.WebView
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import com.savvasdalkitsis.librephotos.albums.worker.AlbumWorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App :
    Application(),
    Configuration.Provider,
    ImageLoaderFactory {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var albumWorkScheduler: AlbumWorkScheduler
    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var logAdapter: LogAdapter

    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true)
        Logger.addLogAdapter(logAdapter)
        albumWorkScheduler.scheduleAlbumsRefreshPeriodic()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun newImageLoader() = imageLoader
}