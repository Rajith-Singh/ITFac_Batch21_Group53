Feature: Category API Management

  @api @tc_api_01
  Scenario: Verify successful retrieval of a specific category with a valid ID
    Given the API server is up and running
    And the user is authenticated as "user"
    When the user sends a GET request to "/api/categories/10"
    Then the API response status code should be 200
    And the response body should contain the field "id" with value 10
    And the response body should contain the field "name"
    And the response body should contain the field "subCategories"


    @api @tc_api_02
  Scenario: Verify system behavior when searching for a Non-Existent Category ID
    Given the API server is up and running
    And the user is authenticated as "user"
    # Using '99999' as a safe non-existent ID
    When the user sends a GET request to "/api/categories/99999" 
    Then the API response status code should be 404
    And the response body should contain the field "error" with string value "NOT_FOUND"
    And the response body should contain the field "message" with string value "Category not found: 99999"


    @api @tc_api_03
  Scenario: Verify validation for Invalid Data Type in ID parameter
    Given the API server is up and running
    And the user is authenticated as "user"
    # Sending a String ("invalid_text") where an Integer is expected
    When the user sends a GET request to "/api/categories/invalid_text"
    Then the API response status code should be 400
    And the response body should contain the field "error" with string value "Bad Request"
    # We verify the status field is present (exact value might vary, but 400 is standard)
    And the response body should contain the field "status" with value 400


    @api @tc_api_04
  Scenario: Verify Unauthorized Access protection for the endpoint
    Given the API server is up and running
    # New step to simulate an unauthenticated user
    And the user provides an invalid authentication token
    When the user sends a GET request to "/api/categories/1"
    Then the API response status code should be 401
    And the response body should contain the field "error" with string value "UNAUTHORIZED"
    And the response body should contain the field "message" with string value "Unauthorized - Use Basic Auth or JWT"


   @api @tc_api_05
  Scenario: Verify successful retrieval of a Sub-category and its Parent linkage
    Given the API server is up and running
    And the user is authenticated as "user"
    When the user sends a GET request to "/api/categories/20"
    Then the API response status code should be 200
    And the response body should contain the field "id" with value 20
    And the response body should contain the field "name" with string value "Flower_Ind"
    
    # These steps will FAIL now (as they should, because it's a bug)
    # Requirement: Expecting "parent" key with Name "Flowers"
    And the response body should contain the field "parent" with string value "Flowers"
    # Requirement: Expecting "subCategories" list to exist
    And the response body should contain the field "subCategories"


  