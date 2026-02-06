@ui @user @plants @security @admin
Feature: Edit Plant Page Restriction for Regular User

  @TC_UI_PLANTS_USER_11 @smoke @regression
  Scenario: TC_UI_PLANTS_USER_11 - Verify User cannot access Edit Plant page
    """
    Test Summary: Verify User cannot access Edit Plant page.
    Test Description: Test Admin-only edit page restriction for a regular User.
    Precondition: Regular User logged in (non-admin), Plant with ID 1 exists in the database.
    """

    # Setup prerequisites
    Given testuser is logged into the application
    And user navigates to Plants List page
    And user is on Plants List page

    # Attempt to access Edit Plant page
    When user manually navigates to the Edit Plant page "/ui/plants/edit/1"
    Then user should be restricted from accessing the Edit Plant page
    And user should be redirected to either login page or error page
    And the URL should change to indicate restriction
    And a clear permission error should be displayed

    # Verify specific restriction behaviors
    And the system should show one of the following:
      | Restriction Type | Description |
      | Login Redirect   | Redirected to login page |
      | 403 Forbidden     | 403 Forbidden error page |
      | Access Denied    | Access Denied message |

    # Verify no edit form is displayed
    And no edit form should be displayed to the user
    And the edit form fields should not be accessible
    And save/update buttons should not be available