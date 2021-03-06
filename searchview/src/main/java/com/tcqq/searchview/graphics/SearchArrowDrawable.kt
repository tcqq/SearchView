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

package com.tcqq.searchview.graphics

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat


class SearchArrowDrawable(context: Context) : DrawerArrowDrawable(context) {

    var position: Float
        get() = progress
        set(position) {
            if (position == STATE_ARROW) {
                setVerticalMirror(true)
            } else if (position == STATE_HAMBURGER) {
                setVerticalMirror(false)
            }
            progress = position
        }

    init {
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    fun animate(state: Float, duration: Long) {
        val anim: ObjectAnimator = if (state == STATE_ARROW) {
            ObjectAnimator.ofFloat(this, PROGRESS, STATE_HAMBURGER, state)
        } else {
            ObjectAnimator.ofFloat(this, PROGRESS, STATE_ARROW, state)
        }
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.duration = duration
        anim.start()
    }

    companion object {

        const val STATE_HAMBURGER = 0.0f
        const val STATE_ARROW = 1.0f

        private val PROGRESS = object : Property<SearchArrowDrawable, Float>(Float::class.java, "progress") {
            override fun set(`object`: SearchArrowDrawable, value: Float?) {
                `object`.progress = value!!
            }

            override fun get(`object`: SearchArrowDrawable): Float {
                return `object`.progress
            }
        }
    }

}
