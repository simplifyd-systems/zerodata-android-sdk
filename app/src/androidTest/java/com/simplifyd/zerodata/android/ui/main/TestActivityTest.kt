package com.simplifyd.zerodata.android.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.TestActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestActivityTest {

    @Test
    fun activityLaunchesSuccessfully() {
        ActivityScenario.launch(TestActivity::class.java)

    }

    @Test
    fun appBarisClickable(){

        ActivityScenario.launch(TestActivity::class.java)

        onView(withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isNotClickable()))

    }
}