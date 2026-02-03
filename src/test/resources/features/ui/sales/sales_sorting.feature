# src/test/resources/features/ui/sales/sales_sorting.feature
@ui @sales
Feature: Sales List Sorting
  As a user
  I want to view sales sorted properly
  So that I can easily find recent transactions
  
  Background:
    Given I am logged in as "admin"
    And there are multiple sales with different dates
  
  @TC_UI_SALES_ADM_05 @smoke
  Scenario: Sales are sorted by sold date descending by default
    When I navigate to sales list page
    Then sales should be sorted by "Sold Date" in descending order
    And the first sale should be the most recent
    And sort indicator should show "▼" for Sold Date column
  
  Scenario: Sort by plant name ascending
    When I click on "Plant Name" column header
    Then sales should be sorted by "Plant Name" in ascending order
    And sort indicator should show "▲" for Plant Name column
  
  Scenario: Sort by plant name descending
    When I click on "Plant Name" column header twice
    Then sales should be sorted by "Plant Name" in descending order
    And sort indicator should show "▼" for Plant Name column
  
  Scenario: Sort by quantity
    When I click on "Quantity" column header
    Then sales should be sorted by "Quantity" in ascending order
  
  Scenario: Sort by total price
    When I click on "Total Price" column header
    Then sales should be sorted by "Total Price" in ascending order
  
  Scenario: Maintain sort after refresh
    Given sales are sorted by "Sold Date" descending
    When I refresh the page
    Then sales should remain sorted by "Sold Date" descending
  
  @negative
  Scenario: Empty sales list message
    Given there are no sales
    When I navigate to sales list page
    Then I should see message "No sales found"