package br.com.bonaldi.currency.conversion.domain

import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO
import br.com.bonaldi.currency.conversion.api.dto.ErrorDTO
import br.com.bonaldi.currency.conversion.api.dto.RatesDTO
import br.com.bonaldi.currency.conversion.data.repository.CurrencyLayerRepository

class CurrencyLayerUseCaseImpl(private val currencyLayerRepository: CurrencyLayerRepository): CurrencyLayerUseCase {

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

    override fun getCurrencyListLiveData() = currencyLayerRepository.getCurrencyListLiveData()
    override fun getCurrencyRateListLiveData() = currencyLayerRepository.getCurrencyRateListLiveData()
}