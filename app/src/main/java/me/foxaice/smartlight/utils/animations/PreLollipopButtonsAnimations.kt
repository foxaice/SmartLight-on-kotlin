package me.foxaice.smartlight.utils.animations

import android.view.View
import android.view.animation.*
import me.foxaice.smartlight.utils.ViewUtils

class PreLollipopButtonsAnimations(screenView: ButtonsAnimation.ButtonsAnimationScreenView,
                                   modeContent: View,
                                   settingsContent: View,
                                   newView: View,
                                   oldView: View,
                                   otherView: View) : ButtonsAnimation(screenView, modeContent, settingsContent, newView, oldView, otherView) {
    init {
        prepareAnimation()
    }

    override fun prepareAnimation() {
        val oldViewPivotX = ViewUtils.getPivotX(oldView)
        val oldViewPivotY = ViewUtils.getPivotY(oldView)
        val newViewPivotX = ViewUtils.getPivotX(newView)
        val newViewPivotY = ViewUtils.getPivotY(newView)
        val modeContentPivotX = ViewUtils.getPivotX(modeContent)
        val modeContentPivotY = ViewUtils.getPivotY(modeContent)

        val downScalePreView = ScaleAnimation(1f, 0f, 1f, 0f, oldViewPivotX, oldViewPivotY)
        val upScalePreView = ScaleAnimation(0f, 1f, 0f, 1f, oldViewPivotX, oldViewPivotY)
        val firstUpScaleCurView = ScaleAnimation(1f, 2f, 1f, 2f, newViewPivotX, newViewPivotY)
        val downScaleCurView = ScaleAnimation(2f, 0f, 2f, 0f, newViewPivotX, newViewPivotY)
        val secondUpScaleCurView = ScaleAnimation(0f, 1f, 0f, 1f, newViewPivotX, newViewPivotY)
        val downScaleModeContent = ScaleAnimation(1f, 0f, 1f, 0f, modeContentPivotX, modeContentPivotY)
        val translateModeContent = TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        )

        AnimationUtils.setDuration(300,
                downScalePreView, upScalePreView, firstUpScaleCurView, downScaleCurView, downScaleModeContent)
        AnimationUtils.setDuration(1000,
                secondUpScaleCurView, translateModeContent)

        AnimationUtils.setInterpolator(AnticipateInterpolator(),
                downScaleModeContent)
        AnimationUtils.setInterpolator(OvershootInterpolator(),
                downScalePreView, upScalePreView, firstUpScaleCurView, downScaleCurView, secondUpScaleCurView, translateModeContent)

        setAnimationListeners(
                an1 = firstUpScaleCurView,
                an2 = downScalePreView,
                an3 = downScaleCurView,
                an4 = upScalePreView,
                an5 = downScaleModeContent,
                an6 = secondUpScaleCurView,
                an7 = translateModeContent
        )

        startAnim = firstUpScaleCurView
    }

    /* Sequence of Animations
         *  *start----->end*
         *
         * an1------------->an1
         *   |                |
         *   |                an3------------------->an3
         *   |                  |                      |
         *   |                  an5------------>an5    |
         *   |                                         |
         *   |                                         an6----------->an6
         *   |                                         |
         *   |                                         an7----------->an7
         *   an2----------------->an2
         *                          |
         *                          an4------------>an4
         */
    private fun setAnimationListeners(an1: Animation, an2: Animation, an3: Animation,
                                      an4: Animation, an5: Animation, an6: Animation, an7: Animation) {

        an1.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                ViewUtils.setEnabled(false, newView, oldView, otherView)
                ViewUtils.bringToFront(newView, oldView, otherView, settingsContent)
                oldView.startAnimation(an2)
            }

            override fun onAnimationEnd(animation: Animation) = newView.startAnimation(an3)
        })

        an2.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) = oldView.startAnimation(an4)
        })

        an3.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) = modeContent.startAnimation(an5)

            override fun onAnimationEnd(animation: Animation) = newView.startAnimation(an6)
        })

        an4.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                oldView.isSelected = true
            }
        })

        an6.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                screenView.setModeFragmentByTag(modeTag)
                newView.isSelected = false
                modeContent.startAnimation(an7)
            }

            override fun onAnimationEnd(animation: Animation) {
                ViewUtils.setEnabled(false, newView)
                ViewUtils.setEnabled(true, oldView, otherView)
            }
        })

        an7.setAnimationListener(object : AnimationUtils.AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) = ViewUtils.bringToFront(newView, modeContent, oldView, otherView)
        })
    }
}