package com.sun.search.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.blankj.utilcode.util.KeyboardUtils

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    fun loseFocus() {
        post {
            setText("")
            isFocusable = false
            KeyboardUtils.hideSoftInput(this)
        }
    }

    fun obtainFocus() {
        post {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            KeyboardUtils.showSoftInput(this)
        }
    }
}