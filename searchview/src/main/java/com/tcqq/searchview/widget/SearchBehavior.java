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
