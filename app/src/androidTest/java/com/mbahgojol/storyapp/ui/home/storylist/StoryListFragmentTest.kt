package com.mbahgojol.storyapp.ui.home.storylist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.di.NetworkModule
import com.mbahgojol.storyapp.launchActivity
import com.mbahgojol.storyapp.utils.EspressoIdlingResource
import com.mbahgojol.storyapp.utils.JsonConverter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class StoryListFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        mockWebServer.start(8080)
        NetworkModule.baseUrl = "http://127.0.0.1:8080/"
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        mockWebServer.shutdown()
    }

    @Test
    fun getStory_Success() {
        launchActivity()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rvMain))
            .check(matches(isDisplayed()))
        onView(withText("Alfian"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickStory() {
        launchActivity()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("response.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rvMain))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rvMain))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("nrap21"))
                )
            )
    }

    @Test
    fun getStory_Error() {
        launchActivity()

        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.animationView))
            .check(matches(isDisplayed()))
    }
}