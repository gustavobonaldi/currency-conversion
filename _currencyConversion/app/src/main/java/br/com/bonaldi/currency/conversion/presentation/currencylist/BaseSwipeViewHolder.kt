package br.com.bonaldi.currency.conversion.presentation.currencylist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.LogTags
import br.com.bonaldi.currency.conversion.utils.customcomponents.controls.OnSwipeListener
import br.com.bonaldi.currency.conversion.utils.databinding.BaseViewHolderLayoutBinding

open class BaseSwipeViewHolder<T : ViewBinding>(
    private val binding: T,
    private val parent: ViewGroup,
    private val baseViewBinding: BaseViewHolderLayoutBinding = BaseViewHolderLayoutBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ),
    override var isSwiped: Boolean = false
) : RecyclerView.ViewHolder(baseViewBinding.root), BaseSwipeHolder {

    init {
        baseViewBinding.swipeableLayout.addView(binding.root)
        addListeners()
        addSwipeListener()
    }

    private fun addSwipeListener() {
        baseViewBinding.apply {
            rootView.setSwipeListener { direction ->
                Log.d(LogTags.LOG_TAG, "item swiped - Direction: $direction")
                if(direction == OnSwipeListener.Direction.left) {
                    swipeableLayout.animate().translationX(-swipeOptions.width.toFloat())
                    isSwiped = true
                }
                else if(direction == OnSwipeListener.Direction.right){
                    swipeableLayout.animate().translationX(0f)
                    isSwiped = false
                }
            }
        }
    }


    private fun addListeners() {
        baseViewBinding.apply {
            rootView.setOnItemClickListener {
                Log.d(LogTags.LOG_TAG, "rootView: clicked")
            }

            swipeButton.setOnClickListener {
                Log.d(LogTags.LOG_TAG, "swipeButton: clicked")
            }
        }
    }

}

interface BaseSwipeHolder {
    fun onPrimaryButtonClicked(position: Int) = Unit
    fun onItemClicked(position: Int) = Unit
    var isSwiped: Boolean
}