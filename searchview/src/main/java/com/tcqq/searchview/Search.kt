package com.tcqq.searchview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent


object Search {

    fun setVoiceSearch(activity: Activity, text: String, resultCode: Int) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)

        activity.startActivityForResult(intent, resultCode)
    }

    fun isVoiceSearchAvailable(context: Context): Boolean {
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
        return activities.size != 0
    }

    object Logo {
        const val GOOGLE: Int = 1000
        const val HAMBURGER_ARROW: Int = 1001
        const val ARROW: Int = 1002
    }

    object Shape {
        const val CLASSIC: Int = 2000
        const val ROUNDED: Int = 2001
        const val OVAL: Int = 2002
    }

    object Theme {
        const val PLAY: Int = 3000
        const val GOOGLE: Int = 3001
        const val LIGHT: Int = 3002
        const val DARK: Int = 3003
    }

    object VersionMargins {
        const val BAR: Int = 5000
        const val TOOLBAR: Int = 5001
        const val MENU_ITEM: Int = 5002
    }

    // SearchLayout
    interface OnMicClickListener {
        fun onMicClick()
    }

    interface OnMenuClickListener {
        fun onMenuClick()
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: CharSequence)

        fun onQueryTextChange(newText: CharSequence)
    }

    interface OnBackClickListener {
        fun onBackClick(hasFocus: Boolean)
    }

    // SearchBar
    interface OnBarClickListener {
        fun onBarClick()
    }

    // SearchView
    interface OnOpenCloseListener {
        fun onOpen()

        fun onClose()
    }

    interface OnSuggestClickListener {
        fun onSuggestClick(position: Int)
    }


}
