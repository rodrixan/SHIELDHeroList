package com.rodrixan.projects.technicaltests.shieldherolist.common

import android.app.Application
import com.rodrixan.projects.technicaltests.shieldherolist.common.di.heroModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class ShieldHeroesListApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ShieldHeroesListApp)
            modules(listOf(heroModule))
        }

        Timber.plant(Timber.DebugTree())
    }
}