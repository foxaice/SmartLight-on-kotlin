package me.foxaice.smartlight.utils.animations

import android.os.Build
import android.view.View
import android.view.animation.Animation
import me.foxaice.smartlight.R
import me.foxaice.smartlight.constants.BULB_MODE_TAG
import me.foxaice.smartlight.constants.DISCO_MODE_TAG
import me.foxaice.smartlight.constants.MUSIC_MODE_TAG

abstract class ButtonsAnimation protected constructor(protected val screenView: ButtonsAnimationScreenView,
                                                      protected val modeContent: View,
                                                      protected val settingsContent: View,
                                                      protected val newView: View,
                                                      protected val oldView: View,
                                                      protected val otherView: View) {
    protected val modeTag: String = getModeTagFromModeButton(newView)
    protected lateinit var startAnim: Animation
    protected abstract fun prepareAnimation()

    interface ButtonsAnimationScreenView {
        fun setModeFragmentByTag(tag: String)
    }

    companion object {
        fun getModeTagFromModeButton(modeButton: View): String = when (modeButton.id) {
            R.id.imageBulbMode -> BULB_MODE_TAG
            R.id.imageMusicMode -> MUSIC_MODE_TAG
            else -> DISCO_MODE_TAG
        }

        fun init(screenView: ButtonsAnimationScreenView,
                 waveBackground: View, screenContent: View, modeContent: View, settingsContent: View,
                 newView: View, oldView: View, otherView: View): ButtonsAnimation {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return PostLollipopButtonsAnimations(screenView, waveBackground,
                        screenContent, modeContent, settingsContent, newView, oldView, otherView)
            } else {
                return PreLollipopButtonsAnimations(screenView, modeContent,
                        settingsContent, newView, oldView, otherView)
            }
        }
    }

    fun startAnimation() = newView.startAnimation(startAnim)
}