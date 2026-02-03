# src/test/resources/features/ui/sales/sell_plant.feature
@ui @sales @admin
Feature: Sell Plant Functionality
  As an admin user
  I want to sell plants
  So that I can manage sales transactions
  
  Background:
    Given I am logged in as "admin"
    And I am on the sales list page
  
  @TC_UI_SALES_ADM_01 @smoke
  Scenario: Admin sells plant with sufficient stock
    Given I navigate to sell plant page
    When I select plant "Snake Plant" from dropdown
    And I enter quantity "3"
    And I click sell button
    Then I should see success message "Sale created successfully"
    And I should be redirected to sales list page
    And plant "Snake Plant" stock should be reduced by 3
  
  @TC_UI_SALES_ADM_02
  Scenario: Admin cannot sell more than available stock
    Given I navigate to sell plant page
    And there is a plant "Aloe Vera" with stock 2
    When I select plant "Aloe Vera" from dropdown
    And I enter quantity "3"
    And I click sell button
    Then I should see error message "Cannot sell more than available stock"
    And I should remain on sell plant page
  
  @TC_UI_SALES_ADM_01
  Scenario Outline: Valid quantity validations
    Given I navigate to sell plant page
    When I select plant "<plant>" from dropdown
    And I enter quantity "<quantity>"
    And I click sell button
    Then sale should be created successfully
    
    Examples:
      | plant        | quantity |
      | Snake Plant  | 1        |
      | Rose Plant   | 5        |
      | Bamboo Plant | 10       |
  
  @negative
  Scenario: Cannot sell with zero quantity
    Given I navigate to sell plant page
    When I select plant "Snake Plant" from dropdown
    And I enter quantity "0"
    And I click sell button
    Then I should see error message "Quantity must be greater than 0"
  
  @negative
  Scenario: Cannot sell with empty quantity
    Given I navigate to sell plant page
    When I select plant "Snake Plant" from dropdown
    And I enter quantity ""
    And I click sell button
    Then I should see error message "Quantity is required"