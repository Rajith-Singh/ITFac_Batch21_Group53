@ui @plants @TC_UI_PLANT_ADM_002
Feature: Plant form validation
  Verify required field validation when adding a plant

  Scenario: Verify that Plant Name, Sub category, Price, Quantity are mandatory
    Given user is logged in as admin
    And user is on the Plants List page
    When user clicks on "Add a Plant" button
    And user leaves Plant Name, Sub category, Price and Quantity empty
    And user clicks on "save" button
    Then validation message "Plant Name is required" should be displayed for field "name"
    And validation message "Category is required" should be displayed for field "categoryId"
    And validation message "Price is required" should be displayed for field "price"
    And validation message "Quantity is required" should be displayed for field "quantity"
