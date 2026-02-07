Feature: Plant API Management
  As a user (Standard or Admin)
  I want to interact with the /api/plants endpoints
  So that I can manage or view plant inventory based on my permissions

  @api @user @tc_api_plant_user_022
  Scenario: Verify User cannot delete plants via API
    Given the API server is up and running
    # Authenticating as a STANDARD USER (who should NOT have delete permissions)
    And the user is authenticated as "user"
    # Attempting to delete a plant (assuming ID 8 exists or even if it doesn't, permission check hits first)
    When the user sends a DELETE request to "/api/plants/4"
    # Expecting 403 Forbidden
    Then the API response status code should be 403
    And the response body should contain the field "error" with string value "Forbidden"


    @api @user @tc_api_plant_user_023
  Scenario Outline: Verify API handles invalid pagination parameters gracefully
    Given the API server is up and running
    And the user is authenticated as "user"
    When the user sends a GET request to "/api/plants/paged" with page <page> and size <size>
    # The requirement states it should return 400 Bad Request for invalid values
    Then the API response status code should be <expectedStatus>

    Examples:
      | page | size | expectedStatus |
      | -1   | 10   | 400            |
      | 0    | 0    | 400            |
      # Optional: Test strictly large size if your API limits it (e.g., max 100)
      # | 0    | 1000 | 400            |




    @api @user @tc_api_plant_user_024
  Scenario Outline: Verify API handles invalid sort parameters gracefully
    Given the API server is up and running
    And the user is authenticated as "user"
    When the user sends a GET request to "/api/plants/paged" with sort "<sortParam>"
    # Expecting 400 Bad Request per your provided schema image
    Then the API response status code should be <expectedStatus>

    Examples:
      | sortParam             | expectedStatus |
      | invalidField,asc      | 400            |
      | name,invalidDirection | 400            |



      @api @user @tc_api_plant_user_025
  Scenario Outline: Verify API requires authentication for protected endpoints
    Given the API server is up and running
    # Note: We intentionally skip the "user is authenticated" step here
    When the user sends a GET request to "<endpoint>" without authentication
    Then the API response status code should be 401
    And the response body should contain the field "error"
    And the response body should contain the field "status"

    Examples:
      | endpoint      |
      | /api/plants   |
      | /api/plants/5 |