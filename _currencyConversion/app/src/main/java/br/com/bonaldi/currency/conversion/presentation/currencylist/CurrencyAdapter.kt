package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.databinding.CurrencyItemBinding
import br.com.bonaldi.currency.conversion.utils.customcomponents.BaseSwipeViewHolder
import br.com.bonaldi.currency.conversion.utils.extensions.setDrawableFlag
import java.util.*


class CurrencyAdapter(
    val context: Context,
    private val currencyType: CurrencyModel.CurrencyType,
    private val onItemClicked: ((CurrencyModel) -> Unit)?,
    private val onFavoriteItemClicked: ((CurrencyModel) -> Unit)?
) : ListAdapter<CurrencyModel, CurrencyAdapter.CurrencyHolder>(CurrencyAdapter.CurrencyDiffer), Filterable {

    private var currencies: List<CurrencyModel> = listOf()
    var filteredCurrencies: List<CurrencyModel>

    init {
        filteredCurrencies = currencies
    }

    fun addItems(currencyList: List<CurrencyModel>) {
        filteredCurrencies = currencyList
        submitList(currencyList){
            currencies = currentList
            filteredCurrencies = currentList
        }
    }

    fun setFilteredItems(currencyList: List<CurrencyModel>) {
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
                        it.currencyCode.lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(Locale.getDefault())) ||
                        it.currencyCountry.orEmpty().lowercase(Locale.getDefault()).contains(constraint.toString().lowercase(Locale.getDefault()))
                    }
                }

                return FilterResults().apply {
                    values = filteredCurrencies
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as? List<CurrencyModel>)?.let {
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
        fun bindName(currency: CurrencyModel, position: Int, context: Context) {
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

        private fun setupCurrencyItem(currency: CurrencyModel) = binding.apply {
            currencyName.text = currency.currencyCode
            currencyCountry.text = currency.currencyCountry
            currencyCountryImage.setDrawableFlag(context, currency.currencyCode)
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

        private fun CurrencyModel.showRecentlyUsedHeader(position: Int) =
            position == 0 && !currentList.isNullOrEmpty() && recentlyUsed

        private fun CurrencyModel.showCurrencyListHeader(position: Int) =
            position > 0 && currentList.size > 1 && currentList[position - 1].recentlyUsed && currentList.size - 1 > position && !recentlyUsed
    }

    private fun getFavoriteImage(currency: CurrencyModel): Int {
        return if (currency.isFavorite) R.drawable.ic_star_to_favorite else R.drawable.ic_star_favorited
    }

    private object CurrencyDiffer: DiffUtil.ItemCallback<CurrencyModel>(){
        override fun areItemsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
            return oldItem.currencyCode == newItem.currencyCode
        }

        override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean {
            return oldItem == newItem
        }
    }
}