package br.com.btg.btgchallenge.presentation.currencylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.btg.btgchallenge.R
import br.com.btg.btgchallenge.databinding.FragmentCurrencyListBinding
import br.com.btg.btgchallenge.api.api.config.Status
import br.com.btg.btgchallenge.api.api.config.Status.*
import br.com.btg.btgchallenge.presentation.conversions.Currency
import br.com.btg.btgchallenge.presentation.conversions.Currency.CurrencyType
import kotlinx.android.synthetic.main.fragment_currency_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class CurrencyListFragment(
    val currencyType: CurrencyType,
    val listener: (Currency) -> Unit
) : DialogFragment() {

    private val currencyListViewModel: CurrencyListViewModel by viewModel()
    private var adapter: CurrencyAdapter? = null
    private lateinit var binding: FragmentCurrencyListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_currency_list, container, false)
        binding.viewModel = currencyListViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        currencyListViewModel.getCurrencies()
    }

    override fun onResume() {
        super.onResume()
        setWindowSettings()
    }

    private fun setCurrencyList(currencies: Map<String, String>?)
    {
        if(currencies != null && currencies.isNotEmpty()) {
            recycler_currency_list.apply {
                adapter = CurrencyAdapter(currencies, requireContext(), currencyType){
                    listener.invoke(it)
                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recycler_currency_list.adapter = adapter
            }
        }
        else{
            Toast.makeText(requireContext(), resources.getString(R.string.no_data_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun setObservers() {
        currencyListViewModel.getCurrencies.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> {
                    loader_currency_list.visibility = View.GONE
                    recycler_currency_list.visibility = View.VISIBLE
                    setCurrencyList(it.data?.currencies)
                    setSearchView()
                }
                ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                LOADING -> {
                    loader_currency_list.visibility = View.VISIBLE
                    recycler_currency_list.visibility = View.GONE
                }
            }
        })
    }

    private fun setSearchView()
    {
        currency_search_view.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
    }

    private fun setWindowSettings()
    {
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}