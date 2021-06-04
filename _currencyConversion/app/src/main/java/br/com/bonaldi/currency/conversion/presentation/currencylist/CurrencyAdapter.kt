package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.com.bonaldi.currency.conversion.databinding.CurrencyItemBinding
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency
import br.com.bonaldi.currency.conversion.presentation.conversions.Currency.CurrencyType
import br.com.bonaldi.currency.conversion.presentation.extensions.getCurrencyMapped
import br.com.bonaldi.currency.conversion.presentation.extensions.listen
import br.com.bonaldi.currency.conversion.presentation.extensions.setDrawableFlag
import kotlin.properties.Delegates


class CurrencyAdapter(
    val context: Context,
    val currencyType: CurrencyType,
    val onItemClicked: ((Currency) -> Unit)?) : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>(), Filterable {

    private var currencies: Map<String, String> = mapOf()
    var filteredCurencies : Map<String, String>

    init {
        filteredCurencies = currencies
    }

    override fun getItemCount(): Int = filteredCurencies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val binding = CurrencyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CurrencyHolder(binding, context).listen { pos, type ->
            onItemClicked?.invoke(Currency(filteredCurencies.toList().get(pos), currencyType))
        }
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val itemCurrency = filteredCurencies.toList()[position]
        holder.bindName(itemCurrency, context)
    }

    class CurrencyHolder(private val binding: CurrencyItemBinding, context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bindName(currency: Pair<String, String>, context: Context) {
            binding.apply {
                currencyName.text = currency.first
                currencyCountry.text = currency.second
                currencyCountryImage.setDrawableFlag(context, currency.getCurrencyMapped())
            }
        }
    }

    fun addItems(currencyList: Map<String, String>){
        currencies = currencyList
        filteredCurencies = currencyList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                constraint?.takeIf { it.isEmpty() }?.let {
                    filteredCurencies = currencies
                } ?: kotlin.run {
                    filteredCurencies = currencies.filterValues { it.toLowerCase().contains(constraint.toString().toLowerCase()) }
                }

                return FilterResults().apply {
                    values = filteredCurencies
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as? Map<String, String>)?.let {
                    filteredCurencies = it
                    notifyDataSetChanged()
                }
            }
        }
    }
}