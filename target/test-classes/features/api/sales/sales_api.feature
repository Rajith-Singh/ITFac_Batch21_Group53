@TC_API_SALES_01 @api @sales
Feature: Sales API
  As a user of the plant management system
  I want to manage sales via API
  So that I can track and analyze sales data

  @TC_API_SALES_USER_01 @api @sales @user @create
  Scenario: User can create a sale with valid data
    Given I have a user authentication token
    And I have a plant available for sale
    When I create a new sale with the following details:
      | plantId | quantity | saleDate   |
      | 1       | 3        | 2024-01-15 |
    Then the response status code should be 201
    And the response should contain the created sale details
    And the plant stock should be reduced by the sold quantity

  @TC_API_SALES_USER_02 @api @sales @user @validation
  Scenario: Sales validation for required fields
    Given I have a user authentication token
    
    # Test missing plantId
    When I create a sale with the following details without plantId:
      | quantity | saleDate   |
      | 3        | 2024-01-15 |
    Then the response status code should be 400
    And the error message should indicate that plantId is required
    
    # Test missing quantity
    When I create a sale with the following details without quantity:
      | plantId | saleDate   |
      | 1       | 2024-01-15 |
    Then the response status code should be 400
    And the error message should indicate that quantity is required
    
    # Test missing saleDate
    When I create a sale with the following details without saleDate:
      | plantId | quantity |
      | 1       | 3        |
    Then the response status code should be 400
    And the error message should indicate that saleDate is required

  @TC_API_SALES_USER_03 @api @sales @user @validation
  Scenario: Sales validation for business rules
    Given I have a user authentication token
    And I have a plant with available stock of "10" units
    
    # Test negative quantity
    When I create a sale with quantity "-5" for the plant
    Then the response status code should be 400
    And the error message should indicate that quantity must be positive
    
    # Test zero quantity
    When I create a sale with quantity "0" for the plant
    Then the response status code should be 400
    And the error message should indicate that quantity must be greater than zero
    
    # Test quantity exceeding stock
    When I create a sale with quantity "15" for the plant
    Then the response status code should be 400
    And the error message should indicate that quantity exceeds available stock

  @TC_API_SALES_USER_04 @api @sales @user @retrieval
  Scenario: User can retrieve sales list
    Given I have a user authentication token
    And there are existing sales in the system
    When I request the sales list
    Then the response status code should be 200
    And the response should contain a list of sales
    And each sale should contain plant, quantity, and saleDate information

  @TC_API_SALES_USER_05 @api @sales @user @pagination
  Scenario: User can paginate sales results
    Given I have a user authentication token
    And there are multiple sales in the system
    When I request sales with page "0" and size "10"
    Then the response status code should be 200
    And the response should contain paginated sales data
    And the content size should be less than or equal to 10
    And the sales page number should be 0
    And the sales page size should be 10

  @TC_API_SALES_USER_06 @api @sales @user @sorting
  Scenario: User can sort sales by date
    Given I have a user authentication token
    And there are sales with different dates
    When I request sales sorted by "saleDate" in "asc" order
    Then the response status code should be 200
    And the sales should be ordered by date in ascending order
    When I request sales sorted by "saleDate" in "desc" order
    Then the response status code should be 200
    And the sales should be ordered by date in descending order

  @TC_API_SALES_USER_07 @api @sales @user @sorting
  Scenario: User can sort sales by total price
    Given I have a user authentication token
    And there are sales with different total prices
    When I request sales sorted by "totalPrice" in "asc" order
    Then the response status code should be 200
    And the sales should be ordered by total price in ascending order
    When I request sales sorted by "totalPrice" in "desc" order
    Then the response status code should be 200
    And the sales should be ordered by total price in descending order

  @TC_API_SALES_USER_08 @api @sales @user @filtering
  Scenario: User can filter sales by date range
    Given I have a user authentication token
    And there are sales from different time periods
    When I request sales filtered by date range from "2024-01-01" to "2024-01-31"
    Then the response status code should be 200
    And the response should contain sales within the specified date range
    And all sale dates should be between the start and end dates inclusive

  @TC_API_SALES_USER_09 @api @sales @user @filtering
  Scenario: User can filter sales by plant
    Given I have a user authentication token
    And there are sales for different plants
    When I request sales filtered by plant name "Monstera Deliciosa"
    Then the response status code should be 200
    And the response should contain sales for the specified plant only
    And all returned sales should be for "Monstera Deliciosa"

  @TC_API_SALES_ADMIN_01 @api @sales @admin
  Scenario: Admin can access all sales regardless of user
    Given I have an admin authentication token
    And there are sales created by different users
    When I request the complete sales list
    Then the response status code should be 200
    And the response should contain all sales in the system
    And the sales count should include sales from all users