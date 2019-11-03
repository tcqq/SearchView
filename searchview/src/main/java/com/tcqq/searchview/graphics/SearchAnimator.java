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

package com.tcqq.searchview.graphics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.tcqq.searchview.R;
import com.tcqq.searchview.Search;
import com.tcqq.searchview.widget.SearchEditText;
import com.tcqq.searchview.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;


public class SearchAnimator {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void revealOpen(@NonNull Context context, @NonNull final CardView cardView, int cx, long duration, @NonNull final SearchEditText editText, @Nullable final Search.OnOpenCloseListener listener) {
        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (isRtlLayout(context)) {
                cx = padding;
            } else {
                cx = cardView.getWidth() - padding;
            }
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height_view) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getSize(displaySize);
            }
            float finalRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, 0.0f, finalRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    editText.requestFocus();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    cardView.setVisibility(View.VISIBLE);
                    if (listener != null) {
                        listener.onSuggestOpen();
                    }
                }
            });
            anim.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void revealClose(@NonNull Context context, @NonNull final CardView cardView, int cx, long duration, @NonNull final SearchEditText editText, @NonNull final SearchView searchView, @Nullable final Search.OnOpenCloseListener listener) {
        if (cx <= 0) {
            int padding = context.getResources().getDimensionPixelSize(R.dimen.search_reveal);
            if (isRtlLayout(context)) {
                cx = padding;
            } else {
                cx = cardView.getWidth() - padding;
            }
        }

        int cy = context.getResources().getDimensionPixelSize(R.dimen.search_height_view) / 2;

        if (cx != 0 && cy != 0) {
            Point displaySize = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getSize(displaySize);
            }
            float initialRadius = (float) Math.hypot(Math.max(cx, displaySize.x - cx), cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(cardView, cx, cy, initialRadius, 0.0f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(duration);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cardView.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    if (listener != null) {
                        listener.onSuggestClose();
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    editText.clearFocus();
                }
            });
            anim.start();
        }
    }

    public static void fadeOpen(@NonNull final View view, long duration) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public static void fadeClose(@NonNull final View view, long duration) {
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    private static boolean isRtlLayout(@NonNull Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

}
