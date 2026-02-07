@api
Feature: Plant API - Get By Category
  As a User
  I want to get plants by their category
  So that I can see all plants belonging to a specific category

  Background:
    Given I have a valid user token

  Scenario: TC_API_PLANTS_USER_013 - Verify Get Plants By Category ID
    When I send a GET request to get plants by category ID 5
    Then the response status code should be 200
    And the response body should not be empty
    And all plants in the response should belong to category ID 5
    And each plant should have valid ID, name, price, and quantity
