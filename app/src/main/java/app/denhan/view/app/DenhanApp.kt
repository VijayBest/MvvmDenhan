package app.certa.views.app

import android.app.Application
import app.denhan.module.networkModule
import app.denhan.module.viewModelModule
import org.koin.android.ext.android.startKoin


class DenhanApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = listOf(networkModule, viewModelModule)
        startKoin(this, modules)
    }

}

