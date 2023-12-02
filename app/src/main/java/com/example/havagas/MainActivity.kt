package com.example.havagas

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
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
import com.example.havagas.databinding.ActivityMainBinding
import com.example.havagas.domain.models.Form
import com.example.havagas.domain.models.enums.FormationEnum
import com.example.havagas.domain.models.enums.GenderEnum
import com.example.havagas.shared.utils.MockUtils
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private lateinit var drawableEnabled: Drawable
    private lateinit var drawableDisabled: Drawable

    private var form: Form? = null
    val mockUtils = MockUtils(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        amb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amb.root)

        drawableEnabled = ContextCompat.getDrawable(this, R.drawable.edittext_background_enabled)!!
        drawableDisabled =
            ContextCompat.getDrawable(this, R.drawable.edittext_background_disabled)!!


        linkVariablesWithComponents()
        setupCellphoneCb()
        setupBirthDatePicker()
        setupFormationSpinner()
        setupSaveBtnOnClickListener()
        setupCleanFormBtnOnClickListener()
    }

    override fun onStart() {
        super.onStart()

        val formRestored: Form? = restoreFormDataFromOnSharedPreferences()
        if (formRestored != null) {
            form = formRestored;
            fillFormWithRestoredData();
        }
    }

    override fun onStop() {
        super.onStop()

        saveFormDataFromOnSharedPreferences()
    }

    private fun saveFormDataFromOnSharedPreferences() {
        val sharedPreferences = getSharedPreferences("HaVagas", Context.MODE_PRIVATE)
        if (form == null) return
        val editor = sharedPreferences.edit()

        val gson = Gson()
        val formJson = gson.toJson(form)

        editor.putString("form", formJson)
        editor.apply()
    }

    private fun restoreFormDataFromOnSharedPreferences(): Form? {
        val sharedPreferences = getSharedPreferences("HaVagas", Context.MODE_PRIVATE)

        val formJson = sharedPreferences.getString("form", "")
        if (formJson.isNullOrEmpty()) return null

        val gson = Gson()
        val formType = object : TypeToken<Form>() {}.type
        return gson.fromJson<Form>(formJson, formType) ?: Form()
    }

    private fun fillFormWithRestoredData() {
        nameEt.text = Editable.Factory.getInstance().newEditable(form?.name ?: "")
        emailEt.text = Editable.Factory.getInstance().newEditable(form?.email ?: "")
        receiveEmailsCb.isChecked = form?.receiveEmails ?: false
        phoneEt.text = Editable.Factory.getInstance().newEditable(form?.phone ?: "")
        fillCellphoneWithRestoredData()
        fillGenderWithRestoredData()
        fillBirthDateWithRestoredData()
        fillFormationWithRestoredData()
        interestJobsEt.text = Editable.Factory.getInstance().newEditable(form?.interestJobs ?: "")
    }

    private fun fillCellphoneWithRestoredData() {
        if (form?.hasCellphone == true) {
            cellphoneCb.isChecked = true

            cellphoneEt.isEnabled = true
            cellphoneEt.background = drawableEnabled
            cellphoneEt.text = Editable.Factory.getInstance().newEditable(form?.cellphone ?: "")
        }
    }

    private fun fillGenderWithRestoredData() {
        if (form?.gender.isNullOrEmpty()) return
        when (form?.gender) {
            GenderEnum.Feminino.toString() -> {
                genderSp.setSelection(0)
            }

            GenderEnum.Masculino.toString() -> {
                genderSp.setSelection(1)
            }

            else -> {
                genderSp.setSelection(2)
            }
        }
    }

    private fun fillBirthDateWithRestoredData() {
        if (form?.birthDate.isNullOrEmpty()) return

        birthDateEt.setText(form?.birthDate)
    }

    private fun fillFormationWithRestoredData() {
        if (form?.formation?.formationType.isNullOrEmpty()) return
        when (form?.formation?.formationType) {
            FormationEnum.ENSINO_FUNDAMENTAL.stringValue -> {
                formationSp.setSelection(0)
                fillFormationFirstOption()
            }

            FormationEnum.ENSINO_MEDIO.stringValue -> {
                formationSp.setSelection(1)
                fillFormationFirstOption()
            }

            FormationEnum.GRADUACAO.stringValue -> {
                formationSp.setSelection(2)
                fillFormationSecondOption()
            }

            FormationEnum.ESPECIALIZACAO.stringValue -> {
                formationSp.setSelection(3)
                fillFormationSecondOption()
            }

            FormationEnum.MESTRADO.stringValue -> {
                formationSp.setSelection(4)
                fillFormationThirdOption()
            }

            FormationEnum.DOUTORADO.stringValue -> {
                formationSp.setSelection(5)
                fillFormationThirdOption()
            }
        }
    }

    private fun fillFormationFirstOption() {
        formationYearEt.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.formationYear ?: "")
        formationFirstOptionLl.visibility = View.VISIBLE
        formationSecondOptionLl.visibility = View.GONE
        formationThirdOptionLl.visibility = View.GONE
    }

    private fun fillFormationSecondOption() {
        conclusionYearEt.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.conclusionYear ?: "")
        institutionEt.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.institution ?: "")
        formationFirstOptionLl.visibility = View.GONE
        formationSecondOptionLl.visibility = View.VISIBLE
        formationThirdOptionLl.visibility = View.GONE
    }

    private fun fillFormationThirdOption() {
        conclusionYearEt2.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.conclusionYear ?: "")
        institutionEt2.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.institution ?: "")
        monographyTitleEt.text =
            Editable.Factory.getInstance().newEditable(form?.formation?.monographyTitle ?: "")
        advisorEt.text = Editable.Factory.getInstance().newEditable(form?.formation?.advisor ?: "")
        formationFirstOptionLl.visibility = View.GONE
        formationSecondOptionLl.visibility = View.GONE
        formationThirdOptionLl.visibility = View.VISIBLE
    }

    private fun clearFormDataFromOnSharedPreferences() {
        val sharedPreferences = getSharedPreferences("HaVagas", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("form")
        editor.apply()
    }

    private fun setupCellphoneCb() {
        cellphoneCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cellphoneEt.isEnabled = true
                cellphoneEt.background = drawableEnabled
            } else {
                cellphoneEt.isEnabled = false
                cellphoneEt.background = drawableDisabled
            }
        }

    }

    private fun setupBirthDatePicker() {
        val calendar = Calendar.getInstance()

        // SETS UP THE EDITTEXT WITH THE CURRENT DATE
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = simpleDateFormat.format(calendar.time)
        birthDateEt.setText(currentDate)

        // SETS UP THE DATEPICKERDIALOG
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
                // NO IMPLEMENTATION NEEDED
            }
        }
    }

    private fun setupCleanFormBtnOnClickListener() {
        cleanFormBtn.setOnClickListener {
            cleanForms(principalConstraintLayout)
            form = null
            clearFormDataFromOnSharedPreferences()
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
                child.isChecked = false
            } else if (child is ViewGroup) {
                cleanForms(child)
            }
        }

        //DISABLE CELLPHONE FIELD
        cellphoneEt.isEnabled = false
        cellphoneEt.background = drawableDisabled
    }

    private fun setupSaveBtnOnClickListener() {
        saveBtn.setOnClickListener {
//           TO USE MOCKS INSTEAD OF FILLING IN THE FIELDS
//            form = createMockForm()
//            return@setOnClickListener

            if (!isInputsValid(principalConstraintLayout)) {
                Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val title = "Dados salvos!"
            form = getData()
            showPopup(title, form.toString())
            form = null
            clearFormDataFromOnSharedPreferences()
        }
    }

    fun createMockForm(): Form? {
        val formType = object : TypeToken<Form>() {}.type

        return mockUtils.getMockFromAsset("form-mocks.json", formType)
    }

    private fun getData(): Form {
        if (form == null) form = Form()
        form!!.name = nameEt.text.toString()
        form!!.email = emailEt.text.toString()
        form!!.receiveEmails = receiveEmailsCb.isChecked
        form!!.phone = phoneEt.text.toString()
        form!!.cellphone = cellphoneEt.text.toString()
        form!!.hasCellphone = cellphoneCb.isChecked
        form!!.gender = genderSp.selectedItem.toString()
        form!!.birthDate = birthDateEt.text.toString()
        var formationSelected = formationSp.selectedItemPosition

        form!!.formation.formationType = formationSp.selectedItem.toString()
        when (formationSelected) {
            0, 1 -> {
                form!!.formation.formationYear = formationYearEt.text.toString()
            }

            2, 3 -> {
                form!!.formation.conclusionYear = conclusionYearEt.text.toString()
                form!!.formation.institution = institutionEt.text.toString()
            }

            4, 5 -> {
                form!!.formation.conclusionYear = conclusionYearEt2.text.toString()
                form!!.formation.institution = institutionEt2.text.toString()
                form!!.formation.monographyTitle = monographyTitleEt.text.toString()
                form!!.formation.advisor = advisorEt.text.toString()
            }
        }
        form!!.interestJobs = interestJobsEt.text.toString()

        return form as Form
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

        if (!isCellphoneInputValid()) return false
        if (!isFormationInputsValid()) return false
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