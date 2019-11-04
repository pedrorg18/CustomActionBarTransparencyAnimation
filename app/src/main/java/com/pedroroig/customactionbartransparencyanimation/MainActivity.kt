package com.pedroroig.customactionbartransparencyanimation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import android.animation.ObjectAnimator


class MainActivity : AppCompatActivity() {

    private val transparenceAnimationTime = 500L

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

    private val onScrollChangeListener = ViewTreeObserver.OnScrollChangedListener {
        val scrollY = scrollView.scrollY
        // Height of the scrollView displayed in the activity
        val scrollHeight = scrollView.height

        // Scrollable region is the difference between scrollView and its child's height, because the child will
        // scroll until it reaches the bottom and it's fully visible, and scrollY gets us the TOP position
        val scrollableRegion = scrollView.getChildAt(0).height - scrollHeight
        val scrollPercentage = scrollY.toFloat() / scrollableRegion.toFloat() * 100

        if(scrollPercentage < 0) // avoid top and bottom screen bouncing
            previousScrollPercentage = 0F
        else if(scrollPercentage > 100)
            previousScrollPercentage = 100F
        else {
            if (scrollPercentage > previousScrollPercentage) {
                if(toolbarFakeBackground.alpha == 0F) {
                    Log.i(
                        "SCROLL:::",
                        "DOWN - scrollPercentage: $scrollPercentage previousScrollPercentage: $previousScrollPercentage"
                    )
                    val anim = ObjectAnimator.ofFloat(toolbarFakeBackground, "alpha", 1f)
                    anim.duration = transparenceAnimationTime
                    anim.start()
                }

            } else {
                if(toolbarFakeBackground.alpha == 1F && scrollY < (2 * toolbarFakeBackground.height)) {
                    Log.i(
                        "SCROLL:::",
                        "UP - scrollPercentage: $scrollPercentage previousScrollPercentage: $previousScrollPercentage"
                    )
                    val anim = ObjectAnimator.ofFloat(toolbarFakeBackground, "alpha", 0f)
                    anim.duration = transparenceAnimationTime
                    anim.start()
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
