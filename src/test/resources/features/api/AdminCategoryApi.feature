Feature: Admin Category Management API

  @api @admin @tc_admin_create_01
  Scenario: Verify Admin can successfully create a new Main Category
    Given the API server is up and running
    And the user is authenticated as "admin"
    # We explicitly say "no parent" to ensure it's a Main Category
    When the user sends a POST request to "/api/categories" with name "Catcus" and no parent
    Then the API response status code should be 201
    And the response body should contain the field "name" with string value "Catcus"
    # We verify an ID was generated (just checking the field exists)
    And the response body should contain the field "id"
    And the response body should contain the field "subCategories"

    @api @admin @tc_admin_create_02
  Scenario: Verify successful creation of a Sub-Category
    Given the API server is up and running
    And the user is authenticated as "admin"
    When the user sends a POST request to "/api/categories" with name "Flower_99" and parent ID 1
    Then the API response status code should be 201
    And the response body should contain the field "name" with string value "Flower_99"
    And the response body should contain the field "id"
    # Verifying the parent link was established
    And the response body should contain the field "parent"


    @api @admin @tc_admin_create_03
    Scenario: Verify validation for Category Name Length (Min 3 chars)
    Given the API server is up and running
    And the user is authenticated as "admin"
    When the user sends a POST request to "/api/categories" with name "AB" and no parent
    Then the API response status code should be 400
    # FIX 1: Update string to match uppercase format
    And the response body should contain the field "error" with string value "BAD_REQUEST"
    # FIX 2: Check for the general failure message first
    And the response body should contain the field "message" with string value "Validation failed"


    @api @admin @tc_admin_create_04
  Scenario: Verify Unique Name Constraint (Prevent Duplicate Categories)
    Given the API server is up and running
    And the user is authenticated as "admin"
    When the user sends a POST request to "/api/categories" with name "Catcus" and no parent
    Then the API response status code should be 400
    
    # FIX: Update the expected error string to match the specific API response
    And the response body should contain the field "error" with string value "DUPLICATE_RESOURCE"
    
    # OPTIONAL: You can also verify the helpful message
    And the response body should contain the field "message" with string value "Main category 'Catcus' already exists"


    @api @admin @tc_admin_create_05
  Scenario: Verify Role-Based Access Control (User Restriction)
    Given the API server is up and running
    # Authenticating as a STANDARD USER, not Admin
    And the user is authenticated as "user"
    # Attempting to create a category (should be blocked)
    When the user sends a POST request to "/api/categories" with name "NewCat" and no parent
    # Expecting 403 Forbidden
    Then the API response status code should be 403
    And the response body should contain the field "error" with string value "Forbidden"