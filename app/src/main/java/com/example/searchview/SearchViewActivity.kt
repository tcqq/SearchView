/*
 * Copyright 2018 Martin Lapis, Alan Dreamer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.searchview

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tcqq.searchview.Search
import com.tcqq.searchview.graphics.SearchArrowDrawable
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.activity_searchview.*
import timber.log.Timber
import java.util.*

class SearchViewActivity : AppCompatActivity(),
    FlexibleAdapter.OnItemClickListener {

    private lateinit var suggestionsAdapter: FlexibleAdapter<IFlexible<*>>

    companion object {
        //Result code for speech recognition
        private const val RESULT_SPEECH_INPUT = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchview)

        suggestionsAdapter = FlexibleAdapter(null, this, true)
        search_view.adapter = suggestionsAdapter
        suggestionsAdapter.updateDataSet(getSuggestionsItems())

        search_view.setOnBackClickListener(object : Search.OnBackClickListener {
            override fun onBackClick(hasFocus: Boolean) {
                Timber.d("onBackClick#hasFocus: $hasFocus")
                if (hasFocus) {
                    search_view.close()
                } else {
                    if (search_view.query.isNullOrEmpty()) {
                        Toast.makeText(this@SearchViewActivity, "Menu", Toast.LENGTH_SHORT).show()
                    } else {
                        onBackPressed()
                    }
                }
            }
        })
        search_view.setOnMicClickListener(object : Search.OnMicClickListener {
            override fun onMicClick() {
                if (Search.isVoiceSearchAvailable(applicationContext)) {
                    Search.setVoiceSearch(
                        this@SearchViewActivity,
                        getString(R.string.speech_prompt),
                        RESULT_SPEECH_INPUT
                    )
                } else {
                    Toast.makeText(
                        applicationContext, getString(R.string.speech_not_supported),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        search_view.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence) {
                Timber.d("onQueryTextSubmit#query: $query")
                search_view.close(true)
            }

            override fun onQueryTextChange(newText: CharSequence) {
                Timber.d("onQueryTextChange#newText: $newText")
            }
        })
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        val item = suggestionsAdapter.getItem(position, SuggestionsItem::class.java)!!
        search_view.setQuery(item.query, true)
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH_INPUT -> if (resultCode == RESULT_OK && data != null) {
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val query = results[0]
                search_view.setQuery(query, true)
                if (search_view.searchArrowState == SearchArrowDrawable.STATE_HAMBURGER) {
                    search_view.setLogoHamburgerToLogoArrowWithAnimation(true)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!search_view.query.isNullOrEmpty()) {
            search_view.skipLogoHamburgerToLogoArrow = false
            search_view.setLogoHamburgerToLogoArrowWithAnimation(false)
            search_view.setText("")
            return
        }
        super.onBackPressed()
    }

    private fun getSuggestionsItems(): List<IFlexible<*>> {
        var index = -1
        val items = ArrayList<IFlexible<*>>()
        while (index < 5) {
            items.add(SuggestionsItem("${++index}", "Title$index"))
        }
        return items
    }
}
