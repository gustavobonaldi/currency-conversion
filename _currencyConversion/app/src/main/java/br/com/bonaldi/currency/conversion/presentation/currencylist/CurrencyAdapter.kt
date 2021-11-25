package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.databinding.CurrencyItemBinding
import br.com.bonaldi.currency.conversion.presentation.extensions.listen
import br.com.bonaldi.currency.conversion.presentation.extensions.setDrawableFlag


class CurrencyAdapter(
    val context: Context,
    private val currencyType: CurrencyDTO.CurrencyType,
    private val onItemClicked: ((CurrencyDTO) -> Unit)?,
    private val onFavoriteItemClicked: ((CurrencyDTO) -> Unit)?
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>(), Filterable {

    private var currencies: List<CurrencyDTO> = listOf()
    var filteredCurrencies: List<CurrencyDTO>

    init {
        filteredCurrencies = currencies
    }

    fun addItems(currencyList: List<CurrencyDTO>) {
        currencies = currencyList
        filteredCurrencies = currencyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val binding = CurrencyItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CurrencyHolder(binding, parent)
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val itemCurrency = filteredCurrencies.toList()[position]
        holder.bindName(itemCurrency, position, context)
    }

    override fun getItemCount(): Int = filteredCurrencies.size

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
                (results?.values as? List<CurrencyDTO>)?.let {
                    filteredCurrencies = it
                    notifyDataSetChanged()
                }
            }
        }
    }

    inner class CurrencyHolder(
        private val binding: CurrencyItemBinding,
        private val parent: ViewGroup
    ) : BaseSwipeViewHolder<CurrencyItemBinding>(binding, parent) {
        fun bindName(currency: CurrencyDTO, position: Int, context: Context) {
            super.bindItem()
            binding.headerContainer.visibility = View.GONE
            when {
                position == 0 && !currencies.isNullOrEmpty() && currency.recentlyUsed -> {
                    binding.headerContainer.visibility = View.VISIBLE
                    binding.tvHeaderItem.text =
                        context.resources.getString(R.string.recently_used_currencies)
                }
                position > 0 && currencies.size > 1 && currencies[position - 1].recentlyUsed && currencies.size - 1 > position && !currency.recentlyUsed -> {
                    binding.headerContainer.visibility = View.VISIBLE
                    binding.tvHeaderItem.text = context.resources.getString(R.string.all_currencies)
                }
            }

            binding.apply {
                currencyName.text = currency.currencyCode
                currencyCountry.text = currency.currencyCountry
                currencyCountryImage.setDrawableFlag(context, currency)
                setButtonDrawable(getFavoriteImage(currency))
            }
        }

        override fun onPrimaryButtonClicked(position: Int, favoriteImage: ImageView) {
            favoriteImage.setImageResource(getFavoriteImage(currencies[position]))
            onFavoriteItemClicked?.invoke(currencies[position])
            notifyItemChanged(position)
        }

        override fun onItemClicked(position: Int) {
            super.onItemClicked(position)
            currencies[position].selectionType = currencyType
            onItemClicked?.invoke(currencies[position])
        }
    }

    private fun getFavoriteImage(currency: CurrencyDTO): Int {
        return if (currency.isFavorite) R.drawable.ic_star_to_favorite else R.drawable.ic_star_favorited
    }
}