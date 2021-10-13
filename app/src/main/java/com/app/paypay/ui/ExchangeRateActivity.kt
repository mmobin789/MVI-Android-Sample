package com.app.paypay.ui

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.app.paypay.R
import com.app.paypay.databinding.ActivityExchangeRateBinding
import com.app.paypay.exchange.di.DI
import com.app.paypay.exchange.vm.ExchangeActivityViewModel
import com.app.paypay.exchange.vm.ExchangeRateViewState
import com.app.paypay.ui.list.CurrenciesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeRateActivity : AppCompatActivity() {

    private val viewModel: ExchangeActivityViewModel by viewModel()

    private lateinit var binding: ActivityExchangeRateBinding

    private lateinit var currencyMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DI.start(this)
        render()


    }

    private fun createCurrencyMenu(currencies: List<String>) {
        val tv = binding.tvCurrency
        tv.text = currencies[0]
        currencyMenu = PopupMenu(tv.context, tv).apply {
            currencies.forEach {
                menu.add(it)
            }

            setOnMenuItemClickListener {
                val srcCurrency = it.title.toString()
                tv.text = srcCurrency
                viewModel.getExchangeRates(srcCurrency, binding.etAmount.text.toString())
                true
            }
        }
    }

    private fun showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun showToast(@StringRes stringResId: Int) =
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show()

    private fun render() {

        binding.tvCurrency.setOnClickListener {
            currencyMenu.show()
        }

        viewModel.let {
            it.getViewState().observe(this) { viewState ->
                when (viewState) {
                    is ExchangeRateViewState.Loading -> {
                        showToast("Loading ${viewState.isLoading}")
                    }
                    is ExchangeRateViewState.ExchangeRates -> {
                        binding.rvCurrencies.adapter =
                            CurrenciesAdapter(viewState.sourceAmount, viewState.exchangeRates)
                    }
                    is ExchangeRateViewState.CurrenciesFromAssets -> {
                        createCurrencyMenu(viewState.currencies)
                        binding.tvCurrency.isEnabled = true
                    }
                    is ExchangeRateViewState.Error -> {
                        showToast("${viewState.error}")
                    }
                    is ExchangeRateViewState.GenericError -> {
                        showToast(viewState.errorIdRes)
                    }
                    ExchangeRateViewState.Idle -> {
                        createCurrencyMenu(listOf("USD"))
                    }
                }
            }

            it.getCurrenciesFromAssets()
        }

    }
}