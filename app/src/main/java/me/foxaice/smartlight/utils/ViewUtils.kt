package me.foxaice.smartlight.utils

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.DrawableRes
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView

//todo delete this lint
@SuppressLint("ObsoleteSdkInt")
class ViewUtils private constructor() {
    companion object {
        fun setEnabled(isEnable: Boolean, vararg views: View) {
            for (item in views) item.isEnabled = isEnable
        }

        fun bringToFront(vararg views: View) {
            for (item in views) item.bringToFront()
        }

        fun setImageDrawable(view: View, @DrawableRes id: Int) = (view as? ImageView)?.setImageResource(id)


        fun setVisibility(visibility: Int, vararg views: View) {
            for (item in views) item.visibility = visibility
        }

        fun setAlpha(alpha: Float, view: View) = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> view.alpha = alpha
            else -> {
                val alphaAnim = AlphaAnimation(alpha, alpha)
                alphaAnim.duration = 0
                alphaAnim.fillAfter = true
                view.startAnimation(alphaAnim)
            }
        }

        fun getX(view: View): Float = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> view.x
            else -> view.left.toFloat()
        }

        fun getY(view: View): Float = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> view.y
            else -> view.top.toFloat()
        }

        fun getPivotX(view: View): Float = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> view.pivotX
            else -> view.width / 2f
        }

        fun getPivotY(view: View): Float = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB -> view.pivotY
            else -> view.height / 2f
        }
    }
}