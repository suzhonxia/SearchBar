package com.sun.search.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.sun.search.R
import com.sun.search.`interface`.SimpleAnimatorListener
import com.sun.search.`interface`.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_search_page.*

class SearchPageActivity : AppCompatActivity() {
    private var marginTop: Int = 0
    private var searchMinWidth: Int = 0
    private var searchMaxWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        initSearchLayout()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    private fun initSearchLayout() {
        SizeUtils.forceGetViewSize(tvTitle) {
            marginTop = it.height
            searchMaxWidth = ScreenUtils.getScreenWidth() - searchBackdrop.marginLeft - searchBackdrop.marginRight
            searchMinWidth = searchMaxWidth - tvCancel.width - tvCancel.marginEnd

            searchLayout.setOnClickListener { }
            updateSearchLayoutMarginTop(marginTop)
            searchBackdrop.layoutParams.width = searchMaxWidth
            searchBackdrop.requestLayout()

            etSearch.run {
                isFocusable = false
                addTextChangedListener(object : SimpleTextWatcher() {
                    override fun afterTextChanged(s: Editable?) {
                        val key = etSearch.text.trim().toString()
                        if (key.isNotEmpty()) {
                            tvCancel.text = "搜索"
                            ivClear.visibility = View.VISIBLE
                            ivClear.setOnClickListener { etSearch.setText("") }
                        } else {
                            tvCancel.text = "取消"
                            ivClear.visibility = View.GONE
                            ivClear.setOnClickListener {}
                            resultLayout.visibility = View.GONE
                        }
                    }
                })
            }

            operateRaiseAnimator()
        }

        tvCancel.setOnClickListener {
            if (tvCancel.text == "取消") {
                operateLowerAnimator()
            } else if (tvCancel.text == "搜索") {
                resultLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun updateSearchLayoutMarginTop(value: Int) {
        ConstraintSet().run {
            clone(rootLayout)
            setMargin(R.id.searchLayout, 3, value)
            applyTo(rootLayout)
        }
    }

    // 执行升起动画
    private fun operateRaiseAnimator() {
        if (tvCancel.visibility == View.VISIBLE) {
            return
        }

        val margin = marginTop// 从[marginTop] -> 到[0]
        val padding = tvCancel.width + tvCancel.marginEnd// 从[0] -> 到[padding]
        ValueAnimator.ofInt(0, 100).apply {
            duration = 150
            interpolator = LinearInterpolator()
            addListener(object : SimpleAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    tvCancel.visibility = View.VISIBLE

                    etSearch.let {
                        it.isFocusable = true
                        it.isFocusableInTouchMode = true
                        it.requestFocus()
                        KeyboardUtils.showSoftInput(it)
                    }
                }
            })
            addUpdateListener {
                val percent = animatedValue as Int / 100.0F
                updateSearchLayoutMarginTop(((1 - percent) * margin).toInt())
                searchBackdrop.layoutParams.width = searchMaxWidth - (percent * padding).toInt()
                searchBackdrop.requestLayout()
            }
        }.start()
    }

    // 执行降下动画
    private fun operateLowerAnimator() {
        if (tvCancel.visibility != View.VISIBLE) {
            return
        }

        val margin = marginTop// 从[0] -> 到[marginTop]
        val padding = tvCancel.width + tvCancel.marginEnd// 从[padding] -> 到[0]
        ValueAnimator.ofInt(0, 100).apply {
            duration = 150
            interpolator = LinearInterpolator()
            addListener(object : SimpleAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                    tvCancel.visibility = View.INVISIBLE
                    resultLayout.visibility = View.GONE

                    etSearch.let {
                        it.setText("")
                        it.isFocusable = false
                        KeyboardUtils.hideSoftInput(it)
                    }
                }

                override fun onAnimationEnd(animation: Animator?) {
                    finish()
                }
            })
            addUpdateListener {
                val percent = animatedValue as Int / 100.0F
                updateSearchLayoutMarginTop((percent * margin).toInt())
                searchBackdrop.layoutParams.width = searchMinWidth + (percent * padding).toInt()
            }
        }.start()
    }
}
