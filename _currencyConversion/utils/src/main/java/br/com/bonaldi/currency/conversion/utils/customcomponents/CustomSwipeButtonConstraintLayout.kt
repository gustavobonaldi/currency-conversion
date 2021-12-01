package br.com.bonaldi.currency.conversion.utils.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import br.com.bonaldi.currency.conversion.utils.R
import br.com.bonaldi.currency.conversion.utils.controls.OnSwipeListener

class CustomSwipeButtonConstraintLayout(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs), View.OnTouchListener {
    private var onSwipeListener: ((OnSwipeListener.Direction?) -> Unit)? = null
    private var onItemClickListener: (() -> Unit)? = null
    private var gesture: GestureDetector
    private var velocityTracker: VelocityTracker? = VelocityTracker.obtain()
    private val PIXEL_PER_SECOND = 1000
    private var swipeMaxVelocity = 0f
    private var swipeScapeVelocity = 0f

    init {
        swipeScapeVelocity = context.resources.getDimension(R.dimen.item_touch_helper_swipe_escape_velocity)
        swipeMaxVelocity = context.resources.getDimension(R.dimen.item_touch_helper_swipe_escape_max_velocity)
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

        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                requestDisallowInterceptTouchEvent(false)
                velocityTracker?.clear()
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                velocityTracker?.addMovement(event)

            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.apply {
                    addMovement(event)
                    computeCurrentVelocity(PIXEL_PER_SECOND)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->{
                velocityTracker?.recycle()
                requestDisallowInterceptTouchEvent(true)
                velocityTracker = null
            }
        }

        val gestureTouch = gesture.onTouchEvent(event)
        if(event?.action == ACTION_UP && !gestureTouch) {
            onItemClickListener?.invoke()
        }
        return gestureTouch
    }
}