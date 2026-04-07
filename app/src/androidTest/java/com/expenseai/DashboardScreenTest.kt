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
class DashboardScreenTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    @Before fun inject() { hiltRule.inject() }

    @Test fun emptyStateLabelIsVisible() {
        composeRule.onNodeWithText("No expenses yet. Tap + to add one!").assertIsDisplayed()
    }

    @Test fun fabIsDisplayed() {
        composeRule.onNodeWithContentDescription("Add expense").assertIsDisplayed()
    }

    @Test fun addExpenseDialogOpensOnFabClick() {
        composeRule.onNodeWithContentDescription("Add expense").performClick()
        composeRule.onNodeWithText("Add Expense").assertIsDisplayed()
    }

    @Test fun addExpenseDialogDismissesOnCancel() {
        composeRule.onNodeWithContentDescription("Add expense").performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        composeRule.onNodeWithText("Add Expense").assertDoesNotExist()
    }
}
