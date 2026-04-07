package com.expenseai

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SourcesScreenTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    @Before fun inject() {
        hiltRule.inject()
        composeRule.onNodeWithText("Sources").performClick()
    }

    @Test fun sourcesScreenShowsSmsCard() {
        composeRule.onNodeWithText("SMS Inbox").assertIsDisplayed()
    }

    @Test fun sourcesScreenShowsEmailCard() {
        composeRule.onNodeWithText("Email (Share)").assertIsDisplayed()
    }

    @Test fun smsSwitchIsOffByDefault() {
        composeRule.onNodeWithText("SMS Inbox")
            .onSiblings()
            .filterToOne(hasClickAction())
            .assertIsOff()
    }
}
