package com.pedroroig.customactionbartransparencyanimation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.pedroroig.customactionbartransparencyanimation.extensions.show
import com.pedroroig.customactionbartransparencyanimation.utils.changeWidthAnimation
import com.pedroroig.customactionbartransparencyanimation.utils.fadeAnimation
import com.pedroroig.customactionbartransparencyanimation.utils.unFadeAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar.*


class MainActivity : AppCompatActivity() {

    private val transparencyAnimationTime = 400L

    private lateinit var actionBarView: View
    private lateinit var actionBarParent: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initActionBar()
    }

    override fun onResume() {
        super.onResume()
        addScrollViewListener()
    }

    override fun onStop() {
        super.onStop()
        removeScrollViewListener()
    }

    private fun addScrollViewListener() {
        scrollView.viewTreeObserver.addOnScrollChangedListener(onScrollChangeListener)
    }

    private fun removeScrollViewListener() {
        scrollView.viewTreeObserver.removeOnScrollChangedListener(onScrollChangeListener)
    }

    private var previousScrollPercentage = 0F

    private val fadeToolBarAnimation: ObjectAnimator by lazy {
        fadeAnimation(toolbarFakeBackground, transparencyAnimationTime)
    }

    private val unFadeToolBarAnimation: ObjectAnimator by lazy {
        unFadeAnimation(toolbarFakeBackground, transparencyAnimationTime)
    }

    private val expandSearchBarAnimation: ValueAnimator by lazy {
        changeWidthAnimation(
            llSearchBox, transparencyAnimationTime, search_icon_vc.width, containerSearchBarWrapper.width) {
                search_vc.show()
        }
    }

    private val contractSearchBarAnimation: ValueAnimator by lazy {
        changeWidthAnimation(
            llSearchBox,
            transparencyAnimationTime,
            containerSearchBarWrapper.width,
            search_icon_vc.width
        )
    }

    private val onScrollChangeListener = ViewTreeObserver.OnScrollChangedListener {
        val scrollY = scrollView.scrollY
        // Height of the scrollView displayed in the activity
        val scrollHeight = scrollView.height

        // Scrollable region is the difference between scrollView and its child's height, because the child will
        // scroll until it reaches the bottom and it's fully visible, and scrollY gets us the TOP position
        val scrollableRegion = scrollView.getChildAt(0).height - scrollHeight
        val scrollPercentage = scrollY.toFloat() / scrollableRegion.toFloat() * 100

        if (scrollPercentage < 0) // avoid top and bottom screen bouncing
            previousScrollPercentage = 0F
        else if (scrollPercentage > 100)
            previousScrollPercentage = 100F
        else {
            if (scrollPercentage > previousScrollPercentage) {
                if (toolbarFakeBackground.alpha == 0F) {
                    Log.i(
                        "SCROLL:::",
                        "DOWN - scrollPercentage: $scrollPercentage previousScrollPercentage: $previousScrollPercentage"
                    )
                    unFadeToolBarAnimation.start()
                    expandSearchBarAnimation.start()
                }

            } else {
                if (toolbarFakeBackground.alpha == 1F && scrollY < (2 * toolbarFakeBackground.height)) {
                    Log.i(
                        "SCROLL:::",
                        "UP - scrollPercentage: $scrollPercentage previousScrollPercentage: $previousScrollPercentage"
                    )
                    fadeToolBarAnimation.start()
                    contractSearchBarAnimation.start()
                }
            }

            previousScrollPercentage = scrollPercentage
        }
    }

    private fun initActionBar() {
        setSupportActionBar(mainToolbar)
        val activityActionBar = supportActionBar!!

        activityActionBar.setDisplayShowHomeEnabled(false)
        activityActionBar.setDisplayShowTitleEnabled(false)
        activityActionBar.setDisplayShowCustomEnabled(true)

        actionBarView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null)
        activityActionBar.customView = actionBarView

        // Fix the actionBar to the parent without horizontal margins
        actionBarParent = actionBarView.parent as Toolbar
        actionBarParent.setPadding(0, 0, 0, 0)
        actionBarParent.setContentInsetsAbsolute(0, 0)

    }

}
