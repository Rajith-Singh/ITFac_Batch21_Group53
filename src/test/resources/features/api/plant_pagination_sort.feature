@api
Feature: Plant API - Pagination and Sorting
  As a User
  I want to get paginated and sorted plants
  So that I can browse plants efficiently

  Background:
    Given I have a valid user token

  Scenario Outline: TC_API_PLANTS_ADM_015 - Verify Pagination and Sorting for All Fields
    When I send a GET request for plants with page 0, size 10, and sort by "<field>" in "<order>" order
    Then the response status code should be 200
    And the response should be sorted by "<field>" in "<order>" order

    Examples:
      | field    | order |
      | name     | asc   |
      | name     | desc  |
      | price    | asc   |
      | price    | desc  |
      | quantity | asc   |
      | quantity | desc  |
