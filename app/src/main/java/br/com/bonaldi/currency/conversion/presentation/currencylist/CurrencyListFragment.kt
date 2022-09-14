package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonaldi.currency.conversion.core.database.model.CurrencyModel
import br.com.bonaldi.currency.conversion.databinding.FragmentCurrencyListBinding
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.utils.extensions.setWindowSettings
import br.com.bonaldi.currency.conversion.utils.controls.setIsVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyListFragment(private val currencyType: CurrencyModel.CurrencyType) : DialogFragment() {
    private lateinit var binding: FragmentCurrencyListBinding
    private val viewModel: ConversionViewModel by activityViewModels()

    private val listAdapter: CurrencyAdapter by lazy {
        CurrencyAdapter(
            requireContext(),
            currencyType,
            ::onCurrencyClicked,
            ::onFavoriteClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        dialog?.setWindowSettings()
    }

    private fun setCurrencyList(currencies: List<CurrencyModel>) {
        binding.recyclerCurrencyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            listAdapter.addItems(currencies)
        }
    }

    private fun setObservers() = binding.apply {
        lifecycleScope.launch {
            viewModel.currencyListState.collectLatest { currencies ->
                loaderCurrencyList.setIsVisible(currencies.isLoading)
                recyclerCurrencyList.setIsVisible(!currencies.isLoading)
                setCurrencyList(currencies.currencyList)
                setSearchView()
            }
        }
        viewModel.updateCurrencies()
    }

    private fun setSearchView() {
        binding.currencySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun onCurrencyClicked(currency: CurrencyModel) {
        viewModel.updateCurrencyRecentlyUsed(currency, currencyType)
        dismiss()
    }

    private fun onFavoriteClicked(currency: CurrencyModel) {
        viewModel.updateCurrencyFavorite(currency)
    }
}