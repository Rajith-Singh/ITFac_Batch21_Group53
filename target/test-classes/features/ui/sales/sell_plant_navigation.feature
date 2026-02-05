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