
@Sales @ui @user
Feature: User Sales View
  
  As a Standard User
  I want to view sales data
  So that I can see plant sales information

  Background:
    Given the User is logged into the system

  @TC_UI_SALES_USER_01 @pagination
  Scenario: Verify user can navigate through paginated sales records
    And sufficient sales records exist to trigger pagination
    Given the user is on the Sales List page
    When the Sales List page loads successfully
    Then the first set of sales records should be displayed
    And pagination controls should be visible at the bottom
    When the user clicks the "Next" page button
    Then the next set of records should be loaded
    And the "Previous" button should become active
    When the user clicks the "Previous" page button
    Then the system should navigate back to the preceding page
    And the original set of records should be displayed

  @TC_UI_SALES_USER_02 @security
  Scenario: Verify "Sell Plant" button is hidden from Regular User
    Given the user is on the Sales List page
    When the Sales List page loads successfully
    Then the "Sell Plant" button should not be visible

    @Sales @ui @user @TC_UI_SALES_USER_03
Scenario: Verify delete icon not visible to User
    Given the User is logged into the system
    Given the user is on the Sales List page 
    When the Sales List page loads successfully
    Then the sales table should be displayed
    And no delete icons should be visible in the first 5 sale records
    And no "Delete" text buttons should be visible in any row
   

   @TC_UI_SALES_USER_04 @sorting
  Scenario: Verify User can sort sales by Plant Name
    Given the user is on the Sales List page
    And multiple plants exist in sales records
    When the Sales List page loads successfully
    And the sales table should be displayed
    When the user clicks the "Plant" column header
    Then the sales should be sorted in A-Z (ascending) order by plant name
    When the user clicks the "Plant" column header again
    Then the sales should be sorted in Z-A (descending) order by plant name

    @TC_UI_SALES_USER_05 @sorting
  Scenario: Verify User can sort sales by Quantity
    Given the user is on the Sales List page
    And sales with different quantities exist
    When the Sales List page loads successfully
    And the sales table should be displayed
    When the user clicks the "Quantity" column header
    Then the sales should be sorted in ascending order by quantity
    When the user clicks the "Quantity" column header again
    Then the sales should be sorted in descending order by quantity