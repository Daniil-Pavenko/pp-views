package com.dansdev.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.dansdev.app.R
import com.dansdev.app.util.PercentSizeManager
import com.dansdev.app.storage.PDSizeStorage

open class PDButton : AppCompatButton {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        gravity = Gravity.CENTER
        initSizes(attrs)
    }

    private val sizeManager get() = PDSizeStorage.instance.run { PercentSizeManager(screenParams) }

    private var percentMarginTop = 0
    private var percentMarginBottom = 0
    private var percentMarginStart = 0
    private var percentMarginEnd = 0
    private var percentHeight = 0
    private var percentWidth = 0
    private var percentPaddingStart = paddingStart
    private var percentPaddingEnd = paddingEnd
    private var percentPaddingTop = paddingTop
    private var percentPaddingBottom = paddingBottom

    @SuppressLint("CustomViewStyleable")
    private fun initSizes(attrs: AttributeSet?) {
        if (isInEditMode) return
        attrs?.also {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PDPercentSizes)
            val textSize = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_textSize, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_textSizeLong, 0f)
            ).toFloat()

            percentWidth = sizeManager.width(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_width, 0f)
            )

            percentHeight = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_height, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_heightLong, 0f)
            )

            percentMarginTop = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_marginTop, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_marginTopLong, 0f)
            )

            percentWidth = sizeManager.width(ta.getFloat(R.styleable.PDPercentSizes_pd_width, 0f))

            percentHeight = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_height, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_heightLong, 0f)
            )

            percentMarginBottom = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_marginBottom, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_marginBottomLong, 0f)
            )

            percentMarginStart = sizeManager.width(ta.getFloat(R.styleable.PDPercentSizes_pd_marginStart, 0f))
            percentMarginEnd = sizeManager.width(ta.getFloat(R.styleable.PDPercentSizes_pd_marginEnd, 0f))

            percentHeight = sizeManager.height(
                    ta.getFloat(R.styleable.PDPercentSizes_pd_height, 0f),
                    ta.getFloat(R.styleable.PDPercentSizes_pd_heightLong, 0f)
            )

            percentPaddingStart = sizeManager.width(ta.getFloat(R.styleable.PDPercentSizes_pd_paddingStart, paddingStart.toFloat()))
            percentPaddingEnd = sizeManager.width(ta.getFloat(R.styleable.PDPercentSizes_pd_paddingEnd, paddingEnd.toFloat()))
            percentPaddingTop = sizeManager.height(
                ta.getFloat(R.styleable.PDPercentSizes_pd_paddingTop, paddingTop.toFloat()),
                ta.getFloat(R.styleable.PDPercentSizes_pd_paddingTopLong, paddingTop.toFloat())
            )
            percentPaddingBottom = sizeManager.height(
                ta.getFloat(R.styleable.PDPercentSizes_pd_paddingBottom, paddingBottom.toFloat()),
                ta.getFloat(R.styleable.PDPercentSizes_pd_paddingBottomLong, paddingBottom.toFloat())
            )

            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

            ta.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        updateLayoutParams<ViewGroup.MarginLayoutParams> {
            if (percentHeight != 0) height = percentHeight
            if (percentWidth != 0) width = percentWidth
            setMargins(
                if (percentMarginStart != 0) percentMarginStart else marginStart,
                if (percentMarginTop != 0) percentMarginTop else marginTop,
                if (percentMarginEnd != 0) percentMarginEnd else marginEnd,
                if (percentMarginBottom != 0) percentMarginBottom else marginBottom
            )
            setPadding(
                if (percentPaddingStart != 0) percentPaddingStart else paddingStart,
                if (percentPaddingTop != 0) percentPaddingTop else paddingTop,
                if (percentPaddingEnd != 0) percentPaddingEnd else paddingEnd,
                if (percentPaddingBottom != 0) percentPaddingBottom else paddingBottom
            )
        }

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