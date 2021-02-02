package br.com.btg.btgchallenge.utils.customcomponents

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import br.com.btg.btgchallenge.utils.R
import br.com.btg.btgchallenge.utils.databinding.CustomCardComponentBinding

class CustomCardCurrencyComponent(context: Context, attrs: AttributeSet? = null) :
    CardView(context, attrs) {
    private val customCardBinding =
        CustomCardComponentBinding.inflate(LayoutInflater.from(context), this)

    init {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent_blue))
        cardElevation = 0f
        radius = resources.getDimension(R.dimen.radiusCardComponent)
        attrs?.let { attributeSet ->
            val typedArray = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomCardCurrencyComponent,
                0,
                0
            )

            typedArray.getString(R.styleable.CustomCardCurrencyComponent_step)?.let {
                setStepText(it)
            }

            typedArray.getString(R.styleable.CustomCardCurrencyComponent_currencyName)?.let {
                setCurrencyText(it)
            }

            typedArray.getResourceId(R.styleable.CustomCardCurrencyComponent_currencyImage, 0)
                .takeIf { it != 0 }
                ?.let {
                    setCurrencyImage(it)
                }
        }
    }


    private fun setStepText(text: String) {
        customCardBinding.stepNumber.text = text
    }

    private fun setStepText(text: Int) {
        customCardBinding.stepNumber.setText(text)
    }

    private fun setCurrencyImage(drawable: Int) {
        customCardBinding.apply {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawable))
        }
    }

    fun setCurrencyImage(drawable: Drawable) {
        customCardBinding.apply {
            imageView.setImageDrawable(drawable)
        }
    }

    fun setCurrencyText(text: String) {
        customCardBinding.textView.text = text
    }

    private fun setCurrencyText(text: Int) {
        customCardBinding.textView.setText(text)
    }

    fun getImageView() = customCardBinding.imageView
}