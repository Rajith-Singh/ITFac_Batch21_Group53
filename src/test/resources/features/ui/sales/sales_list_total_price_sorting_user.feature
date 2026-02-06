@ui @user @sales @sorting @totalprice
Feature: Sales List Total Price Sorting for Regular User

  @TC_UI_SALES_USER_08 @smoke @regression
  Scenario: TC_UI_SALES_USER_08 - Verify User can sort sales by Total Price
    """
    Test Summary: Verify User can sort sales by Total Price.
    Test Description: Test monetary value sorting functionality for a Regular User on the Sales List page.
    Precondition: User logged in, User is on the Sales List page, Sales exist with the Total Prices.
    """

    # Setup prerequisites
    Given testuser is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And verify initial total prices are displayed

    # First click - Verify ascending sort order
    When user clicks Total Price column header once
    Then total prices should be sorted in ascending order
    And the first record should show the lowest price
    And the last record should show the highest price
    And expected ascending order should be "$10.00", "$20.00", "$30.00", "$40.00"

    # Second click - Verify descending sort order
    When user clicks Total Price column header again
    Then total prices should be sorted in descending order
    And the first record should show the highest price
    And the last record should show the lowest price
    And expected descending order should be "$40.00", "$30.00", "$20.00", "$10.00"