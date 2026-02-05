@ui @user @sales @sorting
Feature: Sales List Total Price Sorting Functionality

  @TC_UI_SAL_13 @smoke @regression
  Scenario: TC_UI_SAL_13 - Verify sorting by Total Price column
    """
    Test Summary: Verify sorting by Total Price column.
    Test Description: Test monetary value sorting.
    Precondition: User logged in, Sales with prices: $9.99, $24.50, $49.99, $120.00, User is on Sales List page.
    """

    # Setup prerequisites
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Total Price column header is visible and clickable
    And verify initial total prices are displayed

    # First click - Changes to ascending sort
    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order
    And expected ascending order should be "$9.99", "$24.50", "$49.99", "$120.00"

    # Second click - Back to descending sort
    When user clicks Total Price column header again
    Then total prices should be sorted in descending order
    And expected descending order should be "$120.00", "$49.99", "$24.50", "$9.99"

  @TC_UI_SAL_13_positive @ascending_price
  Scenario: TC_UI_SAL_13_Positive - Verify ascending sort by Total Price
    """
    Verify that clicking Total Price header sorts in ascending order (low to high)
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Total Price column header is visible and clickable

    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order
    And expected ascending order should be "$9.99", "$24.50", "$49.99", "$120.00"

  @TC_UI_SAL_13_negative @descending_price
  Scenario: TC_UI_SAL_13_Negative - Verify descending sort by Total Price
    """
    Verify that clicking Total Price header again sorts in descending order (high to low)
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Total Price column header is visible and clickable

    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order

    When user clicks Total Price column header again
    Then total prices should be sorted in descending order
    And expected descending order should be "$120.00", "$49.99", "$24.50", "$9.99"

  @TC_UI_SAL_13_toggle @toggle_price_sort
  Scenario: TC_UI_SAL_13_Toggle - Verify Total Price sort toggle functionality
    """
    Verify that Total Price column sorting toggles between ascending and descending
    """

    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Total Price column header is visible and clickable

    # First click should sort ascending
    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order

    # Second click should sort descending
    When user clicks Total Price column header again
    Then total prices should be sorted in descending order

    # Third click should sort ascending again
    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order