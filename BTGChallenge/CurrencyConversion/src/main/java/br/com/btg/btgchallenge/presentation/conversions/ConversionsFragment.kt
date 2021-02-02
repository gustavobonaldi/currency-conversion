package br.com.btg.btgchallenge.presentation.conversions

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.btg.btgchallenge.R
import br.com.btg.btgchallenge.databinding.FragmentConversionsBinding
import br.com.btg.btgchallenge.api.api.config.Status
import br.com.btg.btgchallenge.presentation.FragmentBase
import br.com.btg.btgchallenge.presentation.conversions.Currency.CurrencyType
import br.com.btg.btgchallenge.presentation.currencylist.CurrencyListFragment
import br.com.btg.btgchallenge.presentation.extensions.getFormattedString
import br.com.btg.btgchallenge.presentation.extensions.setDrawableFlag
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_conversions.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.*

class ConversionsFragment : FragmentBase() {

    val conversionViewModel: ConversionViewModel by viewModel()
    private lateinit var binding: FragmentConversionsBinding
    private var currencyFrom = Currency(Pair("", ""), CurrencyType.FROM)
    private var currencyTo = Currency(Pair("", ""), CurrencyType.TO)
    private var current = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversions, container, false)
        binding.apply {
            viewModel = conversionViewModel
            lifecycleOwner = this@ConversionsFragment
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        conversionViewModel.getRealtimeRates()
    }

    fun setListeners(){
        container_from.setOnClickListener{
            showCurrencyList(CurrencyType.FROM)
        }

        container_to.setOnClickListener {
            showCurrencyList(CurrencyType.TO)
        }

        currency_from_edit_text.addTextChangedListener(convertCurrency())
    }

    private fun showCurrencyList(currencyType: CurrencyType) {
        var currencyListFragment: CurrencyListFragment? = null
        currencyListFragment = CurrencyListFragment(currencyType) { currency ->
            if (currencyType == CurrencyType.FROM) {
                clearFields()
                currencyFrom = currency
                container_from.getImageView().setDrawableFlag(context, currency)
                container_from.setCurrencyText(currency.getFormattedString())
            } else if (currencyType == CurrencyType.TO) {
                clearFields()
                currencyTo = currency
                container_to.getImageView().setDrawableFlag(context, currency)
                container_to.setCurrencyText(currency.getFormattedString())
            }
            currencyListFragment?.dismiss()
        }
        currencyListFragment.show(activity?.supportFragmentManager!!, "teste")
    }

    private fun setObservers(){
        conversionViewModel.realTimeRatesLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.ERROR -> {
                    showSnackBar(it?.message)
                }
            }
        })
    }

    private fun setValueConverted(value: Double) {
        try {
            conversionViewModel.getConversionFromTo(
                currencyFrom.currency,
                currencyTo.currency,
                ((value).toString().toDouble())
            ) {
                showSnackBar(resources.getString(it.errorMessage))
            }
        } catch (ex: Exception) {
            when {
                ex is NumberFormatException -> {
                    showSnackBar(resources.getString(R.string.type_valid_value))
                }
            }
        }
    }

    private fun convertCurrency(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().equals(current)) {
                    currency_from_edit_text.removeTextChangedListener(this)
                    val cleanString = s?.filter { it.isDigit() }
                    if (!cleanString?.isEmpty()!!) {
                        val parsed = cleanString.toString().toDouble()
                        setValueConverted(parsed / 100)
                        val formatted = NumberFormat.getCurrencyInstance(Locale.US).format((parsed / 100))
                        current = formatted
                        currency_from_edit_text.setText(formatted)
                        currency_from_edit_text.setSelection(formatted.length)
                    }
                    currency_from_edit_text.addTextChangedListener(this)
                }
            }
        }
    }

    private fun showSnackBar(message: String?) {
        try {
            message?.let { Snackbar.make(currency_from_edit_text, it, Snackbar.LENGTH_LONG).show() }
        } catch (ex: Exception) { }
    }

    private fun clearFields() {
        text_view_converted_value.text = ""
        currency_from_edit_text.setText("")
    }
}