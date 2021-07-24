/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package br.com.bonaldi.currency.conversion.presentation.extensions


import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import br.com.bonaldi.currency.conversion.R
import br.com.bonaldi.currency.conversion.api.dto.CurrencyDTO2

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
  itemView.setOnClickListener {
    event.invoke(getAdapterPosition(), getItemViewType())
  }
  return this
}

fun ImageView.setDrawableFlag(context: Context?, currency: CurrencyDTO2){
  val uri = "@drawable/flag_" + currency.currencyCode.toLowerCase()
  context?.let {
    var imageResource: Int =
      it.resources.getIdentifier(uri, null, it.getPackageName())
    if (imageResource == 0) {
      imageResource = R.drawable.globe
    }
    setImageResource(imageResource)
  }
}

fun CurrencyDTO2.getFormattedString(): String{
  return ("$currencyCode - $currencyCountry")
}

fun String.Companion.empty() = ""

fun CurrencyDTO2.getCurrencyMapped(): CurrencyDTO2 {
  return CurrencyDTO2(currencyCode, currencyCountry)
}

fun Dialog.setWindowSettings(){
  window?.attributes?.let {winAttrs ->
    val params: ViewGroup.LayoutParams = winAttrs
    params.width = ViewGroup.LayoutParams.MATCH_PARENT
    params.height = ViewGroup.LayoutParams.MATCH_PARENT
    window?.attributes = params as WindowManager.LayoutParams
  }

}

fun Double.Companion.Zero() = 0.0
