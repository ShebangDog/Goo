package com.shebang.dog.goo.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.ItemWithImageCardViewBinding

class ItemWithImageCardView(context: Context, attr: AttributeSet) :
    MaterialCardView(context, attr) {

    private val binding: ItemWithImageCardViewBinding
    private val typedArray = context.theme.obtainStyledAttributes(
        attr,
        R.styleable.ItemWithImageCardView,
        0, 0
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = ItemWithImageCardViewBinding.inflate(inflater, this, true)

        typedArray.apply {
            try {
                setIconSrc(getDrawable(R.styleable.ItemWithImageCardView_iconSrc))
                setContentsText(getString(R.styleable.ItemWithImageCardView_contentsText))
            } finally {
                recycle()
            }
        }

    }

    private fun <T> setter(value: T, function: (T) -> Unit) {
        function(value)
        invalidate()
        requestLayout()
    }

    fun setIconSrc(drawable: Drawable?) {
        setter(drawable) { binding.iconImageButton.setImageDrawable(it) }
    }

    fun setContentsText(string: String?) {
        setter(string) { binding.contentsTextView.text = it }
    }
}