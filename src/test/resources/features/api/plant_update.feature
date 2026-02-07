@api
Feature: Plant API - Update Plant
  As an Admin
  I want to update an existing plant
  So that I can keep plant information up-to-date

  Background:
    Given I have a valid user token

  Scenario: TC_API_PLANTS_ADM_011 - Verify Plant Updated Successfully
    When I send a PUT request to update plant with ID 0 and name "Anthurium"
    Then the response status code should be 200
    And the updated plant ID should be 0
    And the updated plant name should be "Anthurium"
    And the updated plant price should be 150
    And the updated plant quantity should be 25
    And the updated plant category ID should be 0
    And the updated plant category name should be "Anthurium"
