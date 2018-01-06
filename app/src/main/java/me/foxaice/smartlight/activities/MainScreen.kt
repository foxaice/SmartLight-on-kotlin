package me.foxaice.smartlight.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_screen.*
import me.foxaice.smartlight.R
import me.foxaice.smartlight.constants.BULB_MODE_TAG
import me.foxaice.smartlight.constants.MUSIC_MODE_TAG
import me.foxaice.smartlight.utils.animations.ButtonsAnimation

class MainScreen : AppCompatActivity(), ButtonsAnimation.ButtonsAnimationScreenView, View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        imageBulbMode.setOnClickListener(this)
        imageDiscoMode.setOnClickListener(this)
        imageMusicMode.setOnClickListener(this)
        newView = imageBulbMode
        newView.isSelected = false
        imageMusicMode.isSelected = true
        imageDiscoMode.isSelected = true
        newView.isEnabled = false
    }

    //todo redo to set fragments
    override fun setModeFragmentByTag(tag: String) = frameModeContent.setBackgroundColor(
            when (tag) {
                BULB_MODE_TAG -> R.color.backgroundBulbButton
                MUSIC_MODE_TAG -> R.color.backgroundMusicButton
                else -> R.color.backgroundDiscoButton
            })

    //todo delete this shit below
    private lateinit var oldView: View
    private lateinit var newView: View

    override fun onClick(v: View) {
        oldView = newView
        newView = v
        val otherView = getOtherView()
        ButtonsAnimation.init(screenView = this,
                modeContent = frameModeContent,
                waveBackground = relativeWaveBackground,
                screenContent = constraintContent,
                settingsContent = fragmentSettings,
                newView = newView,
                oldView = oldView,
                otherView = otherView
        ).startAnimation()
    }

    private fun getOtherView(): View {
        val newId = newView.id
        val oldId = oldView.id
        return when {
            newId != R.id.imageBulbMode && oldId != R.id.imageBulbMode -> imageBulbMode
            newId != R.id.imageMusicMode && oldId != R.id.imageMusicMode -> imageMusicMode
            else -> imageDiscoMode
        }
    }
}
