package br.com.bonaldi.currency.conversion.utils.customcomponents.controls

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

class CustomTextWatcher(private val textInput: TextInputEditText, private val valueTyped: (Double) -> Unit) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textInput.removeTextChangedListener(this)
        val cleanString = s?.filter { it.isDigit() }
        if (cleanString?.isEmpty() == false) {
            val parsed = cleanString.toString().toDouble()
            valueTyped.invoke(parsed/100)
        }
        textInput.addTextChangedListener(this)
    }

}