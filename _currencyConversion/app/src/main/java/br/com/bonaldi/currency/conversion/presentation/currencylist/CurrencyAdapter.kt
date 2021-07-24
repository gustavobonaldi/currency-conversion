package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2
import br.com.bonaldi.currency.conversion.databinding.CurrencyItemBinding
import br.com.bonaldi.currency.conversion.presentation.extensions.listen
import br.com.bonaldi.currency.conversion.presentation.extensions.setDrawableFlag
import java.util.*


class CurrencyAdapter(
    val context: Context,
    private val currencyType: CurrencyDTO2.CurrencyType,
    private val onItemClicked: ((CurrencyDTO2) -> Unit)?) : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>(), Filterable {

    private var currencies: List<CurrencyDTO2> = listOf()
    var filteredCurrencies: List<CurrencyDTO2>

    init {
        filteredCurrencies = currencies
    }

    override fun getItemCount(): Int = filteredCurrencies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val binding = CurrencyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CurrencyHolder(binding).listen { pos, type ->
            currencies[pos].type = currencyType
            onItemClicked?.invoke(currencies[pos])
        }
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val itemCurrency = filteredCurrencies.toList()[position]
        holder.bindName(itemCurrency, context)
    }

    class CurrencyHolder(private val binding: CurrencyItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindName(currency: CurrencyDTO2, context: Context) {
            binding.apply {
                currencyName.text = currency.currencyCode
                currencyCountry.text = currency.currencyCountry
                currencyCountryImage.setDrawableFlag(context, currency)
            }
        }
    }

    fun addItems(currencyList: List<CurrencyDTO2>){
        currencies = currencyList
        filteredCurrencies = currencyList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                constraint?.takeIf { it.isEmpty() }?.let {
                    filteredCurrencies = currencies
                } ?: kotlin.run {
                    filteredCurrencies = currencies.filter {
                        it.currencyCode == constraint.toString()
                    }
                }

                return FilterResults().apply {
                    values = filteredCurrencies
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as? List<CurrencyDTO2>)?.let {
                    filteredCurrencies = it
                    notifyDataSetChanged()
                }
            }
        }
    }
}