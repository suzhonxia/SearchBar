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
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.search.R
import com.sun.search.`interface`.SimpleAnimatorListener
import com.sun.search.`interface`.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_cur_search.*

/**
 * 在一个页面中展示动画
 */
class CurSearchActivity : AppCompatActivity() {

    private var marginTop: Int = 0
    private var searchMinWidth: Int = 0
    private var searchMaxWidth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur_search)

        initSearchLayout()
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
                        }
                    }
                })
            }

            contentLayout.run {
                val data = listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
                layoutManager = LinearLayoutManager(this@CurSearchActivity)
                adapter = object : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, data) {
                    override fun convert(helper: BaseViewHolder, item: String?) {
                        helper.setText(android.R.id.text1, "${helper.layoutPosition}")
                    }
                }
            }
        }

        etSearch.setOnClickListener { operateRaiseAnimator() }
        searchBackdrop.setOnClickListener { operateRaiseAnimator() }

        tvCancel.setOnClickListener {
            if (tvCancel.text == "取消") {
                operateLowerAnimator()
            } else if (tvCancel.text == "搜索") {
                ToastUtils.showShort("搜索")
            }
        }
        resultLayout.setOnClickListener { operateLowerAnimator() }
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
                    resultLayout.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    tvCancel.visibility = View.VISIBLE
                    resultLayout.visibility = View.VISIBLE

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
            })
            addUpdateListener {
                val percent = animatedValue as Int / 100.0F
                updateSearchLayoutMarginTop((percent * margin).toInt())
                searchBackdrop.layoutParams.width = searchMinWidth + (percent * padding).toInt()
            }
        }.start()
    }
}