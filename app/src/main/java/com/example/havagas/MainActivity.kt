package com.example.havagas

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.havagas.databinding.ActivityMainBinding
import com.example.havagas.domain.models.Form
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var amb: ActivityMainBinding

    private lateinit var principalConstraintLayout: ConstraintLayout
    private lateinit var nameEt: EditText
    private lateinit var emailEt: EditText
    private lateinit var receiveEmailsCb: CheckBox
    private lateinit var phoneEt: EditText
    private lateinit var cellphoneEt: EditText
    private lateinit var cellphoneCb: CheckBox
    private lateinit var genderSp: Spinner
    private lateinit var birthDateEt: EditText
    private lateinit var formationSp: Spinner
    private lateinit var formationFirstOptionLl: LinearLayout
    private lateinit var formationYearEt: EditText
    private lateinit var formationSecondOptionLl: LinearLayout
    private lateinit var conclusionYearEt: EditText
    private lateinit var institutionEt: EditText
    private lateinit var formationThirdOptionLl: LinearLayout
    private lateinit var conclusionYearEt2: EditText
    private lateinit var institutionEt2: EditText
    private lateinit var monographyTitleEt: EditText
    private lateinit var advisorEt: EditText
    private lateinit var interestJobsEt: EditText
    private lateinit var cleanFormBtn: Button
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amb.root)

        linkVariablesWithComponents()
        setupCellphoneCb()
        setupBirthDatePicker()
        setupFormationSpinner()
        setupSaveBtnOnClickListener()
        setupCleanFormBtnOnClickListener()
    }

    private fun setupCellphoneCb() {
        val drawableEnabled =
            ContextCompat.getDrawable(this, R.drawable.edittext_background_enabled)
        val drawableDisabled =
            ContextCompat.getDrawable(this, R.drawable.edittext_background_disabled)

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
        val calendar = Calendar.getInstance()

        // configura o EditText com a data atual
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = simpleDateFormat.format(calendar.time)
        birthDateEt.setText(currentDate)

        // configurar o DatePickerDialog
        birthDateEt.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
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
        formationSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0, 1 -> {
                        formationFirstOptionLl.visibility = View.VISIBLE
                        formationSecondOptionLl.visibility = View.GONE
                        formationThirdOptionLl.visibility = View.GONE
                    }

                    2, 3 -> {
                        formationFirstOptionLl.visibility = View.GONE
                        formationSecondOptionLl.visibility = View.VISIBLE
                        formationThirdOptionLl.visibility = View.GONE
                    }

                    4, 5 -> {
                        formationFirstOptionLl.visibility = View.GONE
                        formationSecondOptionLl.visibility = View.GONE
                        formationThirdOptionLl.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setupCleanFormBtnOnClickListener() {
        cleanFormBtn.setOnClickListener {
            cleanForms(principalConstraintLayout)
        }
    }

    private fun cleanForms(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is EditText) {
                if (child.id == R.id.birthDateEt) {
                    setupBirthDatePicker()
                    continue
                }
                child.text.clear()
            } else if (child is Spinner) {
                child.setSelection(0)
            } else if (child is CheckBox) {
                child.isSelected = false
            } else if (child is ViewGroup) {
                cleanForms(child)
            }
        }
    }

    private fun setupSaveBtnOnClickListener() {
        saveBtn.setOnClickListener {
            if (!isInputsValid(principalConstraintLayout)) {
                Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val title = "Dados salvos!"
            var form: Form = getData()
            showPopup(title, form.toString())
        }
    }

    private fun getData(): Form {
        var form = Form()
        form.name = nameEt.text.toString()
        form.email = emailEt.text.toString()
        form.receiveEmails = receiveEmailsCb.isChecked
        form.phone = phoneEt.text.toString()
        form.cellphone = cellphoneEt.text.toString()
        form.hasCellphone = cellphoneCb.isChecked
        form.gender = genderSp.selectedItem.toString()
        form.birthDate = birthDateEt.text.toString()
        var formationSelected = formationSp.selectedItemPosition

        form.formation.formationType = formationSp.selectedItem.toString()
        when (formationSelected) {
            0, 1 -> {
                form.formation.formationYear = formationYearEt.text.toString()
            }

            2, 3 -> {
                form.formation.conclusionYear = conclusionYearEt.text.toString()
                form.formation.institution = institutionEt.text.toString()
            }

            4, 5 -> {
                form.formation.conclusionYear = conclusionYearEt2.text.toString()
                form.formation.institution = institutionEt2.text.toString()
                form.formation.monographyTitle = monographyTitleEt.text.toString()
                form.formation.advisor = advisorEt.text.toString()
            }
        }
        form.interestJobs = interestJobsEt.text.toString()

        return form
    }

    private fun linkVariablesWithComponents() {
        principalConstraintLayout = amb.principalConstraintLayout
        nameEt = amb.nameEt
        emailEt = amb.emailEt
        receiveEmailsCb = amb.receiveEmailsCb
        phoneEt = amb.phoneEt
        cellphoneEt = amb.cellphoneEt
        cellphoneCb = amb.cellphoneCb
        genderSp = amb.genderSp
        birthDateEt = amb.birthDateEt
        formationSp = amb.formationSp
        formationFirstOptionLl = amb.formationFirstOptionLl
        formationYearEt = amb.formationYearEt
        formationSecondOptionLl = amb.formationSecondOptionLl
        conclusionYearEt = amb.conclusionYearEt
        institutionEt = amb.institutionEt
        formationThirdOptionLl = amb.formationThirdOptionLl
        conclusionYearEt2 = amb.conclusionYearEt2
        institutionEt2 = amb.institutionEt2
        monographyTitleEt = amb.monographyTitleEt
        advisorEt = amb.advisorEt
        interestJobsEt = amb.interestJobsEt
        cleanFormBtn = amb.cleanFormBtn
        saveBtn = amb.saveBtn
    }

    //TODO implementar corretamente
    private fun isInputsValid(viewGroup: ViewGroup): Boolean {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is EditText) {
                val childId = amb.root.resources.getResourceEntryName(child.id)
                if (inputHasDifferentValidation(childId)) continue
                if (child.text.toString().trim().isEmpty()) {
                    return false
                }
            }
        }

        if(!isCellphoneInputValid()) return false
        if(!isFormationInputsValid()) return false
        return true
    }

    private fun inputHasDifferentValidation(inputId: String): Boolean {
        val inputsWithDiferentValidation = arrayListOf(
            "cellphoneEt",
            "formationYearEt",
            "conclusionYearEt",
            "institutionEt",
            "conclusionYearEt2",
            "institutionEt2",
            "monographyTitleEt",
            "advisorEt",
        )

        return inputsWithDiferentValidation.contains(inputId)
    }

    private fun isCellphoneInputValid(): Boolean {
        if (cellphoneCb.isChecked)
            if (cellphoneEt.text.toString().trim().isEmpty())
                return false
        return true
    }

    private fun isFormationInputsValid(): Boolean {
        var formationSelected = formationSp.selectedItemPosition
        when (formationSelected) {
            0, 1 -> {
                if (formationYearEt.text.toString().trim().isEmpty())
                    return false
            }

            2, 3 -> {
                if (conclusionYearEt.text.toString().trim().isEmpty() ||
                    institutionEt.text.toString().trim().isEmpty()
                )
                    return false
            }

            4, 5 -> {
                if (conclusionYearEt2.text.toString().trim().isEmpty() ||
                    institutionEt2.text.toString().trim().isEmpty() ||
                    monographyTitleEt.text.toString().trim().isEmpty() ||
                    advisorEt.text.toString().trim().isEmpty()
                )
                    return false
            }
        }
        return true
    }

    private fun showPopup(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton("OK") { dialog, _ ->
            cleanForms(principalConstraintLayout)
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}