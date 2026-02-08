@api
Feature: Plant API - Delete Plant
  As an Admin
  I want to delete a plant
  So that I can remove plants from the system

  Background:
    Given I have a valid user token

  Scenario: TC_API_PLANTS_ADM_012 - Verify Delete Non-Existing Plant
    When I send a DELETE request for a non-existing plant with ID 99999
    Then the response status code should be 404
    And the response body should contain message "Plant not found"
    And the response status should be 404
    And the response error should not be null
