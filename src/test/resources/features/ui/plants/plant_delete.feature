@ui @plants
Feature: Plant Deletion

  @TC_UI_PLANT_ADM_003
  Scenario: Verify Admin can successfully delete a plant record from plant list
    Given user is logged in as admin
    And user is on the Plants List page
    And a plant "Test Delete Plant" exists in the list
    When user clicks delete button for plant "Test Delete Plant"
    And user confirms the delete action
    Then a success message "Plant deleted successfully" should be displayed
    And the deleted plant "Test Delete Plant" should no longer be visible in the plants list
