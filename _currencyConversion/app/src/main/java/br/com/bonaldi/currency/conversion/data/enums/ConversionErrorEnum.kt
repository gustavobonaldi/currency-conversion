package br.com.bonaldi.currency.conversion.data.enums

import androidx.annotation.StringRes
import br.com.bonaldi.currency.conversion.R

enum class ConversionErrorEnum(@StringRes val message: Int) {
    INVALID_VALUE_TYPED_ERROR(R.string.type_valid_value),
    CURRENCIES_NOT_SELECTED_ERROR(R.string.currencies_not_selected),
}