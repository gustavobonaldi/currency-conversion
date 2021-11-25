package br.com.bonaldi.currency.conversion.presentation.conversions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel.*
import br.com.bonaldi.currency.conversion.databinding.FragmentConversionsBinding
import br.com.bonaldi.currency.conversion.presentation.BaseFragment
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.CustomTextWatcher
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListFragment
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListener
import br.com.bonaldi.currency.conversion.presentation.extensions.empty
import br.com.bonaldi.currency.conversion.presentation.extensions.getFormattedString
import br.com.bonaldi.currency.conversion.presentation.extensions.setDrawableFlag
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.NumberFormat
import java.util.*

class ConversionsFragment : BaseFragment() {

    private val viewModel: ConversionViewModel by sharedViewModel()
    private lateinit var binding: FragmentConversionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

            NumberFormat.getCurrencyInstance(Locale.US).let { currencyInstance ->
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
            addRealtimeRatesObserver(this@ConversionsFragment) {

            }
            updateRealtimeRates()
        }
    }

    private fun setAds(){
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    private fun getConvertedValue(value: Double) {
        try {
            viewModel.getConversionFromTo(
                viewModel.currencyConversionVO,
                value.toString().toDouble(),
                onSuccess = {
                    binding.apply {
                        textViewConvertedValue.text = it
                        step4.visibility = if (it?.length ?: 0 > 0) View.VISIBLE else View.GONE
                    }
                },
                onError = {
                    showSnackBar(it.info)
                }
            )
        } catch (ex: Exception) {
            when (ex) {
                is NumberFormatException -> {
                    showSnackBar(resources.getString(R.string.type_valid_value))
                }
            }
        }
    }

    private fun showCurrencyList(currencyType: CurrencyType) {
        val currencyListFragment = CurrencyListFragment(currencyType).apply {
            addCurrencyListListener(object: CurrencyListener {
                override fun onCurrencyClicked(currency: CurrencyModel) {
                    viewModel.updateCurrencyRecentlyUsed(currency.currencyCode)
                    binding.apply {
                        when (currencyType) {
                            CurrencyType.FROM -> {
                                clearFields()
                                viewModel.currencyConversionVO.currencyFrom = currency
                                containerFrom.getImageView().setDrawableFlag(context, currency)
                                containerFrom.setCurrencyText(currency.getFormattedString())
                            }
                            CurrencyType.TO -> {
                                clearFields()
                                viewModel.currencyConversionVO.currencyTo = currency
                                containerTo.getImageView().setDrawableFlag(context, currency)
                                containerTo.setCurrencyText(currency.getFormattedString())
                            }
                        }

                    }
                    dismiss()
                }

                override fun onFavoriteClicked(currency: CurrencyModel) {
                    viewModel.updateCurrencyFavorite(currency)
                }
            })
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
        } catch (ex: Exception) {
            Log.e("error", ex.message, ex.cause)
        }
    }

    private fun clearFields() {
        binding.apply {
            textViewConvertedValue.text = String.empty()
            currencyFromEditText.setText(String.empty())
        }
    }
}