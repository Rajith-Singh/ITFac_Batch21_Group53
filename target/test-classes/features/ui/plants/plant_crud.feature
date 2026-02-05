@ui @plants
Feature: Plant CRUD Operations
  As an admin user
  I want to manage plants in the system
  So that I can add, edit, and delete plants

  Background:
    Given user is logged in as admin
    And user is on the Plants List page

  @TC_UI_PLANT_ADM_001
  Scenario: Verify Admin can successfully add a new plant with valid data
    When user clicks on "Add a Plant" button
    Then user should be navigated to "Add Plants" page
    When user enters plant name as "Red Rose"
    And user selects sub category as "Rose UK"
    And user enters price as "2000"
    And user enters quantity as "12"
    And user clicks on "save" button
    Then a success message "Plant added successfully" should be displayed
    And user should be redirected to "/ui/plants" page
    And newly added plant "Red Rose" should be displayed in the plants list
