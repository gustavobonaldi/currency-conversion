package br.com.bonaldi.currency.conversion.utils.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.LogTags
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.OnSwipeListener

class CustomSwipeButtonConstraintLayout(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs), View.OnTouchListener {
    private var onSwipeListener: ((OnSwipeListener.Direction?) -> Unit)? = null
    private var onItemClickListener: (() -> Unit)? = null
    private var gesture: GestureDetector

    init {
        setOnTouchListener(this)
        gesture = GestureDetector(context,
            object : OnSwipeListener() {
                override fun onSwipe(direction: Direction?): Boolean {
                    onSwipeListener?.invoke(direction)
                    return true
                }
            }
        )
    }

    fun setSwipeListener(onSwipeListener: (OnSwipeListener.Direction?) -> Unit) {
        this.onSwipeListener = onSwipeListener
    }

    fun setOnItemClickListener(onItemClickListener: () -> Unit){
        this.onItemClickListener = onItemClickListener
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val gestureTouch = gesture.onTouchEvent(event)
        if(event?.action == ACTION_UP && !gestureTouch) {
            onItemClickListener?.invoke()
        }
        return gestureTouch
    }
}