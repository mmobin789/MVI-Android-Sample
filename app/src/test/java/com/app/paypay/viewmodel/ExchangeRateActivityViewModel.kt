package com.app.paypay.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.paypay.exchange.repositories.currencylayer.CurrencyLayerServiceRepository
import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.exchange.usecases.CachedCurrenciesUseCase
import com.app.paypay.exchange.usecases.CurrencyServiceUseCase
import com.app.paypay.exchange.usecases.GetCurrenciesViaAssetsUseCase
import com.app.paypay.exchange.vm.ExchangeActivityViewModel
import com.app.paypay.exchange.vm.ExchangeRateViewState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExchangeRateActivityViewModel {

    private companion object {
        const val dummyCurrencySource = "USD"
        const val invalidCurrencySource = " "
        const val dummySourceAmount = Double.MAX_VALUE
        const val invalidSourceAmount = " "
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val exchangeRates = mockk<List<ExchangeRate>>()

    private val assetCurrencies = mockk<List<String>>()

    private val currencyLayerServiceRepository = mockk<CurrencyLayerServiceRepository>()

    private val repositoryPayLoadSuccess =
        mockk<CurrencyLayerServiceRepository.Payload.ExchangeRates.Success>()

    private val repositoryPayLoadFail =
        mockk<CurrencyLayerServiceRepository.Payload.ExchangeRates.Fail>()

    private val repositoryPayLoadSuccessAssets =
        mockk<CurrencyLayerServiceRepository.Payload.CurrencyInfoViaAssets.Success>()

    private val repositoryPayLoadFailAssets =
        mockk<CurrencyLayerServiceRepository.Payload.CurrencyInfoViaAssets.Fail>()

    private val cachedCurrenciesUseCase = mockk<CachedCurrenciesUseCase>()

    private val currencyServiceUseCase = mockk<CurrencyServiceUseCase>()

    private val getCurrenciesViaAssetsUseCase = mockk<GetCurrenciesViaAssetsUseCase>()


    private val viewModel = ExchangeActivityViewModel(
        currencyServiceUseCase,
        cachedCurrenciesUseCase,
        getCurrenciesViaAssetsUseCase
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun getExchangeRateViewStateIdle() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it == ExchangeRateViewState.Idle) {
                state.removeObserver(stateObserver!!)
            }
        }

        state.observeForever(stateObserver)

        Assert.assertTrue(state.value == ExchangeRateViewState.Idle)

    }

    @Test
    fun getExchangeRateViewStateError() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Error) {
                state.removeObserver(stateObserver!!)
            }
        }

        coEvery {
            currencyServiceUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadFail

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadFail


        coEvery {
            currencyLayerServiceRepository.getCachedExchangeRates(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadFail

        every { repositoryPayLoadFail.error } returns null


        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(state.value is ExchangeRateViewState.Error)

    }

    @Test
    fun getExchangeRateViewStateGenericErrorInvalidCurrencySource() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.GenericError) {
                state.removeObserver(stateObserver!!)
            }
        }


        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = invalidCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(state.value is ExchangeRateViewState.GenericError)

    }

    @Test
    fun getExchangeRateViewStateGenericErrorInvalidCurrencyAmount() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.GenericError) {
                state.removeObserver(stateObserver!!)
            }
        }


        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = invalidSourceAmount
        )

        Assert.assertTrue(state.value is ExchangeRateViewState.GenericError)

    }

    @Test
    fun getCachedExchangeRateViewStateLoading() {

        var isLoading = false

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading) {
                isLoading = true
                state.removeObserver(stateObserver!!)
            }
        }


        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        coEvery {
            currencyLayerServiceRepository.getCachedExchangeRates(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess


        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates



        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(isLoading)

    }

    @Test
    fun getCachedExchangeRateViewStateLoaded() {

        var isLoading = true

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading.not()) {
                isLoading = false
                state.removeObserver(stateObserver!!)
            }
        }

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        coEvery {
            currencyLayerServiceRepository.getCachedExchangeRates(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates


        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertFalse(isLoading)

    }

    @Test
    fun getCachedExchangeRateViewState() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.ExchangeRates) {
                state.removeObserver(stateObserver!!)
            }
        }

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        coEvery {
            currencyLayerServiceRepository.getCachedExchangeRates(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess


        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates

        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(state.value is ExchangeRateViewState.ExchangeRates)

    }

    @Test
    fun getLiveExchangeRateViewStateLoading() {

        var isLoading = false

        var stateObserver: Observer<ExchangeRateViewState>? = null

        val state = viewModel.getViewState()

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading) {
                isLoading = true
                state.removeObserver(stateObserver!!)
            }
        }
        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadFail


        coEvery {
            currencyServiceUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(isLoading)

    }

    @Test
    fun getLiveExchangeRateViewStateLoaded() {

        var isLoading = true

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading.not()) {
                isLoading = false
                state.removeObserver(stateObserver!!)

            }
        }
        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess

        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertFalse(isLoading)

    }

    @Test
    fun getLiveExchangeRateViewState() {

        val state = viewModel.getViewState()

        var stateObserver: Observer<ExchangeRateViewState>? = null

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.ExchangeRates) {
                state.removeObserver(stateObserver!!)
            }
        }

        coEvery {
            cachedCurrenciesUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadFail


        coEvery {
            currencyServiceUseCase(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess


        coEvery {
            currencyLayerServiceRepository.getLiveExchangeRates(
                dummyCurrencySource,
                dummySourceAmount
            )
        } returns repositoryPayLoadSuccess


        every { repositoryPayLoadSuccess.exchangeRates } returns exchangeRates

        state.observeForever(stateObserver)

        viewModel.getExchangeRates(
            sourceCurrency = dummyCurrencySource,
            sourceAmount = dummySourceAmount.toString()
        )

        Assert.assertTrue(state.value is ExchangeRateViewState.ExchangeRates)

    }

    @Test
    fun getAssetCurrenciesExchangeRateViewStateLoading() {

        var isLoading = false

        var stateObserver: Observer<ExchangeRateViewState>? = null

        val state = viewModel.getViewState()

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading) {
                isLoading = true
                state.removeObserver(stateObserver!!)
            }
        }
        every { repositoryPayLoadSuccessAssets.currencies } returns assetCurrencies

        coEvery {
            getCurrenciesViaAssetsUseCase()
        } returns repositoryPayLoadSuccessAssets


        state.observeForever(stateObserver)

        viewModel.getCurrenciesFromAssets()

        Assert.assertTrue(isLoading)

    }

    @Test
    fun getAssetCurrenciesExchangeRateViewStateLoaded() {

        var isLoading = false

        var stateObserver: Observer<ExchangeRateViewState>? = null

        val state = viewModel.getViewState()

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Loading && it.isLoading.not()) {
                isLoading = false
                state.removeObserver(stateObserver!!)
            }
        }
        every { repositoryPayLoadSuccessAssets.currencies } returns assetCurrencies

        coEvery {
            getCurrenciesViaAssetsUseCase()
        } returns repositoryPayLoadSuccessAssets


        state.observeForever(stateObserver)

        viewModel.getCurrenciesFromAssets()

        Assert.assertFalse(isLoading)

    }

    @Test
    fun getAssetCurrenciesExchangeRateViewState() {

        var stateObserver: Observer<ExchangeRateViewState>? = null

        val state = viewModel.getViewState()

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.CurrenciesFromAssets) {
                state.removeObserver(stateObserver!!)
            }
        }
        every { repositoryPayLoadSuccessAssets.currencies } returns assetCurrencies

        coEvery {
            getCurrenciesViaAssetsUseCase()
        } returns repositoryPayLoadSuccessAssets


        state.observeForever(stateObserver)

        viewModel.getCurrenciesFromAssets()

        Assert.assertTrue(state.value is ExchangeRateViewState.CurrenciesFromAssets)

    }

    @Test
    fun getAssetCurrenciesExchangeRateViewStateError() {

        var stateObserver: Observer<ExchangeRateViewState>? = null

        val state = viewModel.getViewState()

        stateObserver = Observer<ExchangeRateViewState> {
            if (it is ExchangeRateViewState.Error) {
                state.removeObserver(stateObserver!!)
            }
        }


        coEvery {
            getCurrenciesViaAssetsUseCase()
        } returns repositoryPayLoadFailAssets

        every { repositoryPayLoadFailAssets.error } returns "Asset list empty."

        state.observeForever(stateObserver)

        viewModel.getCurrenciesFromAssets()

        Assert.assertTrue(state.value is ExchangeRateViewState.Error)

    }


}