package br.com.bonaldi.currency.conversion.utils.customcomponents

import android.animation.Animator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.bonaldi.currency.conversion.utils.controls.OnSwipeListener
import br.com.bonaldi.currency.conversion.utils.databinding.BaseViewHolderLayoutBinding

open class BaseSwipeViewHolder<T : ViewBinding>(
    private val binding: T,
    private val parent: ViewGroup,
    private val baseViewBinding: BaseViewHolderLayoutBinding = BaseViewHolderLayoutBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    ),
    var isSwiped: Boolean = false
) : RecyclerView.ViewHolder(baseViewBinding.root), BaseSwipeHolder {

    init {
        baseViewBinding.swipeableLayout.addView(binding.root)
    }

    override fun bindItem() {
        addSwipeListener()
        addListeners()
    }

    private fun addSwipeListener() {
        baseViewBinding.apply {
            swipeableLayout.setSwipeListener { direction ->
                when (direction) {
                    OnSwipeListener.Direction.left -> {
                        animateSwipeLayout(-swipeOptions.width.toFloat(), true)
                    }
                    OnSwipeListener.Direction.right -> {
                        animateSwipeLayout(0f, false)
                    }
                }
            }
        }
    }

    private fun addListeners() {
        baseViewBinding.swipeableLayout.setOnItemClickListener {
            onItemClicked(layoutPosition)
        }

        baseViewBinding.swipeButton.setOnClickListener {
            onPrimaryButtonClicked(layoutPosition, baseViewBinding.imgFavorite)
        }
    }

    override fun closeSwipeItem(animationEndCallback: (() -> Unit)?) {
        animateSwipeLayout(0f, false, animationEndCallback)
    }

    fun setButtonDrawable(resId: Int){
        baseViewBinding.imgFavorite.setImageResource(resId)
    }

    private fun animateSwipeLayout(translationX: Float, isSwipeOpen: Boolean, animationEndCallback: (() -> Unit)? = null){
        baseViewBinding.apply{
            swipeableLayout.animate().translationX(translationX).setListener(object: Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator?) = Unit

                override fun onAnimationEnd(p0: Animator?) {
                    isSwiped = isSwipeOpen
                    animationEndCallback?.invoke()
                }

                override fun onAnimationCancel(p0: Animator?) = Unit

                override fun onAnimationRepeat(p0: Animator?) = Unit
            })
        }
    }
}

interface BaseSwipeHolder {
    fun onPrimaryButtonClicked(position: Int, favoriteImage: ImageView) = Unit
    fun onItemClicked(position: Int) = Unit
    fun closeSwipeItem(animationEndCallback: (() -> Unit)?)
    fun bindItem()
}