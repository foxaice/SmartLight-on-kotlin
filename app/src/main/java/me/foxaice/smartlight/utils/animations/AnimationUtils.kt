package me.foxaice.smartlight.utils.animations

import android.animation.Animator
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.animation.Animation
import android.view.animation.Interpolator

class AnimationUtils private constructor() {
    companion object {
        fun setInterpolator(interpolator: Interpolator, vararg animations: Animation) {
            for (item in animations) item.interpolator = interpolator
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        fun setInterpolator(interpolator: Interpolator, vararg animators: Animator) {
            for (item in animators) item.interpolator = interpolator
        }

        fun setDuration(duration: Long, vararg animations: Animation) {
            for (item in animations) item.duration = duration
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        fun setDuration(duration: Long, vararg animators: Animator) {
            for (item in animators) item.duration = duration
        }
    }

    open class AnimationListenerAdapter : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {}

        override fun onAnimationStart(animation: Animation) {}
    }
}