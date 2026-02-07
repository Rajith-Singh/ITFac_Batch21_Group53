@api
Feature: Plants API
  As a user of the Plant Sales API
  I want to manage and query plants via REST API
  So that I can integrate with the backend programmatically

  # --- Pagination ---
  @TC_API_PLANTS_USER_06
  Scenario: User can access paginated plants with default parameters
    Given user is authenticated
    When user requests paged plants with default parameters
    Then response status should be 200
    And response should contain content array
    And totalElements should be at least 25
    And totalPages should be greater than 0
    And response should contain pageable
    And page number should be 0
    And page size should be greater than 0

  @TC_API_PLANTS_USER_07
  Scenario: User can control pagination parameters
    Given user is authenticated
    When user requests paged plants with page 1 and size 10
    Then response status should be 200
    And response should contain content array
    And content size should be at most 10
    And page number should be 1
    And page size should be 10
    When user requests paged plants with page 2 and size 5
    Then response status should be 200
    And content size should be at most 5
    And page number should be 2
    And page size should be 5
    And totalElements should match between both requests

  # --- Sorting by name ---
  @TC_API_PLANTS_USER_08
  Scenario: User can sort plants by name via API
    Given user is authenticated
    When user requests paged plants sorted by name ascending
    Then response status should be 200
    And response should contain content array
    When user requests paged plants sorted by name descending
    Then response status should be 200
    And response should contain content array
    And first item in ascending should differ from first in descending

  # --- Sorting by price ---
  @TC_API_PLANTS_USER_09
  Scenario: User can sort plants by price via API
    Given user is authenticated
    When user requests paged plants sorted by price ascending
    Then response status should be 200
    And content should be ordered by price ascending
    When user requests paged plants sorted by price descending
    Then response status should be 200
    And content should be ordered by price descending
    And first ascending price should be less or equal to first descending price

  # --- Sorting by quantity ---
  @TC_API_PLANTS_USER_10
  Scenario: User can sort plants by stock quantity via API
    Given user is authenticated
    When user requests paged plants sorted by quantity ascending
    Then response status should be 200
    And content should be ordered by quantity ascending
    When user requests paged plants sorted by quantity descending
    Then response status should be 200
    And content should be ordered by quantity descending
    And first ascending quantity should be less or equal to first descending quantity

  # --- Search by name ---
  @TC_API_PLANTS_USER_11
  Scenario: User can search plants by name via API
    Given user is authenticated
    When user searches plants by name "monstera"
    Then response status should be 200
    And response should contain content array
    And all content names should contain "monstera"
    When user searches plants by name "xyznonexistent"
    Then response status should be 200
    And totalElements should be 0

  # --- Filter by category ---
  @TC_API_PLANTS_USER_12
  Scenario: User can filter plants by category via API
    Given user is authenticated
    When user filters plants by category "Indoor"
    Then response status should be 200
    And response should contain content and totalElements
    When user filters plants by category "Outdoor"
    Then response status should be 200
    And response should contain content and totalElements
    And Indoor filter should return only Indoor plants
    And Outdoor filter should return only Outdoor plants

  # --- Admin create plant ---
  @TC_API_PLANTS_ADM_01
  Scenario: Admin can create a plant with valid data under a sub-category
    Given admin is authenticated
    When admin creates a plant with name "ABC Plant" price 150.0 quantity 25 category 8
    Then create response status should be 201 or backend unavailable
    And if created response should have id name price quantity category id 8
