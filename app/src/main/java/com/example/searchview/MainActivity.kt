package com.example.searchview

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Alan Dreamer
 * @since 2018-12-11 Created
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Search"
        search_view.setOnClickListener {
            startActivity(Intent(this, SearchViewActivity::class.java))
        }
        search_bar.setOnClickListener {
            startActivity(Intent(this, SearchBarActivity::class.java))
        }
    }
}