package com.app.paypay.exchange.di

import android.content.Context
import androidx.room.Room
import com.app.paypay.database.AppDatabase
import com.app.paypay.exchange.network.NetworkConfig
import com.app.paypay.exchange.repository.currencylayer.CurrencyLayerServiceRepository
import com.app.paypay.exchange.repository.currencylayer.source.app.AssetSource
import com.app.paypay.exchange.repository.currencylayer.source.local.LocalSource
import com.app.paypay.exchange.repository.currencylayer.source.remote.RemoteSource
import com.app.paypay.exchange.usecases.CachedCurrenciesUseCase
import com.app.paypay.exchange.usecases.CurrencyServiceUseCase
import com.app.paypay.exchange.usecases.GetCurrenciesViaAssetsUseCase
import com.app.paypay.exchange.vm.ExchangeActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * The dependency injection service specific to currency exchange feature.
 */
object DI {
    private var init = false

    /**
     * This initializes Koin DI for the whole application.
     * This must be only called once.
     */
    fun start(context: Context) {

        if (init)
            return

        val networkModule = module {
            single {
                NetworkConfig.getRetrofit()
            }
            single { NetworkConfig.getCurrencyLayerWebService(get()) }

        }

        val exchangeCurrencyModule = module {
            single { Room.databaseBuilder(get(), AppDatabase::class.java, "appDB").build() }
            factory { RemoteSource(get()) }
            factory { LocalSource(get<AppDatabase>().currencyDao()) }
            factory { AssetSource(androidContext()) }
            factory { CurrencyLayerServiceRepository(get(), get(), get()) }
            single { CurrencyServiceUseCase(get()) }
            single { CachedCurrenciesUseCase(get()) }
            single { GetCurrenciesViaAssetsUseCase(get()) }
            viewModel { ExchangeActivityViewModel(get(), get(), get()) }
        }



        startKoin {
            androidLogger()
            androidContext(context)
            modules(exchangeCurrencyModule, networkModule)
        }

        init = true


    }
}