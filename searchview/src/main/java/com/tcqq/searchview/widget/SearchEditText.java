/*
 * Copyright 2018 Martin Lapis, Alan Perry
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
