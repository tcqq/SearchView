package com.tcqq.searchview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.appcompat.widget.AppCompatEditText;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class SearchEditText extends AppCompatEditText {

    private SearchLayout mSearchLayout;

    public SearchEditText(@NonNull Context context) {
        super(context);
    }

    public SearchEditText(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchEditText(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayout(SearchLayout layout) {
        mSearchLayout = layout;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            mSearchLayout.onBackClick(hasFocus());
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

}
