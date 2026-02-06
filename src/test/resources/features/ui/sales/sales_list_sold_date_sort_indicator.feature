@ui @user @sales @sorting @date @indicator
Feature: Sales List Sold Date Sort Indicator

  @TC_UI_SALES_USER_07 @smoke @regression
  Scenario: TC_UI_SALES_USER_07 - Verify Sold Date column shows descending sort indicator
    """
    Test Summary: Verify Sold Date column shows descending sort indicator.
    Test Description: Check for visual feedback that indicates the current sort column and direction when the Sales List page loads.
    Precondition: User logged in, Multiple sales exist with different dates, User is on the Sales List page.
    """

    # Setup prerequisites
    Given testuser is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And verify initial sold dates are displayed

    # Load the Sales List page and check for visual indicators
    When the Sales List page loads
    Then the "Sold Date" column header should display a visual sort indicator
    And the sort indicator should be a downward arrow (↓) indicating descending order
    And no other column headers should show any sort indicators
    And the "Plant Name" column header should not show any sort indicators
    And the "Quantity" column header should not show any sort indicators
    And the "Total Price" column header should not show any sort indicators

    # Verify the sort indicator corresponds to actual sorting behavior
    And sales should be sorted in descending order by Sold Date
    And the visual indicator should match the actual sort direction