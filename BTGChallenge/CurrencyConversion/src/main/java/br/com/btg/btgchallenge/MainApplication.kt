package br.com.btg.btgchallenge

import android.app.Application
import br.com.btg.btgchallenge.data.di.apiModule
import br.com.btg.btgchallenge.data.di.viewModelModule
import br.com.btg.btgchallenge.network.api.GetTrueTime
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            GetTrueTime.getTrueTime()
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(listOf(viewModelModule, apiModule))
        }
    }
}