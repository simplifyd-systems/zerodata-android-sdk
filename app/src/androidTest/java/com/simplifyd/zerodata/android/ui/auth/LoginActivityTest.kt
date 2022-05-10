package com.simplifyd.zerodata.android.ui.auth

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Test
    fun activityLaunchesSuccessfully() {
        ActivityScenario.launch(LoginActivity::class.java)

    }

}