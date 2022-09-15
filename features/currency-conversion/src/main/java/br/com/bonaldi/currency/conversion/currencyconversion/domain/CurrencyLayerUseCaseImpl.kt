package br.com.bonaldi.currency.conversion.currencyconversion.domain

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Currency
import br.com.bonaldi.currency.conversion.core.database.model.conversion.Rates
import br.com.bonaldi.currency.conversion.core.database.model.history.ConversionHistory
import br.com.bonaldi.currency.conversion.currencyconversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyLayerUseCaseImpl @Inject constructor(
    private val currencyLayerRepository: CurrencyLayerRepository
) : CurrencyLayerUseCase {
    private var lastConversionItem: ConversionHistory? = null

    companion object {
        const val RECENTLY_USED_SECTION_SIZE = 5
    }

    override suspend fun updateCurrencyList(): Flow<ResponseResource<List<Currency>>> {
        return currencyLayerRepository.updateCurrencyList()
    }

    override suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<Rates>>> {
        return currencyLayerRepository.updateCurrencyRateList()
    }

    override suspend fun updateRecentlyUsedCurrency(currencyCode: String) {
        currencyLayerRepository.selectRecentlyUsedCurrencies().let { recentlyUsedCurrencies ->
            when {
                recentlyUsedCurrencies.isNullOrEmpty() || (recentlyUsedCurrencies.size < (RECENTLY_USED_SECTION_SIZE - 1)) -> {
                    currencyLayerRepository.updateRecentlyUsedCurrency(currencyCode, true)
                }
                else -> {
                    recentlyUsedCurrencies.mapIndexed { index, currency ->
                        if (index > (RECENTLY_USED_SECTION_SIZE - 2)) {
                            currencyLayerRepository.updateRecentlyUsedCurrency(
                                currency.code,
                                false
                            )
                        }
                    }
                    currencyLayerRepository.updateRecentlyUsedCurrency(currencyCode, true)
                }
            }
        }
    }

    override suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean) {
        currencyLayerRepository.updateFavoriteCurrency(currencyCode, isFavorite)
    }

    override suspend fun updateConversionHistory(origin: Currency, destiny: Currency) {
        if(lastConversionItem?.id != ConversionHistory.generateId(origin.code, destiny.code)) {
            currencyLayerRepository.updateConversionHistory(origin, destiny)
        }
    }

    override fun getCurrencyListFlow(): Flow<List<Currency>> = currencyLayerRepository.getCurrencyListFlow()
    override fun getCurrentRatesFlow():  Flow<List<Rates>> = currencyLayerRepository.getCurrentRatesFlow()
}