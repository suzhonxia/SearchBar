package com.sun.search.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sun.search.R
import kotlinx.android.synthetic.main.activity_cur_search_motion.*

/**
 * 在一个页面中展示动画（MotionLayout实现）
 */
class CurSearchMotionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur_search_motion)

        initSearchLayout()
    }

    private fun initSearchLayout() {
        searchBackdrop.setOnClickListener { toEnd() }
        etSearch.setOnClickListener { toEnd() }
        tvCancel.setOnClickListener { toStart() }
        resultLayout.setOnClickListener { toStart() }
        ivClear.setOnClickListener { etSearch.setText("") }

        etSearch.doAfterTextChanged {
            val size = it?.length ?: 0
            if (size > 0) {
                tvCancel.text = "搜索"
                ivClear.alpha = 1.0F
                ivClear.setOnClickListener { etSearch.setText("") }
            } else {
                tvCancel.text = "取消"
                ivClear.alpha = 0.0F
                ivClear.setOnClickListener {}
            }
        }
        contentLayout.run {
            val data = listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
            layoutManager = LinearLayoutManager(this@CurSearchMotionActivity)
            adapter = object : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, data) {
                override fun convert(helper: BaseViewHolder, item: String?) {
                    helper.setText(android.R.id.text1, "${helper.layoutPosition}")
                }
            }
        }
        rootLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, percent: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if (rootLayout.currentState == rootLayout.startState) {
                    etSearch.loseFocus()
                } else if (rootLayout.currentState == rootLayout.endState) {
                    etSearch.obtainFocus()
                }
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }
        })
    }

    private fun toEnd() {
        if (rootLayout.currentState == -1) {
            return
        }

        if (rootLayout.currentState == rootLayout.startState) {
            rootLayout.transitionToEnd()
        }
    }

    private fun toStart() {
        if (rootLayout.currentState == -1) {
            return
        }

        if (rootLayout.currentState == rootLayout.endState) {
            rootLayout.transitionToStart()
        }
    }
}