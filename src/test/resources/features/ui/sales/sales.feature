@Sales @TC_UI_SALES_ADM_01 @ui
Feature: Sales Management
  As an Admin
  I want to manage plant sales
  So that I can track inventory and transactions

  Scenario: Verify Admin can sell a plant with sufficient stock quantity
    Given the Admin user is logged in 
    And the user is on the Sales List page
    And at least one plant exists with quantity 1 or more
    When the Admin clicks on the "Sell Plant" button
    Then the system navigates to the "/ui/sales/new" page
    When the Admin selects "Snake Plant (Stock: 13)" from the Plant dropdown
    And the Admin enters "3" in the Quantity field
    And the Admin clicks the Sell button
    Then the sale should complete successfully
    And the success message "Sale created successfully" should be displayed
    And the user should be redirected to the "/ui/sales" page


  @Sales @TC_UI_SALES_ADM_02 @ui @negative
  Scenario: Verify error occurs when quantity exceeds available stock
    Given the Admin user is logged in
    And the user is on the Sales List page
    And a plant "Peace Lily" exists with quantity "5" in stock
    When the Admin clicks on the "Sell Plant" button
    Then the system navigates to the "/ui/sales/new" page
    When the Admin selects "Peace Lily (Stock: 5)" from the Plant dropdown
    And the Admin enters "6" in the Quantity field
    And the Admin clicks the Sell button
    Then the system should show an error message "Peace Lily has only 5 items available in stock"
    And the user should remain on the same page

    @Sales @TC_UI_SALES_ADM_03 @ui
  Scenario: Verify Admin can delete sale
    Given the Admin user is logged in
    And the user is on the Sales List page
    And at least one sale record exists
    When the Admin locates a sale record in the list
    Then the delete icon should be visible for the sale
    When the Admin clicks the delete icon for the sale
    Then a JavaScript confirmation alert appears with message "Are you sure you want to delete this sale?"
    When the Admin accepts the confirmation alert
    Then the sale should be deleted successfully
    And the success message "Sale deleted successfully" should be displayed

      @Sales @TC_UI_SALES_ADM_04 @ui @sorting
  Scenario: Verify sales data defaults to newest-first order
    Given the Admin user is logged in
    And the user is on the Sales List page
    And multiple sales exist with different dates and times
    When the Sales List page loads successfully
    Then the sales records should be sorted by Sold Date in descending order (newest first)
    And the first record should have the most recent date and time
    And the second record should have the next most recent date and time  
    And the third record should have an older date and time than the first record
    When the page is refreshed
    Then the sort order should remain consistent


      @Sales @TC_UI_SALES_ADM_05 @ui @empty-state
  Scenario: Verify empty state message when no sales exist
    Given the Admin user is logged in
    And all existing sales records have been deleted from the database
    When the user navigates to the Sales List page
    Then the page should load without errors
    And the message "No sales found" should be displayed prominently
    And no table rows with sales data should be displayed
    And the table should show only the empty state row with "No sales found"
    And the "Sell Plant" button should still be visible to Admin