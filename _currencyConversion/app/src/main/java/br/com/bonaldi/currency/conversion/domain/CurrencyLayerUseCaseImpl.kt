package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository

class CurrencyLayerUseCaseImpl(private val currencyLayerRepository: CurrencyLayerRepository): CurrencyLayerUseCase {

    companion object {
        const val RECENTLY_USED_SECTION_SIZE = 5
    }

    override suspend fun updateCurrencyList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<CurrencyDTO>) -> Unit
    ) {

        currencyLayerRepository.updateCurrencyList(
            shouldShowLoading,
            onError,
            onSuccess
        )
    }

    override suspend fun updateCurrencyRateList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<RatesDTO>) -> Unit
    ) {
        currencyLayerRepository.updateCurrencyRateList(
            shouldShowLoading,
            onError,
            onSuccess
        )
    }

    override suspend fun updateCurrencyRecentlyUsed(currencyCode: String) {
        currencyLayerRepository.selectRecentlyUsedCurrencies().let { recentlyUsedCurrencies ->
            if(recentlyUsedCurrencies.isNullOrEmpty() || recentlyUsedCurrencies.size < RECENTLY_USED_SECTION_SIZE){
                currencyLayerRepository.updateCurrencyRecentlyUsed(currencyCode, true)
            }
            else {
                for(currency in recentlyUsedCurrencies.subList(RECENTLY_USED_SECTION_SIZE-1, recentlyUsedCurrencies.lastIndex)){
                    currencyLayerRepository.updateCurrencyRecentlyUsed(currency.currencyCode, false)
                }
                currencyLayerRepository.updateCurrencyRecentlyUsed(currencyCode, true)
            }
        }
    }

    override suspend fun updateFavoriteCurrency(currencyCode: String, isFavorite: Boolean) {
        currencyLayerRepository.updateFavoriteCurrency(currencyCode, isFavorite)
    }

    override fun getCurrencyListLiveData() = currencyLayerRepository.getCurrencyListLiveData()
    override fun getCurrencyRateListLiveData() = currencyLayerRepository.getCurrencyRateListLiveData()
}