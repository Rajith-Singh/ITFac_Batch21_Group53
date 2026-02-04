# src/test/resources/features/ui/sales/delete_sale.feature
@ui @sales @admin
Feature: Delete Sale Functionality
  As an admin user
  I want to delete sales
  So that I can manage sales records
  
  Background:
    Given I am logged in as "admin"
    And I am on the sales list page
  
  @TC_UI_SALES_ADM_03 @smoke
  Scenario: Admin deletes a sale successfully
    Given there is a sale for "Snake Plant" with quantity 3
    When I click delete icon for "Snake Plant" sale
    Then I should see confirmation dialog "Are you sure you want to delete this sale?"
    When I confirm deletion
    Then I should see success message "Sale deleted successfully"
    And the sale should be removed from the list
    And plant "Snake Plant" stock should be restored by 3
  
  @TC_UI_SALES_ADM_04
  Scenario: Admin cancels sale deletion
    Given there is a sale for "Rose Plant" with quantity 5
    When I click delete icon for "Rose Plant" sale
    Then I should see confirmation dialog "Are you sure you want to delete this sale?"
    When I cancel deletion
    Then the sale should remain in the list
    And plant "Rose Plant" stock should not change
  
  @negative
  Scenario: Regular user cannot delete sales
    Given I am logged in as "user"
    And there is a sale for "Snake Plant" with quantity 3
    Then delete icon should not be visible for "Snake Plant" sale