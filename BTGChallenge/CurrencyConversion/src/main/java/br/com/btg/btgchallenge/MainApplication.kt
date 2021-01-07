package br.com.btg.btgchallenge

import android.app.Application
import br.com.btg.btgchallenge.data.di.apiModule
import br.com.btg.btgchallenge.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(listOf(viewModelModule, apiModule))
        }
    }
}