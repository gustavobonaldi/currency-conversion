package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.databinding.FragmentCurrencyListBinding
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency.CurrencyType
import org.koin.android.viewmodel.ext.android.viewModel


class CurrencyListFragment(private val currencyType: CurrencyType) : DialogFragment() {

    private val currencyListViewModel: CurrencyListViewModel by viewModel()
    private var onCurrencyClicked: ((Currency) -> Unit?)? = null
    private var listAdapter: CurrencyAdapter? = null
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
        setWindowSettings()
    }

    fun addOnCurrencyClickedListener(listener: ((Currency) -> Unit?)?){
        onCurrencyClicked = listener
    }

    private fun setCurrencyList(currencies: Map<String, String>?) {
        if (currencies != null && currencies.isNotEmpty()) {
            binding.recyclerCurrencyList.apply {
                listAdapter = CurrencyAdapter(currencies, requireContext(), currencyType) {
                    onCurrencyClicked?.invoke(it)
                }

                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = listAdapter
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
        currencyListViewModel.apply {
            addCurrenciesObserver(this@CurrencyListFragment) { currencies ->
                binding.loaderCurrencyList.visibility = View.GONE
                binding.recyclerCurrencyList.visibility = View.VISIBLE
                setCurrencyList(currencies.currencies)
                setSearchView()
            }
            updateCurrencies()
        }
    }

    private fun setSearchView() {
        binding.currencySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    private fun setWindowSettings() {
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}