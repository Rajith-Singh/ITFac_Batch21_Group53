@ui @dashboard
Feature: Dashboard Functionality
  As a user of the Plant Sales application
  I want to access and interact with the Dashboard
  So that I can view summary information and navigate to different modules

  Background:
    Given the application is running

  # Admin User Test Cases
  @admin @smoke
  Scenario: Dashboard loads immediately after Admin login
    Given Admin account exists and is active
    And Admin user is logged out
    When Admin user opens the login page
    And Admin user enters valid credentials
    And Admin user clicks Login button
    Then login should succeed
    And Dashboard page should load automatically
    And Dashboard title heading should be visible
    And no unexpected errors should be shown

  @admin
  Scenario: Navigation menu highlights Dashboard as active for Admin
    Given Admin is logged in
    And Admin user is on Dashboard page
    When Admin observes the left sidebar menu
    Then "Dashboard" menu item should be visually highlighted as active
    And other menu items should not be highlighted as active

  @admin
  Scenario: Dashboard summary cards are visible for Admin
    Given Admin is logged in
    And Admin user is on Dashboard page
    When Admin looks at the main content area
    Then Categories card should be displayed
    And Plants card should be displayed
    And Sales card should be displayed
    And Inventory card should be displayed
    And each card should show label and short description
    And each card should show numeric values amounts
    And no undefined null text should appear in cards

  @admin
  Scenario: Manage View buttons navigate to correct modules for Admin
    Given Admin is logged in
    And Admin Dashboard is loaded
    When Admin clicks "Manage Categories" button
    Then Categories page should open
    And the page should show correct heading
    When Admin goes back to Dashboard
    And Admin clicks "Manage Plants" button
    Then Plants page should open
    When Admin goes back to Dashboard
    And Admin clicks "Manage Sales" button
    Then Sales page should open
    And no 404 blank page should occur
    When Admin navigates back to Dashboard
    Then Dashboard should be displayed correctly

  @admin
  Scenario: Admin can see Dashboard and all menu items
    Given Admin account exists
    And Admin is logged in
    When Admin is on Dashboard
    Then Admin should see the following sidebar menu items:
      | Dashboard  |
      | Categories |
      | Plants     |
      | Sales      |
      | Logout     |
    And all listed menu items should be visible
    When Admin clicks on each menu item
    Then each menu item should open the correct page
    When Admin clicks Logout
    Then logout should end the session
    And system should return to login page
    And after logout dashboard should not be accessible without login

  # Regular User Test Cases
  @user @smoke
  Scenario: Dashboard loads immediately after User login
    Given User account exists and is active
    And User is logged out
    When User opens the login page
    And User enters valid credentials
    And User clicks Login button
    Then login should succeed
    And Dashboard page should load automatically
    And Dashboard title heading should be visible
    And no unexpected errors should be shown

  @user
  Scenario: Navigation menu highlights Dashboard as active for User
    Given User is logged in
    And User is on Dashboard page
    When User observes the left sidebar menu
    Then "Dashboard" menu item should be visually highlighted as active
    And other menu items should not be highlighted as active

  @user
  Scenario: Dashboard summary cards are visible for User
    Given User is logged in
    And User is on Dashboard page
    When User looks at the main content area
    Then Categories card should be displayed
    And Plants card should be displayed
    And Sales card should be displayed
    And Inventory card should be displayed
    And each card should show label and short description
    And each card should show numeric values amounts
    And no undefined null text should appear in cards

  @user
  Scenario: View buttons navigate to correct modules for User
    Given User is logged in
    And Dashboard is loaded
    When User clicks available dashboard buttons
    Then respective pages should open correctly
    And User should be able to navigate back to Dashboard
    And no 404 blank page should occur
    And back navigation should return to Dashboard correctly

  @user
  Scenario: User can see Dashboard and permitted menu items
    Given User account exists
    And User is logged in
    When User is on Dashboard
    Then all permitted menu items should be visible for User
    And each menu item should open the correct page
    When User clicks Logout
    Then logout should end the session
    And system should return to login page
    And after logout dashboard should not be accessible without login