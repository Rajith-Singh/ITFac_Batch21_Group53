@API @Sales @Admin
Feature: Admin Sales API
  
  As an Admin user
  I want to manage sales via API
  So that I can create, retrieve, and delete plant sales programmatically

  Background:
    Given admin authentication token is available

  @TC_API_SALES_ADM_01
  Scenario Outline: Verify Admin can create a sale via API with valid data
    Given plant with ID <plant_id> exists with current stock units
    And sale record does not already exist for this transaction
    When admin sends a POST request to "/api/sales/plant/<plant_id>" with body:
      """
      {
        "quantity": <quantity>
      }
      """
    Then the response status code should be 200
    And the response body should contain valid sale creation data
    When admin retrieves the created sale by ID
    Then the sale should be retrievable with the same data
    And plant stock should be reduced by <quantity>

    Examples:
      | plant_id | quantity |
      | 1        | 1        |
      

  @TC_API_SALES_ADM_02
  Scenario Outline: Verify API prevents sale when quantity exceeds available stock
    Given plant with ID <plant_id> exists with current stock units
    And the current stock is more than 1 units
    When admin attempts to create a sale with excessive quantity
      """
      {
        "quantity": <quantity>
      }
      """
    Then the response status code should be 400
    And the response should contain error message about insufficient stock
    And no sale record should be created
    And plant stock should remain unchanged

    Examples:
      | plant_id | quantity |
      | 1        | 1000     |
      

  @TC_API_SALES_ADM_03
  Scenario Outline: Verify API validates quantity must be ≥ 1
    Given plant with ID <plant_id> exists with current stock units
    When admin attempts to create a sale with invalid quantity "<quantity>"
    Then the response status code should be 400
    And the response should contain appropriate validation error
    And no sale record should be created
    And plant stock should remain unchanged

    Examples:
      | plant_id | test_case                 | quantity |
      | 1        | Zero value                | 0        |
      | 1        | Negative value            | -5       |
      | 1        | Null value                | null     |
      | 1        | Missing quantity field    |          |
      

  @TC_API_SALES_ADM_04
  Scenario Outline: Verify Admin can retrieve specific sale by ID
    Given sale exists with ID <sale_id>
    When admin retrieves sale by ID <sale_id>
    Then the response status code should be 200
    And the response should contain a single sale object
    And the response should contain complete sale data
  
    Examples:
      | sale_id |
      | 1      |
      | 2      |
    
  
    @TC_API_SALES_ADM_05
  Scenario Outline: Verify Admin can delete sale via API
    Given sale exists with ID <sale_id>
    When admin deletes sale with ID <sale_id>
    Then the response status code should be 200 or 204
    And the sale should no longer be accessible

    Examples:
      | sale_id |
      | 61      |
      