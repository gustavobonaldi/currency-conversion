package br.com.bonaldi.currency.conversion.utils.controls

import android.view.View

object LogTags{
    const val LOG_TAG = "GUSTAVO"
}

fun View.setVerticalScroll(){
    val onSwipeListener: OnSwipeListener = OnSwipeListener()
}

fun View.setIsVisible(isVisible: Boolean){
    visibility = if(isVisible) View.VISIBLE else View.GONE
}