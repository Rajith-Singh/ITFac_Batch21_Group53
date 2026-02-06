@ui @plants
Feature: Plant Category Selection
  As an admin
  I want to see only sub-categories in the plant category dropdown
  So that I can classify plants correctly

  @TC_UI_PLANT_ADM_004
  Scenario: Verify Category selection is a sub-category
    Given user is logged in as admin
    And user is on the Plants List page
    When user clicks on "Add a Plant" button
    Then user should be navigated to "Add Plants" page
    And the category dropdown should contains sub-categories "Rose, Lotus, Banana, Mango, Rose UK"
    And the category dropdown should not contain main categories "Flower, Fruits, Herbs"
