package com.dansdev.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dansdev.app.R
import com.dansdev.app.util.PercentSizeManager
import com.dansdev.app.storage.PDSizeStorage
import com.dansdev.app.util.updateLayoutParams

class PDSwipeRefreshLayout : SwipeRefreshLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initSizes(attrs)
    }

    private val sizeManager get() = PDSizeStorage.instance.run { PercentSizeManager(screenParams) }

    private var percentMarginTop = 0
    private var percentMarginBottom = 0
    private var percentMarginStart = 0
    private var percentMarginEnd = 0
    private var percentHeight = 0
    private var percentWidth = 0

    private fun initSizes(attrs: AttributeSet?) {
        if (isInEditMode) return
        attrs?.also {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PDSwipeRefreshLayout)

            percentWidth = sizeManager.width(
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_width, 0f),
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_widthLong, 0f)
            )

            percentHeight = sizeManager.height(
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_height, 0f),
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_heightLong, 0f)
            )

            percentMarginTop = sizeManager.height(
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginTop, 0f),
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginTopLong, 0f)
            )

            percentMarginBottom = sizeManager.height(
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginBottom, 0f),
                ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginBottomLong, 0f)
            )

            percentMarginStart = sizeManager.width(ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginStart, 0f))
            percentMarginEnd = sizeManager.width(ta.getFloat(R.styleable.PDSwipeRefreshLayout_pd_marginEnd, 0f))

            ta.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        updateLayoutParams<MarginLayoutParams>(
            defaultBlock = {
                if (percentHeight != 0) height = percentHeight
                if (width != LayoutParams.MATCH_PARENT && percentWidth != 0) width = percentWidth
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
}
