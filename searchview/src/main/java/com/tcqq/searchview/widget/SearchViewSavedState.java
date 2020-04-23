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

package com.tcqq.searchview.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class SearchViewSavedState extends View.BaseSavedState {

    public static final Creator<SearchViewSavedState> CREATOR = new Creator<SearchViewSavedState>() {
        @NonNull
        @Override
        public SearchViewSavedState createFromParcel(@NonNull Parcel source) {
            return new SearchViewSavedState(source);
        }

        @NonNull
        @Override
        public SearchViewSavedState[] newArray(int size) {
            return new SearchViewSavedState[size];
        }
    };

    public String query;
    boolean hasFocus;
    boolean shadow;
    boolean skipLogoHamburgerToLogoArrow;
    float searchArrowState;

    private SearchViewSavedState(@NonNull Parcel source) {
        super(source);
        query = source.readString();
        hasFocus = source.readInt() == 1;
        shadow = source.readInt() == 1;
        skipLogoHamburgerToLogoArrow = source.readInt() == 1;
        searchArrowState = source.readFloat();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public SearchViewSavedState(@NonNull Parcel source, ClassLoader loader) {
        super(source, loader);
        query = source.readString();
        hasFocus = source.readInt() == 1;
        shadow = source.readInt() == 1;
        skipLogoHamburgerToLogoArrow = source.readInt() == 1;
        searchArrowState = source.readFloat();
    }

    SearchViewSavedState(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(@NonNull Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(query);
        out.writeInt(hasFocus ? 1 : 0);
        out.writeInt(shadow ? 1 : 0);
        out.writeInt(skipLogoHamburgerToLogoArrow ? 1 : 0);
        out.writeFloat(searchArrowState);
    }

}
