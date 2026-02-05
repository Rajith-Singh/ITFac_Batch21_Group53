@ui @user @sales @sorting
Feature: Sales List Sorting Functionality

  @TC_UI_SAL_11 @smoke @regression
  Scenario: TC_UI_SAL_11 - Verify sorting by Plant Name column
    """
    Test Summary: Verify sorting by Plant Name column.
    Test Description: Click Plant Name header to toggle between ascending and descending sort.
    Precondition: User logged in, Sales for plants exist, User is on Sales List page.
    """

    # Setup prerequisites
    Given user is logged into the application
    And user navigates to Sales List page
    And user is on Sales List page
    And sales records are displayed for plants
    And Plant Name column header is visible and clickable
    And verify initial plant names are displayed

    # Initial state - Already sorted descending (default)
    
    And expected descending order should be "ZZZ Plant", "Money Plant", "Aloe Vera"

    # First click - Changes to ascending sort
    When user clicks Plant Name column header once
    Then sales should be sorted in ascending order by Plant Name
    And expected ascending order should be "Aloe Vera", "Money Plant", "ZZZ Plant"

    # Second click - Back to descending sort
    When user clicks Plant Name column header again
    Then sales should be sorted in descending order by Plant Name
    And expected descending order should be "ZZZ Plant", "Money Plant", "Aloe Vera"