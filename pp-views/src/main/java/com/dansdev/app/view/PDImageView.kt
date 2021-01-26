package com.dansdev.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import com.dansdev.app.R
import com.dansdev.app.util.PercentSizeManager
import com.dansdev.app.storage.PDSizeStorage
import com.dansdev.app.util.updateLayoutParams

open class PDImageView : AppCompatImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initSizes(attrs)
    }

    private val sizeManager get() = PDSizeStorage.instance.run { PercentSizeManager(screenParams) }

    private var percentMarginTop = 0
    private var percentMarginBottom = 0
    private var percentMarginStart = 0
    private var percentMarginEnd = 0
    private var percentHeight = 0
    private var percentWidth = 0
    private var percentPaddingStart = 0
    private var percentPaddingEnd = 0
    private var percentPaddingTop = 0
    private var percentPaddingBottom = 0

    private fun initSizes(attrs: AttributeSet?) {
        if (isInEditMode) return
        attrs?.also {
            if (isInEditMode) return
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PDImageView)

            percentMarginStart =
                sizeManager.width(ta.getFloat(R.styleable.PDImageView_pd_marginStart, 0f))
            percentMarginEnd =
                sizeManager.width(ta.getFloat(R.styleable.PDImageView_pd_marginEnd, 0f))

            percentWidth = sizeManager.width(
                ta.getFloat(R.styleable.PDImageView_pd_width, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_widthLong, 0f)
            )

            percentHeight = sizeManager.height(
                ta.getFloat(R.styleable.PDImageView_pd_height, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_heightLong, 0f)
            )

            percentMarginTop = sizeManager.height(
                ta.getFloat(R.styleable.PDImageView_pd_marginTop, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_marginTopLong, 0f)
            )

            percentMarginBottom = sizeManager.height(
                ta.getFloat(R.styleable.PDImageView_pd_marginBottom, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_marginBottomLong, 0f)
            )

            percentPaddingStart =
                sizeManager.width(ta.getFloat(R.styleable.PDImageView_pd_paddingStart, 0f))
            percentPaddingEnd =
                sizeManager.width(ta.getFloat(R.styleable.PDImageView_pd_paddingEnd, 0f))
            percentPaddingTop = sizeManager.height(
                ta.getFloat(R.styleable.PDImageView_pd_paddingTop, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_paddingTopLong, 0f)
            )
            percentPaddingBottom = sizeManager.height(
                ta.getFloat(R.styleable.PDImageView_pd_paddingBottom, 0f),
                ta.getFloat(R.styleable.PDImageView_pd_paddingBottomLong, 0f)
            )

            ta.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        updateLayoutParams<ViewGroup.MarginLayoutParams>(
            defaultBlock = {
                if (percentHeight != 0) height = percentHeight
                if (width != ViewGroup.LayoutParams.MATCH_PARENT && percentWidth != 0) width = percentWidth
                setPadding(
                    if (percentPaddingStart != 0) percentPaddingStart else paddingStart,
                    if (percentPaddingTop != 0) percentPaddingTop else paddingTop,
                    if (percentPaddingEnd != 0) percentPaddingEnd else paddingEnd,
                    if (percentPaddingBottom != 0) percentPaddingBottom else paddingBottom
                )
            },
            block = {
                setMargins(
                    if (percentMarginStart != 0) percentMarginStart else marginStart,
                    if (percentMarginTop != 0) percentMarginTop else marginTop,
                    if (percentMarginEnd != 0) percentMarginEnd else marginEnd,
                    if (percentMarginBottom != 0) percentMarginBottom else marginBottom
                )
            }
        )

        requestLayout()
    }

    fun setPDHeight(dpHeight: Float, longDpHeight: Float) {
        percentHeight = sizeManager.height(
            dpHeight,
            longDpHeight
        )
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            if (percentHeight != 0) height = percentHeight
        }
        requestLayout()
    }
}
