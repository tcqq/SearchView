package com.tcqq.searchview.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import com.tcqq.searchview.R
import com.tcqq.searchview.Search


class SearchBar : SearchLayout {

    private var mOnBarClickListener: Search.OnBarClickListener? = null

    override val isView: Boolean
        get() = false

    override val layout: Int
        get() = R.layout.search_bar

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
    override fun onTextChanged(s: CharSequence) {
        onQueryTextListener?.apply {
            onQueryTextChange(s)
        }
    }

    override fun addFocus() {
        if (onMicClickListener == null) {
            imageViewMic.visibility = View.VISIBLE
        }
        showKeyboard()
    }

    override fun removeFocus() {
        if (onMicClickListener == null) {
            imageViewMic.visibility = View.GONE
        }
        hideKeyboard()
    }

    override fun open() {
        searchEditText.requestFocus()
    }

    override fun close(skip: Boolean) {
        searchEditText.clearFocus()
    }

    // ---------------------------------------------------------------------------------------------
    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchBar, defStyleAttr, defStyleRes)
        val layoutResId = layout

        val inflater = LayoutInflater.from(context)
        inflater.inflate(layoutResId, this, true)

        super.init(context, attrs, defStyleAttr, defStyleRes)

        logo = a.getInt(R.styleable.SearchBar_search_logo, Search.Logo.GOOGLE)
        shape = a.getInt(R.styleable.SearchBar_search_shape, Search.Shape.OVAL)
        theme = a.getInt(R.styleable.SearchBar_search_theme, Search.Theme.GOOGLE)
        versionMargins = a.getInt(R.styleable.SearchBar_search_version_margins, Search.VersionMargins.BAR)

        if (a.hasValue(R.styleable.SearchBar_search_elevation)) {
            elevation = a.getDimensionPixelSize(R.styleable.SearchBar_search_elevation, 0).toFloat()
        }

        a.recycle()

        setOnClickListener(this)
    }

    // ---------------------------------------------------------------------------------------------
    // Listeners
    fun setOnBarClickListener(listener: Search.OnBarClickListener) {
        mOnBarClickListener = listener
    }

    // ---------------------------------------------------------------------------------------------
    override fun onClick(v: View) {
        if (v == imageViewLogo) {
            close()
        } else if (v == imageViewMic) {
            onMicClickListener?.apply {
                onMicClick()
            }
        } else if (v == imageViewMenu) {
            onMenuClickListener?.apply {
                onMenuClick()
            }
        } else if (v == this) {
            if (mOnBarClickListener != null) {
                mOnBarClickListener!!.onBarClick()
            } else {
                open()
            }
        }
    }

}
