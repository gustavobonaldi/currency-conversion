package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.databinding.FragmentCurrencyListBinding
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.presentation.extensions.setWindowSettings
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencyListFragment(private val currencyType: CurrencyModel.CurrencyType) : DialogFragment() {
    private lateinit var binding: FragmentCurrencyListBinding
    private val viewModel: ConversionViewModel by sharedViewModel()

    private var onCurrencyListener: CurrencyListener? = null

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

    fun addCurrencyListListener(currencyListener: CurrencyListener) {
        onCurrencyListener = currencyListener
    }

    private fun setCurrencyList(currencies: List<CurrencyModel>) {
        binding.recyclerCurrencyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            listAdapter.addItems(currencies)
        }
    }

    private fun setObservers() {
        viewModel.addCurrenciesObserver(this@CurrencyListFragment) { currencies ->
            binding.loaderCurrencyList.visibility = View.GONE
            binding.recyclerCurrencyList.visibility = View.VISIBLE
            currencies?.let {
                setCurrencyList(it)
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
        onCurrencyListener?.onCurrencyClicked(currency)
    }

    private fun onFavoriteClicked(currency: CurrencyModel) {
        onCurrencyListener?.onFavoriteClicked(currency)
    }
}

interface CurrencyListener {
    fun onCurrencyClicked(currency: CurrencyModel)
    fun onFavoriteClicked(currency: CurrencyModel)
}