Feature: Category Management (Standard User)
  As a Standard User
  I want to view categories
  So that I can see product organization
  But I should NOT be able to edit or delete them

  # --- BACKGROUND: Runs before every scenario in THIS file ---
  Background: Standard User is logged in
    Given the user is on the login page
    # This uses the NEW step definition for Standard User
    When the standard user logs in

  # Test Case: TC_UI_USER_CAT_01
  @tc_user_01 @regression
  Scenario: Verify Edit Category functionality is restricted for non-admin users
    Given the user opens the Category Management page
    
    # 1. UI Check: We expect the column to be visible (Test Blocked Prevention)
    Then the "Actions" column should be displayed
    
    # 2. Security Check: Even if visible, the user tries to click "Edit"
    When the user clicks the Edit button for any category
    
    # 3. Critical Validation: The system MUST still block access to the Edit Page
    # (The test fails if the user successfully reaches the Edit form)
    Then the user should not be navigated to the Edit Category page

  # Test Case: TC_UI_USER_CAT_02
  @tc_user_02 @regression
  Scenario: Verify non-admin User can successfully search for an existing category
    Given the user opens the Category Management page
    
    # 1. Validation: Verify the search bar is ready for the user
    Then the Search input field should be enabled
    
    # 2. Action: Perform the search (Re-using existing logic!)
    When the user enters "Lotus" in the search box
    And the user clicks the Search button
    
    # 3. Validation: Verify results (Re-using existing logic!)
    Then the category list should only display the "Lotus" record

    
    # Test Case: TC_UI_USER_CAT_03
  @tc_user_03 @regression
  Scenario: Verify non-admin User can filter the category list by selecting a Parent Category
    Given the user opens the Category Management page
    
    # 1. Validation: Verify the Parent Category dropdown is enabled
    Then the Parent Category search dropdown should be enabled
    
    # 2. Action: Select "Coconut" from the filter dropdown
    When the user selects "Coconut" from the search parent dropdown
    And the user clicks the Search button
    
    # 3. Validation: Verify the list is filtered
    # (We expect to see "Coconut" or its children in the result)
    Then the category list should display the "Coconut" record


    # Test Case: TC_UI_USER_CAT_04
  @tc_user_04 @regression
  Scenario: Verify correct feedback when User searches for a non-existent category
    Given the user opens the Category Management page
    
    # 1. Action: Enter invalid data
    When the user enters "XYZ_Invalid" in the search box
    And the user clicks the Search button
    
    # 2. Validation: The list should refresh and show no results message
    Then the message "No category found" is displayed clearly in the table area

    # Test Case: TC_UI_USER_CAT_05
  @tc_user_05 @regression
  Scenario: Verify non-admin User can sort by ID, Name, and Parent sequentially
    Given the user opens the Category Management page
    
    # 1. Check ID Sort (Numeric)
    When the user clicks the "ID" column header on the category list
    Then the category list should be sorted by "ID" in ascending order
    
    # 2. Check Name Sort (Alphabetical)
    When the user clicks the "Category Name" column header on the category list
    Then the category list should be sorted by "Category Name" in ascending order
    
    # 3. Check Parent Sort (Alphabetical)
    When the user clicks the "Parent Category" column header on the category list
    Then the category list should be sorted by "Parent Category" in ascending order