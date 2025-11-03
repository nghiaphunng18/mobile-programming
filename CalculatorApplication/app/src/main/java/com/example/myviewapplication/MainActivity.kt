package com.example.myviewapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myviewapplication.R

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var resultTextView: TextView
    private lateinit var historyTextView: TextView

    private var displayValue: String = "0"
    private var historyValue: String = ""

    private var operand1: Int? = null
    private var pendingOperation: String? = null

    private var isNewEntry: Boolean = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
        } catch (e: Exception) {
            resultTextView = TextView(this)
            historyTextView = TextView(this)
            resultTextView.text = "Error: Unable to set content view."
            setContentView(resultTextView)
            return
        }

        resultTextView = findViewById(R.id.resultTextView)
        historyTextView = findViewById(R.id.historyTextView)

        // Number button
        findViewById<Button>(R.id.btn0).setOnClickListener(this)
        findViewById<Button>(R.id.btn1).setOnClickListener(this)
        findViewById<Button>(R.id.btn2).setOnClickListener(this)
        findViewById<Button>(R.id.btn3).setOnClickListener(this)
        findViewById<Button>(R.id.btn4).setOnClickListener(this)
        findViewById<Button>(R.id.btn5).setOnClickListener(this)
        findViewById<Button>(R.id.btn6).setOnClickListener(this)
        findViewById<Button>(R.id.btn7).setOnClickListener(this)
        findViewById<Button>(R.id.btn8).setOnClickListener(this)
        findViewById<Button>(R.id.btn9).setOnClickListener(this)

        // Operator button
        findViewById<Button>(R.id.btnAdd).setOnClickListener(this)
        findViewById<Button>(R.id.btnSubtract).setOnClickListener(this)
        findViewById<Button>(R.id.btnMultiply).setOnClickListener(this)
        findViewById<Button>(R.id.btnDivide).setOnClickListener(this)
        findViewById<Button>(R.id.btnEquals).setOnClickListener(this)

        // Function button
        findViewById<Button>(R.id.btnC).setOnClickListener(this)
        findViewById<Button>(R.id.btnCE).setOnClickListener(this)
        findViewById<Button>(R.id.btnBS).setOnClickListener(this)
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener(this)

        findViewById<Button>(R.id.btnDot).setOnClickListener(this)

        updateDisplay()
    }

    override fun onClick(v: View?) {
        if (v == null) return

        if (displayValue == "Error") {
            if (v.id == R.id.btnC) {
                clearAll()
            }
            return
        }

        when (v.id) {
            R.id.btn0 -> onNumberClick("0")
            R.id.btn1 -> onNumberClick("1")
            R.id.btn2 -> onNumberClick("2")
            R.id.btn3 -> onNumberClick("3")
            R.id.btn4 -> onNumberClick("4")
            R.id.btn5 -> onNumberClick("5")
            R.id.btn6 -> onNumberClick("6")
            R.id.btn7 -> onNumberClick("7")
            R.id.btn8 -> onNumberClick("8")
            R.id.btn9 -> onNumberClick("9")

            R.id.btnAdd -> onOperationClick("+")
            R.id.btnSubtract -> onOperationClick("-")
            R.id.btnMultiply -> onOperationClick("*")
            R.id.btnDivide -> onOperationClick("/")
            R.id.btnEquals -> onEqualsClick()

            R.id.btnC -> clearAll()
            R.id.btnCE -> clearEntry()
            R.id.btnBS -> backspace()
            R.id.btnPlusMinus -> onPlusMinusClick()
        }
    }

    private fun onNumberClick(number: String) {
        if (isNewEntry) {
            if (pendingOperation == null) {
                historyValue = ""
            }
            displayValue = number
            isNewEntry = false
        } else {
            if (displayValue == "0") {
                displayValue = number
            } else {
                displayValue += number
            }
        }
        updateDisplay()
    }

    private fun onOperationClick(operation: String) {
        val currentDisplay = displayValue

        if (!isNewEntry) {
            performCalculation()
        }

        if (pendingOperation == null) {
            historyValue = "$currentDisplay $operation"
        } else {
            historyValue += " $currentDisplay $operation"
        }

        operand1 = displayValue.toIntOrNull()
        pendingOperation = operation
        isNewEntry = true

        updateDisplay()
    }

    private fun onEqualsClick() {
        if (operand1 == null || pendingOperation == null) {
            return
        }

        val op2String = displayValue

        performCalculation()

        if (historyValue == "") {
            historyValue = op2String
        } else {
            historyValue += " $op2String"
        }

        pendingOperation = null
        isNewEntry = true
        updateDisplay()
    }

    private fun performCalculation() {
        if (operand1 == null || pendingOperation == null) {
            return
        }

        val op1 = operand1!!
        val op2 = displayValue.toIntOrNull() ?: 0

        var result = 0

        when (pendingOperation) {
            "+" -> result = op1 + op2
            "-" -> result = op1 - op2
            "*" -> result = op1 * op2
            "/" -> {
                if (op2 == 0) {
                    displayValue = "Error"
                    historyValue = ""
                    operand1 = null
                    pendingOperation = null
                    isNewEntry = true
                    updateDisplay()
                    return
                }
                result = op1 / op2
            }
        }

        displayValue = result.toString()
        operand1 = result
    }

    /**
     * Function button C
     */
    private fun clearAll() {
        displayValue = "0"
        historyValue = ""
        operand1 = null
        pendingOperation = null
        isNewEntry = true
        updateDisplay()
    }

    /**
     * Function button CE
     */
    private fun clearEntry() {
        displayValue = "0"
        isNewEntry = true
        updateDisplay()
    }

    /**
     * function button BS
     */
    private fun backspace() {

        if (isNewEntry || displayValue == "Error") {
            return
        }

        if (displayValue.length > 1) {
            displayValue = displayValue.dropLast(1)
            if(displayValue == "-") {
                displayValue = "0"
                isNewEntry = true
            }
        } else {
            displayValue = "0"
            isNewEntry = true
        }
        updateDisplay()
    }

    private fun onPlusMinusClick() {
        if (displayValue == "Error") return

        if (displayValue == "0") {
            return
        }

        if (displayValue.startsWith("-")) {
            displayValue = displayValue.substring(1)
        } else {
            displayValue = "-$displayValue"
        }

        if (isNewEntry) {
            operand1 = displayValue.toIntOrNull()
        }

        updateDisplay()
    }

    private fun updateDisplay() {
        resultTextView.text = displayValue
        historyTextView.text = historyValue
    }
}
