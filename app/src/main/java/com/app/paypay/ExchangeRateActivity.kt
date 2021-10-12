package com.app.paypay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.paypay.exchange.di.DI
import com.app.paypay.exchange.vm.ExchangeActivityViewModel
import com.app.paypay.exchange.vm.ExchangeRateViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExchangeRateActivity : AppCompatActivity() {

    private val viewModel: ExchangeActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_rate)
        DI.start(this)



        viewModel.let {
            it.getViewState().observe(this) { viewState ->
                when (viewState) {
                    is ExchangeRateViewState.Loading -> {
                        Toast.makeText(this, "Loading ${viewState.isLoading}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ExchangeRateViewState.Success -> {
                        Toast.makeText(
                            this,
                            "Data ${viewState.exchangeRates.size}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    is ExchangeRateViewState.Error -> {
                        Toast.makeText(this, "${viewState.error}", Toast.LENGTH_SHORT)
                            .show()
                    }
                    ExchangeRateViewState.Idle -> {

                    }
                }
            }
            it.getExchangeRates("USD")
        }

    }
}