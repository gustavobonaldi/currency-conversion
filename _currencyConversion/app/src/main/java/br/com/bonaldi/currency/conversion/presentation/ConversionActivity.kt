package br.com.bonaldi.currency.conversion.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.bonaldi.currency.conversion.R
import com.google.android.gms.ads.MobileAds
import org.koin.android.viewmodel.ext.android.viewModel


class ConversionActivity : AppCompatActivity() {
    private val viewModel: ConversionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) { }
    }
}