package br.com.bonaldi.currency.conversion.domain

import androidx.lifecycle.LiveData
import br.com.bonaldi.currency.conversion.api.api.config.Resource
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.SupportedCurrenciesDTO
import br.com.bonaldi.currency.conversion.api.dto.currency.ExchangeRateDataDTO
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyLayerUseCaseImpl(private val currencyLayerRepository: CurrencyLayerRepository): CurrencyLayerUseCase {

    override suspend fun updateCurrencyList(
        shouldShowLoading: (Boolean) -> Unit,
        onError: (ErrorDTO) -> Unit,
        onSuccess: (List<CurrencyDTO2>) -> Unit
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

    override fun getCurrencyListLiveData() = currencyLayerRepository.getCurrencyListLiveData()
    override fun getCurrencyRateListLiveData() = currencyLayerRepository.getCurrencyRateListLiveData()
}