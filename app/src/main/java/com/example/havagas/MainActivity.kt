package com.example.havagas

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.havagas.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var amb: ActivityMainBinding;
    private lateinit var birthDateEt: EditText;
    private lateinit var formationSp: Spinner;
    private lateinit var formationLl: LinearLayout;
    private lateinit var cellphoneCb: CheckBox;
    private lateinit var cellphoneEt: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amb.root)

        setupCellphoneCb()
        setupBirthDatePicker()
        setupFormationSpinner()
    }

    private fun setupCellphoneCb(){
        cellphoneCb = amb.cellphoneCb;
        cellphoneEt = amb.cellphoneEt;
        val drawableEnabled = ContextCompat.getDrawable(this, R.drawable.edittext_background_enabled)
        val drawableDisabled = ContextCompat.getDrawable(this, R.drawable.edittext_background_disabled)

        cellphoneCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                println("Enabled")
                cellphoneEt.isEnabled = true
                cellphoneEt.background = drawableEnabled
            } else {
                println("Disabled")
                cellphoneEt.isEnabled = false
                cellphoneEt.background = drawableDisabled
            }
        }

    }

    private fun setupBirthDatePicker() {
        birthDateEt = amb.birthDateEt;
        birthDateEt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, monthOfYear, dayOfMonth)

                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val selectedDate = simpleDateFormat.format(calendar.time)

                    birthDateEt.setText(selectedDate)
                },
                1990, 0, 1
            )
            datePickerDialog.show()
        }

        birthDateEt.setOnClickListener {
            val calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val selectedDate = simpleDateFormat.format(calendar.time)
                    birthDateEt.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun setupFormationSpinner() {
        formationSp = amb.formationSp;
        formationLl = amb.formationLl;


        formationSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formationLl.removeAllViews()

                when (position) {
                    0,1 -> {
                        println("primeiro caso")
                    }
                    2,3 -> {
                        println("segundo caso")
                    }
                    4,5 -> {
                        println("terceiro caso")
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun addFormationFieldsCase1() {

    }
}