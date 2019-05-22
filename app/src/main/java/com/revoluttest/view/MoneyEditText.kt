package com.revoluttest.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import java.util.Locale

class MoneyEditText : AppCompatEditText {

    var amountChangedListener: ((CharSequence) -> Unit)? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr)

    init {
        keyListener = MoneyValueFilter()
    }

    fun setOnSelectedListener(onSelected: () -> Unit) {
        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onSelected.invoke()
            }
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
        amountChangedListener?.invoke(text)
    }

    inner class MoneyValueFilter : DigitsKeyListener(Locale.getDefault(), false, true) {

        private val maxBeforeDecimal = 9
        private val maxAfterDecimal = 2

        override fun filter(source: CharSequence, start: Int, end: Int,
                            dest: Spanned, dstart: Int, dend: Int): CharSequence {

            val out = super.filter(source, start, end, dest, dstart, dend)

            var mutableSource = source
            var mutableStart = start
            var mutableEnd = end


            if (out != null) {
                mutableSource = out
                mutableStart = 0
                mutableEnd = out.length
            }

            val len = mutableEnd - mutableStart

            if (len == 0) return mutableSource

            val temp = StringBuilder(dest).insert(dstart, mutableSource).toString()

            when {
                temp == "." -> return "0."
                temp == "0" -> return ""
                isLongerThanMaxBeforeDecimal(temp) -> return ""
                isLongerThanMaxAfterDecimal(temp) -> return ""
            }

            return SpannableStringBuilder(mutableSource, mutableStart, mutableEnd)
        }

        private fun isLongerThanMaxAfterDecimal(temp: String) =
            temp.contains(".") && temp.substring(temp.indexOf(".") + 1).length > maxAfterDecimal

        private fun isLongerThanMaxBeforeDecimal(temp: String): Boolean =
            if (temp.contains(".")) {
                temp.substring(0, temp.indexOf(".")).length > maxBeforeDecimal
            } else {
                temp.length > maxBeforeDecimal
            }
    }
}