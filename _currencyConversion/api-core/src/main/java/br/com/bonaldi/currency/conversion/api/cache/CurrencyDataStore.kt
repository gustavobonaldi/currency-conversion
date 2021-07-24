package br.com.bonaldi.currency.conversion.api.cache

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

interface CurrencyDataStore {
    suspend fun <T> writeValue(value: T, prefs: Preferences.Key<T>)
    suspend fun <T> observeValue(prefs: Preferences.Key<T>, observer: (T?) -> Unit)
    suspend fun writeSetOfString(value: String, prefs: Preferences.Key<Set<String>>, onWritten: () -> Unit)

    companion object {
        val RECENT_CURRENCIES_ID = stringSetPreferencesKey("RECENT_CURRENCIES_ID")
    }
}