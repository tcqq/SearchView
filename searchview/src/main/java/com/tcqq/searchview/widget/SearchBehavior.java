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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.tcqq.searchview.widget.SearchView;


public class SearchBehavior extends CoordinatorLayout.Behavior<SearchView> {

    public SearchBehavior() {
    }

    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            ViewCompat.setElevation(child, ViewCompat.getElevation(dependency));
            ViewCompat.setZ(child, ViewCompat.getZ(dependency) + 1); // todo no click background
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, @NonNull SearchView child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            child.setTranslationY(dependency.getY());
            return true;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

}
