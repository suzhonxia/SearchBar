package com.sun.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.search.ui.CurSearchActivity
import com.sun.search.ui.SkipSearchActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * main
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCur.setOnClickListener {
            startActivity(Intent(this, CurSearchActivity::class.java))
        }
        btnSkip.setOnClickListener {
            startActivity(Intent(this, SkipSearchActivity::class.java))
        }
    }
}