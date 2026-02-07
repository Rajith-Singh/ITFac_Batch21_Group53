Feature: Category Management
  As an Admin
  I want to manage plant categories
  So that I can organize the inventory

  # Test Case: TC_UI_ADMIN_CAT_01
  Scenario: Verify Admin can successfully add a new Main Category
    Given the user is on the login page
    When the user logs in with valid credentials
    Then the user is on the Category Management page
    When the user clicks the "Add A Category" button
    And the user enters "Lotus_new2" in the Category Name field
    And the user leaves the Parent Category empty
    And the user clicks the Save button
    Then a success message should be displayed
    And the user should be redirected to the Category List
    And the new category "Lotus_new2" should appear in the list

