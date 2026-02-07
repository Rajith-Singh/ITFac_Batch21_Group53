@api
Feature: Plant API - Summary
  As a User
  I want to see the plant summary
  So that I can see high-level statistics of plants

  Background:
    Given I have a valid user token

  Scenario: TC_API_PLANTS_USER_014 - Verify User Can Access Plant Summary
    When I send a GET request to get plant summary
    Then the response status code should be 200
    And the summary should contain totalPlants and lowStockPlants
    And totalPlants and lowStockPlants should be greater than or equal to 0
    And the response should not contain admin fields "adminData" or "internalStats"
