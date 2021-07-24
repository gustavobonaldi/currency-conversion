package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2
import br.com.bonaldi.currency.conversion.databinding.FragmentCurrencyListBinding
import br.com.bonaldi.currency.conversion.presentation.ConversionViewModel
import br.com.bonaldi.currency.conversion.presentation.extensions.setWindowSettings
import org.koin.android.viewmodel.ext.android.sharedViewModel


class CurrencyListFragment(private val currencyType: CurrencyDTO2.CurrencyType) : DialogFragment() {
    private val viewModel: ConversionViewModel by sharedViewModel()
    private var onCurrencyClicked: ((CurrencyDTO2) -> Unit)? = null
    private val listAdapter: CurrencyAdapter by lazy { CurrencyAdapter(requireContext(), currencyType, onCurrencyClicked) }
    private lateinit var binding: FragmentCurrencyListBinding

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

    fun addOnCurrencyClickedListener(listener: ((CurrencyDTO2) -> Unit)?){
        onCurrencyClicked = listener
    }

    private fun setCurrencyList(currencies: List<CurrencyDTO2>) {
        if (currencies.isNotEmpty()) {
            binding.recyclerCurrencyList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listAdapter
                listAdapter.addItems(currencies)
            }
        } else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.no_data_found),
                Toast.LENGTH_LONG
            ).show()
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
}