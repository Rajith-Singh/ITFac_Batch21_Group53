@ui @user @sales @navigation
Feature: Sales Navigation Functionality

  @TC_UI_SAL_14 @smoke @regression
  Scenario: TC_UI_SAL_14 - Verify "Sell Plant" button navigation
    """
    Test Summary: Verify "Sell Plant" button navigation.
    Test Description: Admin clicks Sell Plant button to navigate.
    Precondition: Admin logged in, User is on Sales List page.
    """

    # Setup prerequisites
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And Sell Plant button is visible

    # Test execution
    When user clicks Sell Plant button
    Then URL should change to "/ui/sales/new"
    And URL should contain "/ui/sales/new"
    And Sell Plant form should load successfully

  # Expected Results verified in step definitions:
  # - Navigates to /ui/sales/new
  # - URL updates accordingly  
  # - Sell Plant form loads successfully

  @TC_UI_SAL_15 @validation @regression
  Scenario: TC_UI_SAL_15 - Verify error when Plant not selected
    """
    Test Summary: Verify error when Plant not selected.
    Test Description: Attempt to submit form without selecting a plant.
    Precondition: Admin logged in, User is on Sales List page, User is on /ui/sales/new page.
    """

    # Setup prerequisites
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And Sell Plant button is visible
    When user clicks Sell Plant button
    And Sell Plant form should load successfully

    # Test execution
    When user leaves Plant dropdown empty
    And user enters "2" in Quantity field
    And user clicks Sell button
    Then form submission should be handled
    And error message should appear below Plant dropdown with text "Plant is required"
    And Sell button should remain enabled

  # Expected Results verified in step definitions:
  # - Form is not submitted
  # - Stays on same page (/ui/sales/new)
  # - Red error message appears below Plant dropdown
  # - Error message text: "Plant is required"
  # - Sell button remains enabled

  @TC_UI_SAL_16 @multi-tab @stock-update @regression
  Scenario: TC_UI_SAL_16 - Verify dropdown updates after sales (if multiple tabs/windows)
    """
    Test Summary: Verify dropdown updates after sales (if multiple tabs/windows).
    Test Description: Test if plant stock updates in dropdown after a sale is made in another tab or window.
    Precondition: Admin logged in, Two browser tabs open with Sell Plant form, Plant "Mango Tree" has initial stock of 10.
    """

    # Setup prerequisites - Tab1
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And Sell Plant button is visible
    When user clicks Sell Plant button
    Then Sell Plant form should load successfully
    And plant "Mango Tree" is available in dropdown
    And Mango Tree has initial stock of 10

    # Setup prerequisites - Tab2 (Open second tab)
    When user opens new tab and navigates to Sell Plant form
    And user switches to Tab1

    # Test execution - Tab1: Make a sale
    When user selects "Mango Tree" from plant dropdown
    And user enters "3" in Quantity field
    And user clicks Sell button
    Then sale should be successful
    And user navigates back to Sell Plant form
    And stock for "Mango Tree" should be 7 in Tab1

    # Test execution - Tab2: Verify stock update after refresh
    When user switches to Tab2
    And user refreshes the page
    Then page should refresh without errors
    And plant "Mango Tree" should be available in dropdown after refresh
    And stock for "Mango Tree" should be 7 in Tab2