package br.com.bonaldi.currency.conversion.api.cache

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class CurrencyDataStoreImpl(val context: Context): CurrencyDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "currencyPreferenceStore")

    override suspend fun <T> writeValue(value: T, prefs: Preferences.Key<T>) {
        context.dataStore.edit { settings ->
            settings[prefs] = value
        }
    }

    override suspend fun writeSetOfString(value: String, prefs: Preferences.Key<Set<String>>, onWritten: () -> Unit) {
        context.dataStore.edit { settings ->
            if(settings[prefs] == null){
                settings[prefs] = setOf()
            }
            settings[prefs]?.plus(value)
            onWritten.invoke()
        }
    }

    override suspend fun <T> observeValue(prefs: Preferences.Key<T>, observer: (T?) -> Unit) {
        context.dataStore.data.catch { exception ->
            if (exception is IOException) { // 2
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            observer.invoke(preferences[prefs])
        }
    }
}