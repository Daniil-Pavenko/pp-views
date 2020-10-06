package com.dansdev.pp_views.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.dansdev.pp_views.R
import com.dansdev.pp_views.storage.PDSizeStorageImpl
import com.dansdev.pp_views.util.PercentSizeManager

open class PDTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        includeFontPadding = false
        initSizes(attrs)
    }

    private val sizeManager get() = PDSizeStorageImpl(context).run { PercentSizeManager(screenParams) }

    private var percentMarginTop = 0
    private var percentMarginBottom = 0
    private var percentMarginStart = 0
    private var percentMarginEnd = 0
    private var percentHeight = 0
    private var percentWidth = 0
    private var _textSize = 0f

    fun changePDTextSize(default: Float, long: Float = 0f) {
        val textSize = sizeManager.height(default, long).toFloat()
        _textSize = textSize
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun setPDTextSize(textSize: Float) {
        _textSize = textSize
        setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initSizes(attrs: AttributeSet?) {
        if (isInEditMode) return
        attrs?.also {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PDTextView)
            _textSize = sizeManager.height(
                ta.getFloat(R.styleable.PDTextView_pd_textSize, 0f),
                ta.getFloat(R.styleable.PDTextView_pd_textSizeLong, 0f)
            ).toFloat()

            percentWidth = sizeManager.width(ta.getFloat(R.styleable.PDTextView_pd_width, 0f))

            percentHeight = sizeManager.height(
                ta.getFloat(R.styleable.PDTextView_pd_height, 0f),
                ta.getFloat(R.styleable.PDTextView_pd_heightLong, 0f)
            )

            percentMarginTop = sizeManager.height(
                ta.getFloat(R.styleable.PDTextView_pd_marginTop, 0f),
                ta.getFloat(R.styleable.PDTextView_pd_marginTopLong, 0f)
            )

            percentMarginBottom = sizeManager.height(
                ta.getFloat(R.styleable.PDTextView_pd_marginBottom, 0f),
                ta.getFloat(R.styleable.PDTextView_pd_marginBottomLong, 0f)
            )

            percentMarginStart =
                sizeManager.width(ta.getFloat(R.styleable.PDTextView_pd_marginStart, 0f))
            percentMarginEnd =
                sizeManager.width(ta.getFloat(R.styleable.PDTextView_pd_marginEnd, 0f))

            ta.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode) return
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            if (percentHeight != 0) height = percentHeight
            if (percentWidth != 0) width = percentWidth
            setMargins(
                if (percentMarginStart != 0) percentMarginStart else marginStart,
                if (percentMarginTop != 0) percentMarginTop else marginTop,
                if (percentMarginEnd != 0) percentMarginEnd else marginEnd,
                if (percentMarginBottom != 0) percentMarginBottom else marginBottom
            )
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize)

        requestLayout()
    }

    fun setPDPadding(start: Int = 0, top: Int = 0, end: Int = 0, bottom: Int = 0) {
        val correctStart = sizeManager.width(start.toFloat())
        val correctEnd = sizeManager.width(end.toFloat())
        val correctTop = sizeManager.height(top.toFloat())
        val correctBottom = sizeManager.height(bottom.toFloat())
        setPadding(correctStart, correctTop, correctEnd, correctBottom)
    }

    fun setPDMargins(start: Int = 0, top: Int = 0, end: Int = 0, bottom: Int = 0) {
        setPDMargins(start.toFloat(), top.toFloat(), end.toFloat(), bottom.toFloat())
    }

    fun setPDMargins(start: Float = 0f, top: Float = 0f, end: Float = 0f, bottom: Float = 0f) {
        sizeManager.width(start).also {
            percentMarginStart = if (it != 0) it else percentMarginStart
        }
        sizeManager.width(end).also {
            percentMarginEnd = if (it != 0) it else percentMarginEnd
        }
        sizeManager.height(top).also {
            percentMarginTop = if (it != 0) it else percentMarginTop
        }
        sizeManager.height(bottom).also {
            percentMarginBottom = if (it != 0) it else percentMarginBottom
        }
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            setMargins(percentMarginStart, percentMarginTop, percentMarginEnd, percentMarginBottom)
            requestLayout()
        }
    }

    inline fun <reified T : ViewGroup.MarginLayoutParams> setMargin(
        top: Int = 0,
        bottom: Int = 0,
        start: Int = 0,
        end: Int = 0
    ) {
        updateLayoutParams<T> {
            setMargins(start, top, end, bottom)
        }
    }

    fun updateSizePD(w: Float, h: Float) {
        percentWidth = sizeManager.width(w)
        percentHeight = sizeManager.height(h)
        updateLayoutParams {
            width = percentWidth
            height = percentHeight
        }
    }

    fun updateSize(w: Int, h: Int) {
        percentWidth = w
        percentHeight = h
        updateLayoutParams {
            width = w
            height = h
        }
    }
}
