package br.com.bonaldi.currency.conversion.presentation.conversions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel.CurrencyType
import br.com.bonaldi.currency.conversion.databinding.FragmentConversionsBinding
import br.com.bonaldi.currency.conversion.presentation.BaseFragment
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel.ConversionUIEvent.*
import br.com.bonaldi.currency.conversion.presentation.currencylist.CurrencyListFragment
import br.com.bonaldi.currency.conversion.utils.controls.CustomTextWatcher
import br.com.bonaldi.currency.conversion.utils.controls.LogTags.LOG_TAG
import br.com.bonaldi.currency.conversion.utils.controls.setIsVisible
import br.com.bonaldi.currency.conversion.utils.extensions.empty
import br.com.bonaldi.currency.conversion.utils.extensions.setDrawableFlag
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.NumberFormat
import java.util.*

class ConversionsFragment : BaseFragment() {

    private val viewModel: ConversionViewModel by sharedViewModel()
    private lateinit var binding: FragmentConversionsBinding

    companion object {
        const val CURRENCY_LIST_FRAGMENT_TAG = "currency_list_fragment_tag"
    }

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

    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
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
                        viewModel.performConversion(
                            requireContext(),
                            typedValue
                        )

                        currencyInstance.format((typedValue)).let { formattedNumber ->
                            setText(formattedNumber)
                            setSelection(formattedNumber.length)
                        }
                    })
                }
            }
        }
    }

    private fun setObservers() = lifecycleScope.launch {
        viewModel.apply {
            updateRealtimeRates()
            conversionEventFlow.collectLatest { event ->
                handleUIEvent(event)
            }
        }
    }

    private fun handleUIEvent(event: ConversionViewModel.ConversionUIEvent) = when (event) {
        is SnackBarError -> showSnackBar(event.message)
        is ShowConvertedValue -> updateConvertedValue(
            event.convertedValue
        )
        is SelectClickedCurrency -> updateSelectedCurrency(
            event.currency,
            event.currencyType
        )
    }

    private fun updateConvertedValue(convertedValue: String) = binding.apply {
        textViewConvertedValue.text = convertedValue
        step4.setIsVisible(convertedValue.isNotBlank())
    }

    private fun updateSelectedCurrency(currency: CurrencyModel, currencyType: CurrencyType) =
        binding.apply {
            when (currencyType) {
                CurrencyType.TO -> {
                    clearFields()
                    containerTo.getImageView().setDrawableFlag(context, currency.currencyCode)
                    containerTo.setCurrencyText(currency.getFormattedString())

                }
                CurrencyType.FROM -> {
                    clearFields()
                    containerFrom.getImageView()
                        .setDrawableFlag(context, currency.currencyCode)
                    containerFrom.setCurrencyText(currency.getFormattedString())

                }
            }
        }

    private fun showCurrencyList(currencyType: CurrencyType) {
        val currencyListFragment = CurrencyListFragment(currencyType)
        activity?.supportFragmentManager?.let {
            currencyListFragment.show(it, CURRENCY_LIST_FRAGMENT_TAG)
        }
    }

    private fun showSnackBar(message: String?) =
        try {
            message?.let {
                Snackbar.make(binding.currencyFromEditText, it, Snackbar.LENGTH_LONG).show()
            }
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.message, ex.cause)
        }


    private fun clearFields() = binding.apply {
        textViewConvertedValue.text = String.empty()
        currencyFromEditText.setText(String.empty())
    }
}