package com.nsh.currencyconverter.views.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nsh.currencyconverter.R
import com.nsh.currencyconverter.controllers.ExchangeController

class HomeFragment : Fragment() {
    private lateinit var convertFromDropDown: TextView
    private lateinit var convertToDropDown: TextView
    private val exchangeController = ExchangeController()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        convertFromDropDown = view.findViewById(R.id.convertFromDropDown)
        convertToDropDown = view.findViewById(R.id.convertToDropDown)

        convertFromDropDown.setOnClickListener {
            showCurrencyDialog { selectedCurrency ->
                convertFromDropDown.text = selectedCurrency
            }
        }

        convertToDropDown.setOnClickListener {
            showCurrencyDialog { selectedCurrency ->
                convertToDropDown.text = selectedCurrency
            }
        }

        return view
    }

    private fun showCurrencyDialog(onCurrencySelected: (String) -> Unit) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.spinner)

        val lvCurrency = dialog.findViewById<ListView>(R.id.lvCurrency)
        val searchEditText = dialog.findViewById<EditText>(R.id.edtCurrency)

        exchangeController.fetchSymbols { symbols ->
            symbols?.let {
                val currencyNames = it.values.toList()
                val currencyCodes = it.keys.toList()
                val filteredCurrencyNames = currencyNames.toMutableList()
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filteredCurrencyNames)
                lvCurrency.adapter = adapter

                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val query = s.toString().lowercase()
                        val newFilteredCurrencyNames = currencyNames.filter { it.lowercase().contains(query) }
                        adapter.clear()
                        adapter.addAll(newFilteredCurrencyNames)
                        adapter.notifyDataSetChanged()
                    }
                })

                lvCurrency.setOnItemClickListener { _, _, position, _ ->
                    val selectedCurrencyCode = currencyCodes[currencyNames.indexOf(filteredCurrencyNames[position])]
                    onCurrencySelected(selectedCurrencyCode)
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }

}
