package com.nsh.currencyconverter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.models.ConvertedCurrencyItem
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class ConvertedCurrencyAdapter(
    private val context: Context,
    private var items: List<ConvertedCurrencyItem>,
    private var amount: Double
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): ConvertedCurrencyItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false)

        val currencyNameTextView: TextView = view.findViewById(R.id.currencyCode)
        val currencyValueTextView: TextView = view.findViewById(R.id.currencyAmount)

        val currencyItem = getItem(position)

        val decimalFormat = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))

        currencyNameTextView.text = currencyItem.name

        val convertedAmount = amount * (currencyItem.value.toDoubleOrNull() ?: 0.0)
        currencyValueTextView.text = decimalFormat.format(convertedAmount)

        return view
    }

    fun updateData(newItems: List<ConvertedCurrencyItem>, newAmount: Double) {
        items = newItems
        amount = newAmount
        notifyDataSetChanged()
    }
}
