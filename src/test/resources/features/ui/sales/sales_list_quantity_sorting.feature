@ui @user @sales @sorting
Feature: Sales List Quantity Sorting Functionality

  @TC_UI_SAL_12 @smoke @regression
  Scenario: TC_UI_SAL_12 - Verify sorting by Quantity column
    """
    Test Summary: Verify sorting by Quantity column.
    Test Description: Test quantity sorting with numeric values.
    Precondition: User is on Sales List page.
    """

    # Setup prerequisites
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Quantity column header is visible and clickable
    And verify initial quantities are displayed

    # Initial state - Check current sort order (descending)
    
    And expected descending order should be 5, 3, 2

    # First click - Changes to ascending sort
    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order
    And expected ascending order should be 2, 3, 5

    # Second click - Back to descending sort
    When user clicks Quantity column header again
    Then quantities should be sorted in descending order
    And expected descending order should be 5, 3, 2

    # Third click - Back to ascending sort
    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order
    And expected ascending order should be 2, 3, 5

    # First click - Changes to descending sort
    When user clicks Quantity column header once
    Then quantities should be sorted in descending order
    And expected descending order should be 5, 3, 2

    # Second click - Back to ascending sort
    When user clicks Quantity column header again
    Then quantities should be sorted in ascending order
    And expected ascending order should be 2, 3, 5

  @TC_UI_SAL_12_positive @ascending_quantity
  Scenario: TC_UI_SAL_12_Positive - Verify ascending sort by Quantity
    """
    Verify that clicking Quantity header sorts in ascending order (low to high)
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Quantity column header is visible and clickable

    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order
    And expected ascending order should be 2, 3, 5

  @TC_UI_SAL_12_negative @descending_quantity
  Scenario: TC_UI_SAL_12_Negative - Verify descending sort by Quantity
    """
    Verify that clicking Quantity header again sorts in descending order (high to low)
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Quantity column header is visible and clickable

    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order

    When user clicks Quantity column header again
    Then quantities should be sorted in descending order
    And expected descending order should be 5, 3, 2

  @TC_UI_SAL_12_toggle @toggle_quantity_sort
  Scenario: TC_UI_SAL_12_Toggle - Verify Quantity sort toggle functionality
    """
    Verify that Quantity column sorting toggles between ascending and descending
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Quantity column header is visible and clickable

    # First click should sort ascending
    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order

    # Second click should sort descending
    When user clicks Quantity column header again
    Then quantities should be sorted in descending order

    # Third click should sort ascending again
    When user clicks Quantity column header once
    Then quantities should be sorted in ascending order