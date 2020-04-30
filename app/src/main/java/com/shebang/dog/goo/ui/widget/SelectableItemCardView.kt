package com.shebang.dog.goo.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import com.shebang.dog.goo.R
import com.shebang.dog.goo.databinding.SelectableItemCardViewBinding

class SelectableItemCardView(context: Context, attr: AttributeSet) :
    MaterialCardView(context, attr) {

    private val binding: SelectableItemCardViewBinding
    private val typedArray = context.theme.obtainStyledAttributes(
        attr,
        R.styleable.SelectableItemCardView,
        0, 0
    )

    init {
        val inflater = LayoutInflater.from(context)
        binding = SelectableItemCardViewBinding.inflate(inflater, this, true)

        typedArray.apply {
            try {
                setIconSrc(getDrawable(R.styleable.SelectableItemCardView_iconSrc))
                setContentsText(getString(R.styleable.SelectableItemCardView_contentsText))
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