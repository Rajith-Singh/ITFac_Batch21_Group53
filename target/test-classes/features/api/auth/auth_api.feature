@TC_API_AUTH_01 @api @auth @login
Feature: Authentication API
  As a user of the plant management system
  I want to authenticate via API
  So that I can access protected endpoints

  @TC_API_AUTH_ADMIN_LOGIN @api @auth @admin
  Scenario: Admin can login with valid credentials
    Given I have admin credentials
    When I send a POST request to "/api/auth/login" with the admin credentials
    Then the response status code should be 200
    And the response should contain an authentication token
    And the token should not be null or empty

  @TC_API_AUTH_USER_LOGIN @api @auth @user
  Scenario: User can login with valid credentials
    Given I have user credentials
    When I send a POST request to "/api/auth/login" with the user credentials
    Then the response status code should be 200
    And the response should contain an authentication token
    And the token should not be null or empty

  @TC_API_AUTH_INVALID_CREDENTIALS @api @auth @validation
  Scenario: Login fails with invalid credentials
    Given I have invalid login credentials
    When I send a POST request to "/api/auth/login" with the invalid credentials
    Then the response status code should be 401
    And the response should not contain an authentication token
    And the error message should indicate authentication failed

  @TC_API_AUTH_MISSING_FIELDS @api @auth @validation
  Scenario Outline: Login fails with missing required fields
    Given I have incomplete login credentials with missing "<missingField>"
    When I send a POST request to "/api/auth/login" with the incomplete credentials
    Then the response status code should be 400
    And the error message should indicate that "<missingField>" is required

    Examples:
      | missingField |
      | username     |
      | password     |

  @TC_API_AUTH_TOKEN_VALIDATION @api @auth @validation
  Scenario: API endpoints validate authentication tokens
    Given I have a valid admin authentication token
    When I access a protected endpoint with the valid token
    Then the response status code should indicate success or appropriate business logic response
    When I access a protected endpoint with an invalid token
    Then the response status code should be 401
    And the error message should indicate that the token is invalid or expired
    When I access a protected endpoint without any token
    Then the response status code should be 401
    And the error message should indicate that authentication is required