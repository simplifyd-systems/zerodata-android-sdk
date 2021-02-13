package com.simplifydvpn.android.ui.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.simplifydvpn.android.R
import com.simplifydvpn.android.utils.animateStatusBarColorChangeTo
import com.simplifydvpn.android.utils.getColorHexString
import com.simplifydvpn.android.utils.getColorInt
import com.simplifydvpn.android.utils.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

        }

        observeGetProtectMe()

        val navController = Navigation.findNavController(this@MainActivity, R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isOverviewScreenActive = destination.id == R.id.navigation_overview
            Log.d("SSS", isOverviewScreenActive.toString())
            btnProtectMe.isGone = isOverviewScreenActive.not()
            hamburger.isGone = isOverviewScreenActive.not()
            imageView2.isGone = isOverviewScreenActive.not()
            toolbar.isInvisible= isOverviewScreenActive
        }

        toolbar.setNavigationOnClickListener {
            navController.navigateUp() || super.onSupportNavigateUp()
        }



    }

    private fun observeGetProtectMe() {
        viewModel.getProtectMeStatus.observe(this, Observer{
            customiseProtectMeButton(it)
        })
    }

     fun customiseProtectMeButton(protectMe: Boolean) {
        btnProtectMe.apply {
            if (protectMe) {
                backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorGreen)
                strokeColor = ColorStateList.valueOf(getColorInt(R.color.colorGreen))
                icon = ContextCompat.getDrawable(context, R.drawable.ic_check_shield)
                iconTint = ColorStateList.valueOf(getColorInt(R.color.white))
                setTextColor(getColorInt(R.color.white))
                text = getString(R.string.you_re_protected)
            } else {
                backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorDarkRed)
                strokeColor = ColorStateList.valueOf(getColorInt(R.color.colorDarkRed))
                icon = ContextCompat.getDrawable(context, R.drawable.ic_remove_shield)
                iconTint = ColorStateList.valueOf(getColorInt(R.color.white))
                setTextColor(getColorInt(R.color.white))
                text = getString(R.string.protect_me)
            }
        }
    }

}