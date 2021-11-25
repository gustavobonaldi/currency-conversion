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
import br.com.bonaldi.currency.conversion.api.model.CurrencyModel
import java.util.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
  itemView.setOnClickListener {
    event.invoke(adapterPosition, itemViewType)
  }
  return this
}

fun ImageView.setDrawableFlag(context: Context?, currency: CurrencyModel){
  val uri = "@drawable/flag_" + currency.currencyCode?.lowercase(Locale.getDefault())
  context?.let {
    var imageResource: Int =
      it.resources.getIdentifier(uri, null, it.packageName)
    if (imageResource == 0) {
      imageResource = R.drawable.globe
    }
    setImageResource(imageResource)
  }
}

fun CurrencyModel.getFormattedString(): String{
  return ("$currencyCode - $currencyCountry")
}

fun String.Companion.empty() = ""

fun CurrencyModel.getCurrencyMapped(): CurrencyModel {
  return CurrencyModel(currencyCode, currencyCountry)
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

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
  return if (p1 != null && p2 != null) block(p1, p2) else null
}
inline fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
  return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
inline fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
  return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
inline fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
  return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}
