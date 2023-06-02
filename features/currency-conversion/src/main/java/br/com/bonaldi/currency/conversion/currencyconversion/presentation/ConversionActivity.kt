package br.com.bonaldi.currency.conversion.currencyconversion.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import br.com.bonaldi.currency.conversion.currencyconversion.R
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.navigation.ConversionApp
import br.com.bonaldi.currency.conversion.currencyconversion.presentation.navigation.ConversionNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversionActivity : ComponentActivity() {
    private val viewModel: ConversionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //MobileAds.initialize(this) { }
        setContent {
            ConversionApp(viewModel)
        }
    }
}