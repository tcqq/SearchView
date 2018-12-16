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

package com.tcqq.searchview.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.viewanimator.ViewAnimator
import com.tcqq.searchview.R
import com.tcqq.searchview.Search
import com.tcqq.searchview.graphics.SearchAnimator
import com.tcqq.searchview.graphics.SearchArrowDrawable
import kotlinx.android.synthetic.main.search_view.view.*

class SearchView : SearchLayout,
    CoordinatorLayout.AttachedBehavior {

    // ---------------------------------------------------------------------------------------------
    private val tag = "SearchView"

    private var menuItemCx = -1
    private lateinit var mImageViewImage: ImageView
    private lateinit var mMenuItemView: View

    // Shadow
    private var shadow: Boolean = false
    @ColorInt
    var shadowColor = 0
        set(value) = search_view_shadow.setBackgroundColor(value)

    // Animation duration
    var animationDuration: Long = 0

    // Listeners
    private var onOpenCloseListener: Search.OnOpenCloseListener? = null

    var skipLogoHamburgerToLogoArrow = false
    var searchArrowState = SearchArrowDrawable.STATE_HAMBURGER

    override val isView: Boolean
        get() = true

    override val layout: Int
        get() = R.layout.search_view

    // Adapter
    var adapter: RecyclerView.Adapter<*>?
        get() = search_recyclerView.adapter
        set(adapter) {
            search_recyclerView!!.adapter = adapter
        }

    // ---------------------------------------------------------------------------------------------
    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    // ---------------------------------------------------------------------------------------------
    private fun getCenterX(view: View): Int {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return location[0] + view.width / 2
    }

    // ---------------------------------------------------------------------------------------------
    override fun onTextChanged(s: CharSequence) {
        queryText = s

        setMicOrClearIcon(searchEditText.isFocused)

        onQueryTextListener?.onQueryTextChange(s)
    }

    override fun addFocus() {
        SearchAnimator.fadeOpen(search_view_shadow, animationDuration)

        if (!skipLogoHamburgerToLogoArrow) {
            setLogoHamburgerToLogoArrowWithAnimation(true)
        }
        // todo SavedState, marginy kulate a barva divideru
        onOpenCloseListener?.onOpen()

        showSuggestions()
        showKeyboard()
        setMicOrClearIcon(true)
        shadow = true
    }

    override fun removeFocus() {
        SearchAnimator.fadeClose(search_view_shadow, animationDuration)

        if (!skipLogoHamburgerToLogoArrow) {
            setLogoHamburgerToLogoArrowWithAnimation(false)
        }
        onOpenCloseListener?.onClose()

        //setTextImageVisibility(false); todo error + shadow error pri otoceni, pak mizi animace
        hideSuggestions()
        hideKeyboard()
        setMicOrClearIcon(false)
        shadow = false
    }

    override fun open() {
        searchEditText.requestFocus()
    }

    override fun close(skip: Boolean) {
        skipLogoHamburgerToLogoArrow = skip
        searchEditText.clearFocus()
    }

    // ---------------------------------------------------------------------------------------------
    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, defStyleRes)
        val layoutResId = layout

        val inflater = LayoutInflater.from(context)
        inflater.inflate(layoutResId, this, true)

        super.init(context, attrs, defStyleAttr, defStyleRes)

        mImageViewImage = findViewById(R.id.search_imageView_image)
        mImageViewImage.setOnClickListener(this)

        imageViewClear = findViewById(R.id.search_imageView_clear)
        imageViewClear!!.setOnClickListener(this)
        imageViewClear!!.visibility = View.GONE

        search_recyclerView.visibility = View.GONE
        search_recyclerView.layoutManager = LinearLayoutManager(context)
        search_recyclerView.isNestedScrollingEnabled = false
        search_recyclerView.itemAnimator = DefaultItemAnimator()
        search_recyclerView.setHasFixedSize(true)
/*        search_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })*/

        search_recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                Log.v(tag, "onInterceptTouchEvent#MotionEvent: $e")
                if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_CANCEL) {
                    if (e.eventTime - e.downTime < 250) {
                        hideKeyboard()
                    }
                }
                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })

        search_view_divider.visibility = View.GONE

        search_view_shadow.visibility = View.GONE
        search_view_shadow.setOnClickListener(this)

        logo = a.getInteger(R.styleable.SearchView_search_logo, Search.Logo.HAMBURGER_ARROW)
        shape = a.getInteger(R.styleable.SearchView_search_shape, Search.Shape.CLASSIC)
        theme = a.getInteger(R.styleable.SearchView_search_theme, Search.Theme.PLAY)
        versionMargins = a.getInteger(R.styleable.SearchView_search_version_margins, Search.VersionMargins.TOOLBAR)

        if (a.hasValue(R.styleable.SearchView_search_logo_icon)) {
            setLogoIcon(a.getInteger(R.styleable.SearchView_search_logo_icon, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_logo_color)) {
            setLogoColor(ContextCompat.getColor(context, a.getResourceId(R.styleable.SearchView_search_logo_color, 0)))
        }

        if (a.hasValue(R.styleable.SearchView_search_mic_icon)) {
            setMicIcon(a.getResourceId(R.styleable.SearchView_search_mic_icon, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_mic_color)) {
            setMicColor(a.getColor(R.styleable.SearchView_search_mic_color, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_clear_icon)) {
            setClearIcon(a.getDrawable(R.styleable.SearchView_search_clear_icon))
        } else {
            setClearIcon(ContextCompat.getDrawable(context, R.drawable.search_ic_clear_black_24dp))
        }

        if (a.hasValue(R.styleable.SearchView_search_clear_color)) {
            setClearColor(a.getColor(R.styleable.SearchView_search_clear_color, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_menu_icon)) {
            setMenuIcon(a.getResourceId(R.styleable.SearchView_search_menu_icon, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_menu_color)) {
            setMenuColor(a.getColor(R.styleable.SearchView_search_menu_color, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_background_color)) {
            setBackgroundColor(a.getColor(R.styleable.SearchView_search_background_color, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_text_image)) {
            setTextImage(a.getResourceId(R.styleable.SearchView_search_text_image, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_text_color)) {
            setTextColor(a.getColor(R.styleable.SearchView_search_text_color, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_text_size)) {
            setTextSize(a.getDimension(R.styleable.SearchView_search_text_size, 0f))
        }

        if (a.hasValue(R.styleable.SearchView_search_text_style)) {
            setTextStyle(a.getInt(R.styleable.SearchView_search_text_style, 0))
        }

        if (a.hasValue(R.styleable.SearchView_search_hint)) {
            setHint(a.getString(R.styleable.SearchView_search_hint)!!)
        }

        if (a.hasValue(R.styleable.SearchView_search_hint_color)) {
            setHintColor(a.getColor(R.styleable.SearchView_search_hint_color, 0))
        }

        animationDuration = a.getInt(
            R.styleable.SearchView_search_animation_duration,
            resources.getInteger(R.integer.search_animation_duration)
        ).toLong()
        shadow = a.getBoolean(R.styleable.SearchView_search_shadow, false)
        shadowColor = a.getColor(
            R.styleable.SearchView_search_shadow_color,
            ContextCompat.getColor(context, R.color.search_shadow)
        )

        if (a.hasValue(R.styleable.SearchView_search_elevation)) {
            elevation = a.getDimensionPixelSize(R.styleable.SearchView_search_elevation, 0).toFloat()
        }

        if (a.hasValue(R.styleable.SearchView_search_corner_radius)) {
            setRadius(a.getDimensionPixelSize(R.styleable.SearchView_search_corner_radius, 0).toFloat())
        }

        a.recycle()

        isSaveEnabled = true

        searchEditText.visibility = View.VISIBLE // todo
    }

    // ---------------------------------------------------------------------------------------------
    // Divider
    override fun setDividerColor(@ColorInt color: Int) {
        search_view_divider.setBackgroundColor(color)
    }

    // Clear
    fun setClearIcon(@DrawableRes resource: Int) {
        imageViewClear?.setImageResource(resource)
    }

    fun setClearIcon(drawable: Drawable?) {
        imageViewClear?.setImageDrawable(drawable)
    }

    override fun setClearColor(@ColorInt color: Int) {
        imageViewClear?.setColorFilter(color)
    }

    // Image
    fun setTextImage(@DrawableRes resource: Int) {
        mImageViewImage.setImageResource(resource)
        setTextImageVisibility(false)
    }

    fun setTextImage(drawable: Drawable?) {
        mImageViewImage.setImageDrawable(drawable)
        setTextImageVisibility(false)
    }

    // Others
    fun setLogoHamburgerToLogoArrowWithAnimation(animate: Boolean) {
        if (logo == Search.Logo.HAMBURGER_ARROW) {
            searchArrowDrawable.apply {
                searchArrowState = if (animate) {
                    setVerticalMirror(false)
                    animate(SearchArrowDrawable.STATE_ARROW, animationDuration)
                    SearchArrowDrawable.STATE_ARROW
                } else {
                    setVerticalMirror(true)
                    animate(SearchArrowDrawable.STATE_HAMBURGER, animationDuration)
                    SearchArrowDrawable.STATE_HAMBURGER
                }
            }
        }
    }

    fun setLogoHamburgerToLogoArrowWithoutAnimation(animation: Boolean) {
        if (logo == Search.Logo.HAMBURGER_ARROW) {
            searchArrowDrawable.apply {
                progress = if (animation) {
                    searchArrowState = SearchArrowDrawable.STATE_ARROW
                    SearchArrowDrawable.STATE_ARROW
                } else {
                    searchArrowState = SearchArrowDrawable.STATE_HAMBURGER
                    SearchArrowDrawable.STATE_HAMBURGER
                }
            }
        }
    }

    // Listeners
    fun setOnOpenCloseListener(listener: Search.OnOpenCloseListener) {
        onOpenCloseListener = listener
    }

    // ---------------------------------------------------------------------------------------------
    private fun setMicOrClearIcon(hasFocus: Boolean) {
        if (hasFocus && queryText.isNotBlank()) {
            if (onMicClickListener != null) {
                imageViewMic.visibility = View.GONE
            }
            imageViewClear?.visibility = View.VISIBLE
        } else {
            imageViewClear?.visibility = View.GONE
            if (onMicClickListener != null) {
                imageViewMic.visibility = View.VISIBLE
            }
        }
    }

    private fun setTextImageVisibility(hasFocus: Boolean) {
        if (hasFocus) {
            mImageViewImage.visibility = View.GONE
            searchEditText.visibility = View.VISIBLE
            searchEditText.requestFocus()
        } else {
            searchEditText.visibility = View.GONE
            mImageViewImage.visibility = View.VISIBLE
        }
    }

    private val cardViewPaddingTopBottom = Utils.dp2px(context, 8F) * 2
    private val searchViewHeight = resources.getDimensionPixelSize(R.dimen.search_height_view)
    private val suggestionVisibleHeight = Utils.getScreenHeight(context) -
            (Utils.getStatusBarHeight(context) + cardViewPaddingTopBottom + searchViewHeight + Utils.getNavBarHeight())
    private val suggestionsFullHeight by lazy {
        Utils.getMeasuredHeight(search_recyclerView).toFloat()
    }
    private val suggestionsHeight by lazy {
        if (Utils.getMeasuredHeight(search_recyclerView) > suggestionVisibleHeight) {
            suggestionVisibleHeight.toFloat()
        } else {
            Utils.getMeasuredHeight(search_recyclerView).toFloat()
        }
    }

    private fun showSuggestions() {
        if (adapter?.itemCount!! > 0) {
            search_view_divider.visibility = View.VISIBLE
            search_recyclerView.visibility = View.VISIBLE

            ViewAnimator
                .animate(search_recyclerView)
                .duration(300)
                .waitForHeight()
                .height(0F, suggestionsFullHeight)
                .start()
            Log.d(tag, "showSuggestions#suggestionsFullHeight: $suggestionsFullHeight")
        }
    }


    private fun hideSuggestions() {
        if (adapter?.itemCount!! > 0) {
            search_view_divider.visibility = View.GONE
//            search_recyclerView.visibility = View.GONE

            ViewAnimator
                .animate(search_recyclerView)
                .duration(250)
                .waitForHeight()
                .height(suggestionsHeight, 0F)
                .start()
            Log.d(tag, "hideSuggestions#suggestionsHeight: $suggestionsHeight")
        }
    }

    private fun getMenuItemPosition(menuItemId: Int) {
        val viewParent: ViewParent? = parent
        while (viewParent != null && viewParent is View) {
            val parent = viewParent as View?
            val view = parent!!.findViewById<View>(menuItemId)
            if (view != null) {
                mMenuItemView = view
                menuItemCx = getCenterX(mMenuItemView)
                break
            }
        }
    }

    // ---------------------------------------------------------------------------------------------
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SearchViewSavedState(superState)
        ss.hasFocus = searchEditText.hasFocus()
        ss.shadow = shadow
        ss.query = queryText.toString()
        ss.skipLogoHamburgerToLogoArrow = skipLogoHamburgerToLogoArrow
        ss.searchArrowState = searchArrowState
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SearchViewSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        state.also {
            super.onRestoreInstanceState(it.superState)
            shadow = it.shadow
            search_view_shadow.isVisible = shadow
            skipLogoHamburgerToLogoArrow = it.skipLogoHamburgerToLogoArrow
            searchArrowState = it.searchArrowState
            setLogoHamburgerToLogoArrowWithoutAnimation(searchArrowState == SearchArrowDrawable.STATE_ARROW)
            if (it.hasFocus) {
                open()
            }
            setText(it.query)
            requestLayout()
        }
    }

    // ---------------------------------------------------------------------------------------------
    override fun onClick(v: View) {
        if (v == imageViewLogo) {
            onBackClick(searchEditText.hasFocus())
        } else if (v == mImageViewImage) {
            setTextImageVisibility(true)
        } else if (v == imageViewMic) {
            onMicClickListener?.apply {
                onMicClick()
            }
        } else if (v == imageViewClear) {
            if (!searchEditText.text.isNullOrBlank()) {
                searchEditText.text!!.clear()
            }
        } else if (v == imageViewMenu) {
            onMenuClickListener?.apply {
                onMenuClick()
            }
        } else if (v == search_view_shadow) {
            onBackClick(searchEditText.hasFocus())
        }
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return SearchBehavior()
    }

}
