package com.app.paypay

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.paypay.databinding.ActivityExchangeRateBinding
import com.app.paypay.exchange.di.DI
import com.app.paypay.exchange.vm.ExchangeActivityViewModel
import com.app.paypay.exchange.vm.ExchangeRateViewState
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
        val v = binding.tvCurrency
        currencyMenu = PopupMenu(v.context, v).apply {
            currencies.forEach {
                menu.add(it)
            }

            setOnMenuItemClickListener {
                viewModel.getExchangeRates(it.title.toString())
                true
            }
        }
    }

    private fun render() {

        binding.tvCurrency.setOnClickListener {
            currencyMenu.show()
        }

        viewModel.getViewState().observe(this) { viewState ->
            when (viewState) {
                is ExchangeRateViewState.Loading -> {
                    Toast.makeText(this, "Loading ${viewState.isLoading}", Toast.LENGTH_SHORT)
                        .show()
                }
                is ExchangeRateViewState.ExchangeRates -> {
                    Toast.makeText(
                        this,
                        "Data ${viewState.exchangeRates.size}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                is ExchangeRateViewState.CurrenciesFromAssets -> {
                    createCurrencyMenu(viewState.currencies)
                    binding.tvCurrency.isEnabled = true
                }
                is ExchangeRateViewState.Error -> {
                    Toast.makeText(this, "${viewState.error}", Toast.LENGTH_SHORT)
                        .show()
                }
                ExchangeRateViewState.Idle -> {

                }
            }
        }

        viewModel.getCurrenciesFromAssets()

    }
}