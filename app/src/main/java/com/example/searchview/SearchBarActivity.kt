/*
 * Copyright 2018 Martin Lapis, Perry Lance
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tcqq.searchview.Search
import kotlinx.android.synthetic.main.activity_searchbar.*
import timber.log.Timber

/**
 * @author Perry Lance
 * @since 2018-12-11 Created
 */
class SearchBarActivity : AppCompatActivity() {

    companion object {
        //Result code for speech recognition
        private const val RESULT_SPEECH_INPUT = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchbar)
        search_bar.setOnMicClickListener(object : Search.OnMicClickListener {
            override fun onMicClick() {
                if (Search.isVoiceSearchAvailable(applicationContext)) {
                    Search.setVoiceSearch(
                        this@SearchBarActivity, getString(R.string.speech_prompt),
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
        search_bar.setOnQueryTextListener(object : Search.OnQueryTextListener {
            override fun onQueryTextSubmit(query: CharSequence) {
                Timber.d("onQueryTextSubmit#query: $query")
                search_bar.close()
            }

            override fun onQueryTextChange(newText: CharSequence) {
                Timber.d("onQueryTextChange#newText: $newText")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH_INPUT -> if (resultCode == RESULT_OK && data != null) {
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)!!
                val query = results[0]
                search_bar.setQuery(query, true)
            }
        }
    }
}