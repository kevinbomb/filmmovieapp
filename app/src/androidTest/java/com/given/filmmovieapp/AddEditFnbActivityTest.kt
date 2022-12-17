package com.given.filmmovieapp


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddEditFnbActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(AddEditFnbActivity::class.java)

    @Test
    fun addEditFnbActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText = onView(
            allOf(
                withId(R.id.et_namafnb),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_namafnb),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("Bebek"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.et_jenisfnb),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_jenisfnb),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("Makanan"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_jumlahfnb),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_jumlahfnb),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText3.perform(scrollTo(), replaceText("5"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        onView(isRoot()).perform(waitFor(3000))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.et_hargafnb),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.layout_hargafnb),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText4.perform(scrollTo(), replaceText("24000"), closeSoftKeyboard())

        val materialButton5 = onView(
            allOf(
                withId(R.id.btn_save), withText("Simpan"),
                childAtPosition(
                    allOf(
                        withId(R.id.ll_button),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        onView(isRoot()).perform(waitFor(3000))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
    private fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $delay milliseconds."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
