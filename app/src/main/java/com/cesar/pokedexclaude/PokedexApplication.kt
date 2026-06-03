package com.cesar.pokedexclaude

import android.app.Application
import com.cesar.pokedexclaude.core.network.di.networkModule
import com.cesar.pokedexclaude.data.di.dataModule
import com.cesar.pokedexclaude.di.viewModelModule
import com.cesar.pokedexclaude.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Custom Application class for the Pokedex app.
 * Initializes Koin dependency injection framework with all required modules.
 */
class PokedexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for dependency injection
        startKoin {
            // Enable Koin logging for debugging (set to ERROR in production)
            androidLogger(Level.ERROR)

            // Provide Android context to Koin
            androidContext(this@PokedexApplication)

            // Load all Koin modules
            modules(
                networkModule,      // Core network infrastructure
                dataModule,         // Data layer (repositories, API)
                domainModule,       // Domain use cases
                viewModelModule     // Presentation layer ViewModels
            )
        }
    }
}
