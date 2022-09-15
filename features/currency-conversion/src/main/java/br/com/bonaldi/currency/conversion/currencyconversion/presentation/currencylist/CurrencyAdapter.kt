package br.com.bonaldi.currency.conversion.currencyconversion.presentation.currencylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.currencyconversion.R
import br.com.bonaldi.currency.conversion.currencyconversion.databinding.CurrencyItemBinding
import br.com.bonaldi.currency.conversion.utils.customcomponents.BaseSwipeViewHolder
import br.com.bonaldi.currency.conversion.utils.extensions.setDrawableFlag
import java.util.*

class CurrencyAdapter(
    val context: Context,
    private val currencyType: Currency.CurrencyType,
    private val onItemClicked: ((Currency) -> Unit)?,
    private val onFavoriteItemClicked: ((Currency) -> Unit)?
) : ListAdapter<Currency, CurrencyAdapter.CurrencyHolder>(CurrencyDiffer), Filterable {

    private var currencies: List<Currency> = listOf()
    var filteredCurrencies: List<Currency>

    init {
        filteredCurrencies = currencies
    }

    fun addItems(currencyList: List<Currency>) {
        filteredCurrencies = currencyList
        submitList(currencyList){
            currencies = currentList
            filteredCurrencies = currentList
        }
    }

    fun setFilteredItems(currencyList: List<Currency>) {
        filteredCurrencies = currencyList
        submitList(currencyList){
            filteredCurrencies = currentList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val binding = CurrencyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CurrencyHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val itemCurrency = currentList.toList()[position]
        holder.bindName(itemCurrency, position, context)
    }

    override fun getItemCount(): Int = currentList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                constraint?.takeIf { it.isEmpty() }?.let {
                    filteredCurrencies = currencies
                } ?: kotlin.run {
                    filteredCurrencies = currencies.filter {
                        it.code.lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(Locale.getDefault())) ||
                        it.country.orEmpty().lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(Locale.getDefault()))
                    }
                }

                return FilterResults().apply {
                    values = filteredCurrencies
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as? List<Currency>)?.let {
                    filteredCurrencies = it
                    setFilteredItems(it)
                }
            }
        }
    }

    inner class CurrencyHolder(
        private val binding: CurrencyItemBinding,
        private val parent: ViewGroup
    ) : BaseSwipeViewHolder<CurrencyItemBinding>(binding, parent) {
        fun bindName(currency: Currency, position: Int, context: Context) {
            super.bindItem()
            binding.headerContainer.visibility = View.GONE
            when {
                currency.showRecentlyUsedHeader(position) -> showRecentlyUsedHeader()
                currency.showCurrencyListHeader(position) -> showListHeader()
            }

            setupCurrencyItem(currency)
        }

        private fun showListHeader() = binding.apply {
            binding.headerContainer.visibility = View.VISIBLE
            binding.tvHeaderItem.text = context.resources.getString(R.string.all_currencies)
        }

        private fun showRecentlyUsedHeader() = binding.apply {
            binding.headerContainer.visibility = View.VISIBLE
            binding.tvHeaderItem.text =
                context.resources.getString(R.string.recently_used_currencies)
        }

        private fun setupCurrencyItem(currency: Currency) = binding.apply {
            currencyName.text = currency.code
            currencyCountry.text = currency.country
            currencyCountryImage.setDrawableFlag(context, currency.code)
            setButtonDrawable(getFavoriteImage(currency))
        }

        override fun onPrimaryButtonClicked(position: Int, favoriteImage: ImageView) {
            favoriteImage.setImageResource(getFavoriteImage(currentList[position]))
            onFavoriteItemClicked?.invoke(currentList[position])
            notifyItemChanged(position)
        }

        override fun onItemClicked(position: Int) {
            super.onItemClicked(position)
            currentList[position].selectionType = currencyType
            onItemClicked?.invoke(currentList[position])
        }

        private fun Currency.showRecentlyUsedHeader(position: Int) =
            position == 0 && !currentList.isNullOrEmpty() && isRecentUse

        private fun Currency.showCurrencyListHeader(position: Int) =
            position > 0 && currentList.size > 1 && currentList[position - 1].isRecentUse && currentList.size - 1 > position && !isRecentUse
    }

    private fun getFavoriteImage(currency: Currency): Int {
        return if (currency.isFavorite) R.drawable.ic_star_to_favorite else R.drawable.ic_star_favorited
    }

    private object CurrencyDiffer: DiffUtil.ItemCallback<Currency>(){
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }
    }
}