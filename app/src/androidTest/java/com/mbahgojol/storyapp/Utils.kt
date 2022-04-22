package com.mbahgojol.storyapp

import androidx.test.core.app.ActivityScenario
import com.mbahgojol.storyapp.ui.home.MainActivity

fun launchActivity(): ActivityScenario<MainActivity> =
    ActivityScenario.launch(MainActivity::class.java)