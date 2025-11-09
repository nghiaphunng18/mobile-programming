package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val exchangeRates = mapOf(
        "USD" to 1.0,           // US Dollar (base)
        "VND" to 26_300.0,      // Vietnamese Dong
        "EUR" to 0.86,          // Euro ~1 USD = 0.86 EUR
        "JPY" to 153.4,         // Japanese Yen ~1 USD
        "GBP" to 0.76,          // British Pound ~1 USD = 0.76 GBP
        "AUD" to 1.54,          // Australian Dollar ~1 USD = 1.54 AUD
        "CAD" to 1.35,          // Canadian Dollar ~1 USD = 1.35 CAD
        "CHF" to 0.805,         // Swiss Franc ~1 USD = 0.805 CHF
        "CNY" to 7.20,          // Chinese Yuan ~1 USD = 7.20 CNY
        "KRW" to 1_380.0,       // South Korean Won ~1 USD ≈ 1,380 KRW
        "SGD" to 1.35           // Singapore Dollar ~1 USD ≈ 1.35 SGD
    )

    private val currencies = exchangeRates.keys.toList()

    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()

        setupListeners()

        // default USD -> VND
        binding.spnFrom.setSelection(currencies.indexOf("USD"))
        binding.spnTo.setSelection(currencies.indexOf("VND"))
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spnFrom.adapter = adapter
        binding.spnTo.adapter = adapter
    }

    private fun setupListeners() {
        // listener 2 Spinner
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // change Spinner, update -> etFrom
                convertCurrency(binding.etFrom, binding.etTo, binding.spnFrom, binding.spnTo)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spnFrom.onItemSelectedListener = spinnerListener
        binding.spnTo.onItemSelectedListener = spinnerListener

        // listener EditText "From"
        binding.etFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) {
                    convertCurrency(binding.etFrom, binding.etTo, binding.spnFrom, binding.spnTo)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // listener EditText "To"
        binding.etTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) {
                    convertCurrency(binding.etTo, binding.etFrom, binding.spnTo, binding.spnFrom)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun convertCurrency(
        sourceInput: android.widget.EditText,
        targetOutput: android.widget.EditText,
        sourceSpinner: android.widget.Spinner,
        targetSpinner: android.widget.Spinner
    ) {
        isUpdating = true

        val amountString = sourceInput.text.toString()
        if (amountString.isNotEmpty()) {
            try {
                val amount = amountString.toDouble()
                val fromCurrency = sourceSpinner.selectedItem.toString()
                val toCurrency = targetSpinner.selectedItem.toString()

                // get exchange rate
                val fromRate = exchangeRates[fromCurrency]!!
                val toRate = exchangeRates[toCurrency]!!

                // conversion
                val amountInBase = amount / fromRate // convert 1 USD base
                val result = amountInBase * toRate

                // format
                val formattedResult = String.format(Locale.US, "%.2f", result)
                targetOutput.setText(formattedResult)

            } catch (e: Exception) {
                targetOutput.setText("")
            }
        } else {
            targetOutput.setText("")
        }

        isUpdating = false
    }
}