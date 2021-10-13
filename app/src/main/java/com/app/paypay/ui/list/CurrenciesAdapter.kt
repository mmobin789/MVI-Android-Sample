package com.app.paypay.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.paypay.databinding.AdapterCurrenciesBinding
import com.app.paypay.exchange.repositories.currencylayer.source.local.business.ExchangeRate
import com.app.paypay.utils.getConvertedAmount

class CurrenciesAdapter(private val sourceAmount: Double, private val list: List<ExchangeRate>) :
    RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            AdapterCurrenciesBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.run {
            val exchangeRate = list[position]
            tvAmount.text = exchangeRate.getConvertedAmount(sourceAmount)
            tvCurrency.text = exchangeRate.destinationCurrency
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(val binding: AdapterCurrenciesBinding) :
        RecyclerView.ViewHolder(binding.root)
}