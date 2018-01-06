package me.foxaice.smartlight.utils.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.*
import me.foxaice.smartlight.R
import me.foxaice.smartlight.constants.BULB_MODE_TAG
import me.foxaice.smartlight.constants.MUSIC_MODE_TAG
import me.foxaice.smartlight.utils.ViewUtils

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class PostLollipopButtonsAnimations(screenView: ButtonsAnimationScreenView,
                                    private val waveBackground: View,
                                    private val contentView: View,
                                    modeContent: View, settingsContent: View,
                                    newView: View, oldView: View,
                                    otherView: View) : ButtonsAnimation(screenView, modeContent, settingsContent, newView, oldView, otherView) {

    private val mWaveColor: Int = ContextCompat.getColor(newView.context, when (modeTag) {
        BULB_MODE_TAG -> R.color.backgroundBulbButton
        MUSIC_MODE_TAG -> R.color.backgroundMusicButton
        else -> R.color.backgroundDiscoButton
    })
    private val mFinalColor: Int = ContextCompat.getColor(newView.context, R.color.backgroundActivity)

    init {
        prepareAnimation()
    }

    override fun prepareAnimation() {
        val coords = intArrayOf(0, 0)
        newView.getLocationInWindow(coords)

        val revealX = coords[0] + newView.width / 2
        val revealY = (coords[1] - newView.height * 1.4f).toInt()
        val radius = Math.max(waveBackground.height, waveBackground.width) * 1.1f
        val pivotX = ViewUtils.getPivotX(newView)
        val pivotY = ViewUtils.getPivotY(newView)

        val firstAnim = ViewAnimationUtils.createCircularReveal(waveBackground, revealX, revealY, pivotX, radius)
        val secondAnim = ViewAnimationUtils.createCircularReveal(contentView, revealX, revealY, pivotX, radius)
        val firstScaleUp = ScaleAnimation(1f, 2f, 1f, 2f, pivotX, pivotY)
        val firstScaleDown = ScaleAnimation(1f, 0.5f, 1f, 0.5f, pivotX, pivotY)
        val secondScaleDown = ScaleAnimation(1f, 0.1f, 1f, 0.1f, pivotX, pivotY)
        val secondScaleUp = ScaleAnimation(0.1f, 1f, 0.1f, 1f, pivotX, pivotY)

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(firstScaleUp)
        animationSet.addAnimation(firstScaleDown)

        firstScaleDown.startOffset = 100

        AnimationUtils.setDuration(100, firstScaleUp, secondScaleUp)
        AnimationUtils.setDuration(200, firstScaleDown)
        AnimationUtils.setDuration(250, secondScaleDown)
        AnimationUtils.setDuration(300, firstAnim)
        AnimationUtils.setDuration(400, secondAnim)

        AnimationUtils.setInterpolator(OvershootInterpolator(), animationSet, secondScaleDown, secondScaleUp)
        AnimationUtils.setInterpolator(AccelerateInterpolator(), secondAnim)

        setAnimationListeners(
                animator1 = firstAnim,
                animator2 = secondAnim,
                animation1 = animationSet,
                animation2 = secondScaleDown,
                animation3 = secondScaleUp
        )

        startAnim = animationSet
    }

    /* Sequence of Animations
     *  *start----->end*
     *
     * animation1-->animation1
     *                       |
     *                       animator1-->animator1
     *                                           |
     *                                           animation2-->animation2
     *                                                    |
     *                                                    animation3-->animation3
     *                                                                          |
     *                                                                          animator2-->animator2
     */
    private fun setAnimationListeners(animator1: Animator, animator2: Animator,
                                      animation1: Animation, animation2: Animation, animation3: Animation) {

        animation1.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                ViewUtils.bringToFront(newView)
                ViewUtils.setEnabled(false, newView, oldView, otherView)
            }

            override fun onAnimationEnd(animation: Animation) = animator1.start()
        })

        animator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                ViewUtils.setEnabled(false, newView, oldView, otherView)
                waveBackground.setBackgroundColor(mWaveColor)
                ViewUtils.bringToFront(waveBackground, newView, settingsContent)
            }

            override fun onAnimationEnd(animation: Animator) = newView.startAnimation(animation2)
        })

        animation2.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) = newView.startAnimation(animation3)
        })

        animation3.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                newView.isSelected = false
                oldView.isSelected = true
            }

            override fun onAnimationEnd(animation: Animation) = animator2.start()
        })

        animator2.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                (contentView.getParent() as View).setBackgroundColor(mWaveColor)
                contentView.setBackgroundColor(mFinalColor)
                ViewUtils.bringToFront(newView, oldView, otherView, modeContent, settingsContent)
                screenView.setModeFragmentByTag(modeTag)
            }

            override fun onAnimationEnd(animation: Animator) {
                ViewUtils.setEnabled(false, newView)
                ViewUtils.setEnabled(true, oldView, otherView)
                contentView.setBackgroundColor(mFinalColor)
            }
        })
    }
}