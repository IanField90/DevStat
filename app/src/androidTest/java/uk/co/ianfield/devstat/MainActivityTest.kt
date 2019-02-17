package uk.co.ianfield.devstat

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
        intended(hasComponent(AboutActivity::class.java.name))
        Intents.release()
    }

}
