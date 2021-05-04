package br.com.bonaldi.currency.conversion.presentation.conversions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.databinding.FragmentConversionsBinding
import br.com.bonaldi.currency.conversion.presentation.BaseFragment
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.CustomTextWatcher
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency.CurrencyType
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListFragment
import br.com.bonaldi.currency.conversion.presentation.extensions.empty
import br.com.bonaldi.currency.conversion.presentation.extensions.getFormattedString
import br.com.bonaldi.currency.conversion.presentation.extensions.setDrawableFlag
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.*

class ConversionsFragment : BaseFragment() {

    private val viewModel: ConversionViewModel by viewModel()
    private lateinit var binding: FragmentConversionsBinding
    private var currencyFrom = Currency(Pair(String.empty(), String.empty()), CurrencyType.FROM)
    private var currencyTo = Currency(Pair(String.empty(), String.empty()), CurrencyType.TO)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentConversionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        setAds()
    }

    private fun setListeners() {
        binding.apply {
            containerFrom.setOnClickListener {
                showCurrencyList(CurrencyType.FROM)
            }

            containerTo.setOnClickListener {
                showCurrencyList(CurrencyType.TO)
            }

            NumberFormat.getCurrencyInstance(Locale.US).let {currencyInstance ->
                currencyFromEditText.apply {
                    addTextChangedListener(CustomTextWatcher(this) { typedValue ->
                        getConvertedValue(typedValue)
                        currencyInstance.format((typedValue)).let { formattedNumber ->
                                setText(formattedNumber)
                                setSelection(formattedNumber.length)
                        }
                    })
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.apply {
            addRealtimeRatesObserver(this@ConversionsFragment)
            updateRealtimeRates()
        }
    }

    private fun setAds() = binding.apply {
        adView.loadAd(AdRequest.Builder().build())
    }

    private fun getConvertedValue(value: Double) {
        try {
            viewModel.getConversionFromTo(currencyFrom.currency,
                currencyTo.currency,
                value.toString().toDouble(),
                onSuccess = {
                    binding.apply {
                        textViewConvertedValue.text = it
                        step4.visibility = if (it.length > 0) View.VISIBLE else View.GONE
                    }
                },
                onError = {
                    showSnackBar(resources.getString(it.errorMessage))
                })
        } catch (ex: Exception) {
            when {
                ex is NumberFormatException -> {
                    showSnackBar(resources.getString(R.string.type_valid_value))
                }
            }
        }
    }

    private fun showCurrencyList(currencyType: CurrencyType){
        val currencyListFragment = CurrencyListFragment(currencyType).apply {
            addOnCurrencyClickedListener { currency ->
                binding.apply {
                    if (currencyType == CurrencyType.FROM) {
                        clearFields()
                        currencyFrom = currency
                        containerFrom.getImageView().setDrawableFlag(context, currency)
                        containerFrom.setCurrencyText(currency.getFormattedString())
                    } else if (currencyType == CurrencyType.TO) {
                        clearFields()
                        currencyTo = currency
                        containerTo.getImageView().setDrawableFlag(context, currency)
                        containerTo.setCurrencyText(currency.getFormattedString())
                    }
                }
                dismiss()
            }
        }

        activity?.supportFragmentManager?.let {
            currencyListFragment.show(it, "currency-list-tag")
        }
    }


    private fun showSnackBar(message: String?) {
        try {
            message?.let {
                Snackbar.make(binding.currencyFromEditText, it, Snackbar.LENGTH_LONG).show()
            }
        } catch (ex: Exception) { Log.e("error", ex.message, ex.cause)}
    }

    private fun clearFields() {
        binding.apply {
            textViewConvertedValue.text = String.empty()
            currencyFromEditText.setText(String.empty())
        }
    }
}