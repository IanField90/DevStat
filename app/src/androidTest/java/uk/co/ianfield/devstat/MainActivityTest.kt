package uk.co.ianfield.devstat

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.withText

/**
 * Created by Ian on 20/11/2015.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    var mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun checkAboutIsLaunched() {
        Intents.init()
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        onView(withText(R.string.action_about)).perform(click())
        intended(hasComponent(AboutActivity::class.java!!.getName()))
        Intents.release()
    }

}
