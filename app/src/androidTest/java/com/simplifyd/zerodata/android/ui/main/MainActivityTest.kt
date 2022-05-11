package com.simplifyd.zerodata.android.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplifyd.zerodata.android.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun activityLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)

    }

    @Test
    fun appBarisClickable(){

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.app_bar))
            .check(ViewAssertions.matches(ViewMatchers.isNotClickable()))

    }
}