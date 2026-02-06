@ui @user @plants @security @admin
Feature: Admin-only Page Restriction for Regular User

  @TC_UI_PLANTS_USER_10 @smoke @regression
  Scenario: TC_UI_PLANTS_USER_10 - Test Admin-only page restriction
    """
    Test Summary: Test Admin-only page restriction.
    Test Description: Verify that a regular User cannot access the Admin-only Add Plant page and is appropriately restricted.
    Precondition: User logged in (as a regular User, not an Admin), User is on the Plants List page.
    """

    # Setup prerequisites
    Given testuser is logged into the application
    And user navigates to Plants List page
    And user is on Plants List page

    # Attempt to access Admin-only page
    When user manually navigates to the Admin-only URL "/ui/plants/add"
    Then user should be restricted from accessing the page
    And user should be redirected to either login page or error page
    And the URL should change to indicate restriction
    And a clear permission error should be displayed

    # Verify specific restriction behaviors
    And the system should show one of the following:
      | Restriction Type | Description |
      | Login Redirect   | Redirected to login page |
      | 403 Forbidden     | 403 Forbidden error page |
      | Access Denied    | Access Denied message |

    # Verify error message visibility
    And an error message should be displayed on the screen
    And the error message should indicate insufficient permissions