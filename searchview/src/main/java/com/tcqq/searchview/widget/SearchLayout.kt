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

package com.tcqq.searchview.widget

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.tcqq.searchview.R
import com.tcqq.searchview.Search
import com.tcqq.searchview.graphics.SearchArrowDrawable


abstract class SearchLayout : FrameLayout, View.OnClickListener {

    internal var queryText: CharSequence = ""
    internal lateinit var cardView: MaterialCardView
    internal lateinit var imageViewLogo: ImageView
    internal lateinit var imageViewMic: ImageView
    internal var imageViewClear: ImageView? = null
    internal lateinit var imageViewMenu: ImageView
    internal lateinit var searchEditText: SearchEditText
    internal lateinit var searchArrowDrawable: SearchArrowDrawable
    internal var onMicClickListener: Search.OnMicClickListener? = null
    internal var onMenuClickListener: Search.OnMenuClickListener? = null
    internal var onQueryTextListener: Search.OnQueryTextListener? = null
    internal var onBackClickListener: Search.OnBackClickListener? = null
    // ---------------------------------------------------------------------------------------------
    var logo: Int = 0
        set(logo) {
            field = logo

            when (this.logo) {
                Search.Logo.GOOGLE -> imageViewLogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.search_ic_g_color_24dp
                    )
                )
                Search.Logo.HAMBURGER_ARROW -> {
                    searchArrowDrawable = SearchArrowDrawable(context)
                    imageViewLogo.setImageDrawable(searchArrowDrawable)
                }
                Search.Logo.ARROW -> imageViewLogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.search_ic_arrow_back_black_24dp
                    )
                )
            }
        }
    var shape: Int = 0
        set(shape) {
            field = shape

            when (this.shape) {
                Search.Shape.CLASSIC -> cardView.radius =
                        resources.getDimensionPixelSize(R.dimen.search_shape_classic).toFloat()
                Search.Shape.ROUNDED -> cardView.radius =
                        resources.getDimensionPixelSize(R.dimen.search_shape_rounded).toFloat()
                Search.Shape.OVAL -> if (!isView) {
                    cardView.radius = resources.getDimensionPixelSize(R.dimen.search_shape_oval).toFloat()
                }
            }
        }
    var theme: Int = 0
        set(theme) {
            field = theme

            when (this.theme) {
                Search.Theme.PLAY -> {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.search_play_background))
                    setDividerColor(ContextCompat.getColor(context, R.color.search_play_divider))
                    setLogoColor(ContextCompat.getColor(context, R.color.search_play_icon))
                    setMicColor(ContextCompat.getColor(context, R.color.search_play_icon))
                    setClearColor(ContextCompat.getColor(context, R.color.search_play_icon))
                    setMenuColor(ContextCompat.getColor(context, R.color.search_play_icon))
                    setHintColor(ContextCompat.getColor(context, R.color.search_play_hint))
                    setTextColor(ContextCompat.getColor(context, R.color.search_play_title))
                }
                Search.Theme.GOOGLE -> {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.search_google_background))
                    setDividerColor(ContextCompat.getColor(context, R.color.search_google_divider))
                    clearIconsColor()
                    setClearColor(ContextCompat.getColor(context, R.color.search_google_icon))
                    setMenuColor(ContextCompat.getColor(context, R.color.search_google_menu))
                    setHintColor(ContextCompat.getColor(context, R.color.search_google_hint))
                    setTextColor(ContextCompat.getColor(context, R.color.search_google_title))
                }
                Search.Theme.LIGHT -> {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.search_light_background))
                    setDividerColor(ContextCompat.getColor(context, R.color.search_light_divider))
                    setLogoColor(ContextCompat.getColor(context, R.color.search_light_icon))
                    setMicColor(ContextCompat.getColor(context, R.color.search_light_icon))
                    setClearColor(ContextCompat.getColor(context, R.color.search_light_icon))
                    setMenuColor(ContextCompat.getColor(context, R.color.search_light_icon))
                    setHintColor(ContextCompat.getColor(context, R.color.search_light_hint))
                    setTextColor(ContextCompat.getColor(context, R.color.search_light_title))
                }
                Search.Theme.DARK -> {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.search_dark_background))
                    setDividerColor(ContextCompat.getColor(context, R.color.search_dark_divider))
                    setLogoColor(ContextCompat.getColor(context, R.color.search_dark_icon))
                    setMicColor(ContextCompat.getColor(context, R.color.search_dark_icon))
                    setClearColor(ContextCompat.getColor(context, R.color.search_dark_icon))
                    setMenuColor(ContextCompat.getColor(context, R.color.search_dark_icon))
                    setHintColor(ContextCompat.getColor(context, R.color.search_dark_hint))
                    setTextColor(ContextCompat.getColor(context, R.color.search_dark_title))
                }
            }
        }
    private var mTextStyle = Typeface.NORMAL
    private var mTextFont = Typeface.DEFAULT
    private var mLinearLayout: LinearLayout? = null
    var versionMargins: Int = 0
        set(versionMargins) {
            field = versionMargins

            val params =
                FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            when (this.versionMargins) {
                Search.VersionMargins.BAR -> {
                    left = resources.getDimensionPixelSize(R.dimen.search_bar_margin_left_right)
                    top = resources.getDimensionPixelSize(R.dimen.search_bar_margin_top)
                    right = resources.getDimensionPixelSize(R.dimen.search_bar_margin_left_right)
                    bottom = resources.getDimensionPixelSize(R.dimen.search_bar_margin_bottom)

                    params.setMargins(left, top, right, bottom)

                    cardView.layoutParams = params
                }
                Search.VersionMargins.TOOLBAR -> {
                    left = resources.getDimensionPixelSize(R.dimen.search_toolbar_margin_left_right)
                    top = resources.getDimensionPixelSize(R.dimen.search_toolbar_margin_top_bottom)
                    right = resources.getDimensionPixelSize(R.dimen.search_toolbar_margin_left_right)
                    bottom = resources.getDimensionPixelSize(R.dimen.search_toolbar_margin_top_bottom)

                    params.setMargins(left, top, right, bottom)

                    cardView.layoutParams = params
                }
                Search.VersionMargins.MENU_ITEM -> {
                    left = resources.getDimensionPixelSize(R.dimen.search_menu_item_margin)
                    top = resources.getDimensionPixelSize(R.dimen.search_menu_item_margin)
                    right = resources.getDimensionPixelSize(R.dimen.search_menu_item_margin)
                    bottom = resources.getDimensionPixelSize(R.dimen.search_menu_item_margin)

                    params.setMargins(left, top, right, bottom)

                    cardView.layoutParams = params
                }
            }
        }

    protected abstract val isView: Boolean

    protected abstract val layout: Int

    val text: Editable?
        get() = searchEditText.text

    // Query
    val query: Editable?
        get() = searchEditText.text

    // Height
    var customHeight: Int
        get() {
            val params = mLinearLayout!!.layoutParams
            return params.height
        }
        set(height) {
            val params = mLinearLayout!!.layoutParams
            params.height = height
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            mLinearLayout!!.layoutParams = params
        }

    //@FloatRange(from = 0.5, to = 1.0)
    // Others
    val isOpen: Boolean
        get() = visibility == View.VISIBLE

    // ---------------------------------------------------------------------------------------------
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    // ---------------------------------------------------------------------------------------------
    protected abstract fun onTextChanged(s: CharSequence)

    protected abstract fun addFocus()

    protected abstract fun removeFocus()

    protected abstract fun open()

    abstract fun close(skip: Boolean = false)

    // ---------------------------------------------------------------------------------------------
    @CallSuper
    internal open fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        cardView = findViewById(R.id.search_cardView)

        mLinearLayout = findViewById(R.id.search_linearLayout)

        imageViewLogo = findViewById(R.id.search_imageView_logo)
        imageViewLogo.setOnClickListener(this)

        imageViewMic = findViewById(R.id.search_imageView_mic)
        imageViewMic.visibility = View.GONE
        imageViewMic.setOnClickListener(this)

        imageViewMenu = findViewById(R.id.search_imageView_menu)
        imageViewMenu.visibility = View.GONE
        imageViewMenu.setOnClickListener(this)

        searchEditText = findViewById(R.id.search_searchEditText)
        searchEditText.setLayout(this)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                this@SearchLayout.onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        searchEditText.setOnEditorActionListener { _, _, _ ->
            onSubmitQuery()
            true
        }
        searchEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                addFocus()
            } else {
                removeFocus()
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Logo
    fun setLogoIcon(@DrawableRes resource: Int) {
        imageViewLogo.setImageResource(resource)
    }

    fun setLogoIcon(drawable: Drawable?) {
        if (drawable != null) {
            imageViewLogo.setImageDrawable(drawable)
        } else {
            imageViewLogo.visibility = View.GONE
        }
    }

    fun setLogoColor(@ColorInt color: Int) {
        imageViewLogo.setColorFilter(color)
    }

    // Mic
    fun setMicIcon(@DrawableRes resource: Int) {
        imageViewMic.setImageResource(resource)
    }

    fun setMicIcon(drawable: Drawable?) {
        imageViewMic.setImageDrawable(drawable)
    }

    fun setMicColor(@ColorInt color: Int) {
        imageViewMic.setColorFilter(color)
    }

    // Menu
    internal fun setMenuIcon(@DrawableRes resource: Int) {
        imageViewMenu.setImageResource(resource)
    }

    fun setMenuIcon(drawable: Drawable?) {
        imageViewMenu.setImageDrawable(drawable)
    }

    internal fun setMenuColor(@ColorInt color: Int) {
        imageViewMenu.setColorFilter(color)
    }

    // Text
    fun setTextImeOptions(imeOptions: Int) {
        searchEditText.imeOptions = imeOptions
    }

    fun setTextInputType(inputType: Int) {
        searchEditText.inputType = inputType
    }

    fun setText(text: CharSequence) {
        searchEditText.setText(text)
    }

    fun setText(@StringRes text: Int) {
        searchEditText.setText(text)
    }

    fun setTextColor(@ColorInt color: Int) {
        searchEditText.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        searchEditText.textSize = size
    }

    /**
     * Typeface.NORMAL
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     */
    fun setTextStyle(style: Int) {
        mTextStyle = style
        searchEditText.typeface = Typeface.create(mTextFont, mTextStyle)
    }

    /**
     * Typeface.DEFAULT
     * Typeface.DEFAULT_BOLD
     * Typeface.SANS_SERIF
     * Typeface.SERIF
     * Typeface.MONOSPACE
     */
    fun setTextFont(font: Typeface) {
        mTextFont = font
        searchEditText.typeface = Typeface.create(mTextFont, mTextStyle)
    }

    /*
     * Use Gravity or GravityCompat
     */
    fun setTextGravity(gravity: Int) {
        searchEditText.gravity = gravity
    }

    // Hint
    fun setHint(hint: String) {
        searchEditText.hint = hint
    }

    fun setHint(@StringRes hint: Int) {
        searchEditText.setHint(hint)
    }

    fun setHintColor(@ColorInt color: Int) {
        searchEditText.setHintTextColor(color)
    }

    fun setQuery(query: CharSequence, submit: Boolean = false) {
        if (query.isNotBlank()) {
            queryText = query
            searchEditText.setText(query)
            searchEditText.setSelection(searchEditText.length())
            if (submit) {
                onSubmitQuery()
            }
        }
    }

    fun setQuery(@StringRes query: Int, submit: Boolean = false) {
        if (query != 0) {
            queryText = context.getString(query)
            searchEditText.setText(context.getString(query))
            searchEditText.setSelection(searchEditText.length())
            if (submit) {
                onSubmitQuery()
            }
        }
    }

    fun setRadius(radius: Float) {
        cardView.radius = radius
    }

    // Overrides
    override fun setElevation(elevation: Float) {
        cardView.maxCardElevation = elevation
        cardView.cardElevation = elevation
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        cardView.setCardBackgroundColor(color)
    }

    // Listeners
    fun setOnMicClickListener(listener: Search.OnMicClickListener) {
        onMicClickListener = listener
        if (onMicClickListener != null) {
            imageViewMic.visibility = View.VISIBLE
            if (theme == Search.Theme.GOOGLE) {
                imageViewMic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.search_ic_mic_color_24dp))
            } else {
                imageViewMic.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.search_ic_mic_black_24dp))
            }
        } else {
            imageViewMic.visibility = View.GONE
        }
    }

    fun setOnMenuClickListener(listener: Search.OnMenuClickListener) {
        onMenuClickListener = listener
        if (onMenuClickListener != null) {
            imageViewMenu.visibility = View.VISIBLE
            imageViewMenu.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.search_ic_menu_black_24dp))
        } else {
            imageViewMenu.visibility = View.GONE
        }
    }

    fun setOnQueryTextListener(listener: Search.OnQueryTextListener) {
        onQueryTextListener = listener
    }

    fun setOnBackClickListener(listener: Search.OnBackClickListener) {
        onBackClickListener = listener
    }

    // ---------------------------------------------------------------------------------------------
    open fun setDividerColor(@ColorInt color: Int) {

    }

    open fun setClearColor(@ColorInt color: Int) {

    }

    fun showKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchEditText, 0)
        }
    }

    fun hideKeyboard() {
        if (!isInEditMode) {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun onBackClick(hasFocus: Boolean) {
        if (onBackClickListener != null) {
            onBackClickListener!!.onBackClick(hasFocus)
        } else {
            close()
        }
    }

    // ---------------------------------------------------------------------------------------------
    private fun clearIconsColor() {
        imageViewLogo.clearColorFilter()
        imageViewMic.clearColorFilter()
        if (imageViewClear != null) {
            imageViewClear!!.clearColorFilter()
        }
    }

    private fun onSubmitQuery() {
        onQueryTextListener?.onQueryTextSubmit(queryText)
    }

}
