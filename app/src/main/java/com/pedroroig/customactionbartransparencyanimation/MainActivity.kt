package com.pedroroig.customactionbartransparencyanimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar.*


class MainActivity : AppCompatActivity() {

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

    private val onScrollChangeListener = ViewTreeObserver.OnScrollChangedListener {
        val scrollY = scrollView.scrollY
        // Height of the scrollView displayed in the activity
        val scrollHeight = scrollView.height

        // scrollRate will define the alpha. We can do it faster by increasing the multiplier
        val scrollRate = ((scrollY.toFloat() / scrollHeight.toFloat()) * 2).let {
            if(it < 1.0 ) it
            else 1F
        }

        toolbarFakeBackground.alpha = scrollRate
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
