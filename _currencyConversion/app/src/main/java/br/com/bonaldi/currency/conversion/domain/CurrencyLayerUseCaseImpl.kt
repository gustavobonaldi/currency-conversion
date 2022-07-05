package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.api.api.config.ResponseResource
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import br.com.bonaldi.currency.conversion.api.model.RatesModel
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.flow.Flow

class CurrencyLayerUseCaseImpl(
    private val currencyLayerRepository: CurrencyLayerRepository
) : CurrencyLayerUseCase {

    companion object {
        const val RECENTLY_USED_SECTION_SIZE = 5
    }

    override suspend fun updateCurrencyList(): Flow<ResponseResource<List<CurrencyModel>>> {
        return currencyLayerRepository.updateCurrencyList()
    }

    override suspend fun updateCurrencyRateList(): Flow<ResponseResource<List<RatesModel>>> {
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
                                currency.currencyCode,
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

    override fun getCurrencyListFlow(): Flow<List<CurrencyModel>> = currencyLayerRepository.getCurrencyListFlow()
    override fun getCurrentRatesFlow():  Flow<List<RatesModel>> = currencyLayerRepository.getCurrentRatesFlow()
}