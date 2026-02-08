@api
Feature: Plant API - Get By ID
  As a User
  I want to get a plant by its ID
  So that I can see the details of a specific plant

  Background:
    Given I have a valid user token

  Scenario: TC_API_PLANTS_USER_016 - Verify Get Plant By ID
    When I send a GET request to get plant with ID 17
    Then the response status code should be 200
    And the plant ID in response should be 17
    And the plant name should not be null
    And the plant price should be greater than 0
    And the plant quantity should be greater than or equal to 0
    And the plant categoryId should be greater than or equal to 0
