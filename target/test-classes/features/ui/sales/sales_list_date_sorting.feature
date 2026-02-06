@ui @user @sales @sorting @date
Feature: Sales List Default Date Sorting

  @TC_UI_SALES_USER_06 @smoke @regression
  Scenario: TC_UI_SALES_USER_06 - Verify sales data defaults to newest-first order
    """
    Test Summary: Verify sales data defaults to newest-first order.
    Test Description: Validate that sales records are automatically sorted by Sold Date in descending order (newest first) when the Sales List page loads.
    Precondition: User logged in, Multiple sales exist with different dates.
    """

    # Setup prerequisites
    Given testuser is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And verify initial sold dates are displayed

    # Verify default sort order is newest-first (descending by Sold Date)
    Then sales should be sorted in descending order by Sold Date
    And the first record should show the most recent date
    And the second record should show the next most recent date
    And the last record should show an older date

    # Verify consistency after refresh
    When user refreshes the sales list page
    And user is on Sales List page
    Then sales should still be sorted in descending order by Sold Date
    And the sort order remains consistent after refreshing