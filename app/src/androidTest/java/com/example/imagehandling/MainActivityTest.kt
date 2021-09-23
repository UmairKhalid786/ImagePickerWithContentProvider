package com.example.imagehandling

import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun takeActivityToCreatedStateTest () {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
    }

    @Test
    fun takeActivityToStartedStateTest () {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun takeActivityToResumedStateTest () {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun takeActivityToDestroyedStateTest () {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }


    @Test
    fun activityResultTest() {

        // Create an expected result URI
        val testUrl = "file//dummy_file.test"
        val expectedResult = Uri.parse(testUrl)

        // Create the test ActivityResultRegistry
        val testRegistry = object : ActivityResultRegistry() {
            override fun <I, O> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                dispatchResult(requestCode, expectedResult)
            }
        }

        val uri = ImageContractHandler(testRegistry).getImageFromGallery().getOrAwaitValue()
        assert(uri == expectedResult)
    }
}