package com.sun.search.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.search.R
import kotlinx.android.synthetic.main.activity_skip_search.*

/**
 * 跳转页面后展示动画
 */
class SkipSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip_search)

        initSearchLayout()
    }

    private fun initSearchLayout() {
        etSearch.isFocusable = false
        etSearch.setOnClickListener { skipSearchPage() }
        searchBackdrop.setOnClickListener { skipSearchPage() }
    }

    private fun skipSearchPage() {
        startActivity(Intent(this, SearchPageActivity::class.java))
        overridePendingTransition(0, 0)
    }
}