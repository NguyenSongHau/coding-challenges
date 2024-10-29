package com.nsh.currencyconverter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.models.CurrencyDetails

class CurrencyAdapter(
    context: Context,
    private val currencyDetails: List<CurrencyDetails>
) : ArrayAdapter<CurrencyDetails>(context, 0, currencyDetails) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)
        val currencyDetail = getItem(position)

        val currencyNameTextView = view.findViewById<TextView>(R.id.tvNameCurrency)
        currencyNameTextView.text = "${currencyDetail?.name} (${currencyDetail?.symbol})"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
