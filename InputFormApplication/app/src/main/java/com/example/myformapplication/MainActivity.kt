package com.example.myformapplication

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etBirthday: EditText
    private lateinit var btnSelect: Button
    private lateinit var calendarView: CalendarView
    private lateinit var etAddress: EditText
    private lateinit var etEmail: EditText
    private lateinit var cbTerms: CheckBox
    private lateinit var btnRegister: Button

    private val originalBackgrounds = mutableMapOf<View, Drawable?>()

    private val errorColor = Color.parseColor("#FFCDD2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // view
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        rgGender = findViewById(R.id.rgGender)
        etBirthday = findViewById(R.id.etBirthday)
        btnSelect = findViewById(R.id.btnSelect)
        calendarView = findViewById(R.id.calendarView)
        etAddress = findViewById(R.id.etAddress)
        etEmail = findViewById(R.id.etEmail)
        cbTerms = findViewById(R.id.cbTerms)
        btnRegister = findViewById(R.id.btnRegister)

        // set bg
        saveOriginalBackgrounds()


        // select to show/ hide CalendarView
        btnSelect.setOnClickListener {
            toggleCalendarVisibility()
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            updateBirthdayText(year, month, dayOfMonth)
            calendarView.visibility = View.GONE
        }

        btnRegister.setOnClickListener {
            validateForm()
        }

        addValidationResetListeners()
    }

    /**
     * Save the original background color of the fields
     */
    private fun saveOriginalBackgrounds() {
        originalBackgrounds[etFirstName] = etFirstName.background
        originalBackgrounds[etLastName] = etLastName.background
        originalBackgrounds[rgGender] = rgGender.background
        originalBackgrounds[etBirthday] = etBirthday.background
        originalBackgrounds[etAddress] = etAddress.background
        originalBackgrounds[etEmail] = etEmail.background
        originalBackgrounds[cbTerms] = cbTerms.background
    }

    /**
     * Show/ hide CalendarView
     */
    private fun toggleCalendarVisibility() {
        if (calendarView.visibility == View.GONE) {
            calendarView.visibility = View.VISIBLE
        } else {
            calendarView.visibility = View.GONE
        }
    }

    /**
     * update etBirthday
     */
    private fun updateBirthdayText(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etBirthday.setText(sdf.format(calendar.time))

        resetFieldBackground(etBirthday)
    }

    /**
     * validate Register
     */
    private fun validateForm() {
        resetAllBackgrounds()

        var isValid = true

        // First Name
        if (etFirstName.text.isNullOrEmpty()) {
            setFieldError(etFirstName)
            isValid = false
        }

        // Last Name
        if (etLastName.text.isNullOrEmpty()) {
            setFieldError(etLastName)
            isValid = false
        }

        // Gender
        if (rgGender.checkedRadioButtonId == -1) {
            setFieldError(rgGender)
            isValid = false
        }

        // Birthday
        if (etBirthday.text.isNullOrEmpty()) {
            setFieldError(etBirthday)
            isValid = false
        }

        // Address
        if (etAddress.text.isNullOrEmpty()) {
            setFieldError(etAddress)
            isValid = false
        }

        // Email
        if (etEmail.text.isNullOrEmpty()) {
            setFieldError(etEmail)
            isValid = false
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            setFieldError(etEmail)
            isValid = false
        }

        if (!cbTerms.isChecked) {
            setFieldError(cbTerms)
            isValid = false
        }

        if (isValid) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ các trường bắt buộc.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFieldError(view: View) {
        view.setBackgroundColor(errorColor)
    }

    /**
     * Reset bg
     */
    private fun resetFieldBackground(view: View) {
        view.background = originalBackgrounds[view]
    }

    private fun resetAllBackgrounds() {
        resetFieldBackground(etFirstName)
        resetFieldBackground(etLastName)
        resetFieldBackground(rgGender)
        resetFieldBackground(etBirthday)
        resetFieldBackground(etAddress)
        resetFieldBackground(etEmail)
        resetFieldBackground(cbTerms)
    }


    private fun addValidationResetListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { resetFieldBackground(etFirstName) }
            override fun afterTextChanged(s: Editable?) {}
        })
        etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { resetFieldBackground(etLastName) }
            override fun afterTextChanged(s: Editable?) {}
        })
        etAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { resetFieldBackground(etAddress) }
            override fun afterTextChanged(s: Editable?) {}
        })
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { resetFieldBackground(etEmail) }
            override fun afterTextChanged(s: Editable?) {}
        })

        rgGender.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                resetFieldBackground(rgGender)
            }
        }

        cbTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                resetFieldBackground(cbTerms)
            }
        }
    }
}
