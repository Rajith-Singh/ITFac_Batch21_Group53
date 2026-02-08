Feature: Plant List Management

  @TC_UI_PLANT_USER_007 @ui
  Scenario Outline: Verify plant search by name
    Given I am logged in as "user"
    And user clicks on "Plants" button
    When user enters "<SearchTerm>" in the search field
    And user clicks on "Search" button
    Then the plant list should display plants matching "<SearchTerm>"

    Examples:
      | SearchTerm  |
      | Rose        |
      | Yellow Rose |

  @TC_UI_PLANT_USER_008 @ui
  Scenario: Verify "Low" badge for plants with low stock
    Given user is logged in as admin
    When user clicks on "Plants" button
    Then user should be redirected to "/ui/plants" page
    And I should see a plant with quantity less than 5 displaying a "Low" badge
    And I should see a plant with quantity 5 or more not displaying a "Low" badge

  @TC_UI_PLANT_USER_008_USER @ui
  Scenario: Verify "Low" badge for plants with low stock (User)
    Given I am logged in as "user"
    When user clicks on "Plants" button
    Then user should be redirected to "/ui/plants" page
    And I should see a plant with quantity less than 5 displaying a "Low" badge
    And I should see a plant with quantity 5 or more not displaying a "Low" badge

  @TC_UI_PLANT_USER_009 @ui
  Scenario Outline: Verify plant list sorting properties
    Given I am logged in as "user"
    And user clicks on "Plants" button
    When user sorts the list by "<Column>"
    Then the list should be sorted by "<Column>" in "<Order>" order

    Examples:
      | Column   | Order      |
      | Name     | Ascending  |
      | Price    | Ascending  |
      | Quantity | Ascending  |
      | Name     | Descending |
      | Price    | Descending |
      | Quantity | Descending |

  @TC_UI_PLANT_USER_010 @ui
  Scenario: Verify filter plant by category
    Given I am logged in as "user"
    And user clicks on "Plants" button
    When user selects "Mango" from category dropdown
    And user clicks on "Search" button
    Then only plants with category "Mango" should be displayed

  @TC_UI_PLANT_ADM_005 @ui
  Scenario: Verify that admin can successfully edit existing plant data with valid input data
    Given user is logged in as admin
    And user clicks on "Plants" button
    When admin clicks on edit icon for plant "Rose UK"
    And user selects sub category as "Rose"
    And user enters price as "3000"
    And user enters quantity as "20"
    And user clicks on "save" button
    Then a success message "Plant updated successfully" should be displayed
    And plant "Rose UK" should be updated with category "Rose", price "3000", and quantity "20"
