Feature: Category Management
  As an Admin
  I want to manage plant categories
  So that I can organize the inventory

  # --- BACKGROUND: This runs before EVERY Scenario automatically ---
  Background: Admin is logged in
    Given the user is on the login page
    When the user logs in with valid credentials

  # Test Case: TC_UI_ADMIN_CAT_01
  @tc01 @smoke
  Scenario: Verify Admin can successfully add a new Main Category
    # Login steps removed here because Background handles them!
    Then the user is on the Category Management page
    When the user clicks the "Add A Category" button
    And the user enters "Lotus" in the Category Name field
    And the user leaves the Parent Category empty
    And the user clicks the Save button
    Then a success message should be displayed
    And the user should be redirected to the Category List
    And the new category "Lotus" should appear in the list

  # Test Case: TC_UI_ADMIN_CAT_02
  @tc02 @regression
  Scenario: Verify Admin is prevented from creating a category with an empty Name
    # Background logs us in, so we are ready to open the page
    Given the user opens the Category Management page
    When the user clicks the "Add A Category" button
    And the user leaves the Category Name field empty
    And the user leaves the Parent Category empty
    And the user clicks the Save button
    Then an error message should be displayed indicating the name is required
    And the user should remain on the Add Category page

    # Test Case: TC_UI_ADMIN_CAT_03
  @tc03 @regression
  Scenario: Verify Admin can successfully create a new Sub-Category
    # Background logs us in automatically
    Given the user opens the Category Management page
    When the user clicks the "Add A Category" button
    And the user enters "Lotus 001" in the Category Name field
    # New Step: Selecting a parent
    And the user selects "Lotus" from the Parent Category dropdown
    And the user clicks the Save button
    Then a success message should be displayed
    And the user should be redirected to the Category List
    And the new category "Lotus 001" should appear in the list
    # Optional: Verify it shows the correct parent in the table
    And the category "Lotus 001" should show "Lotus" as its parent

# Test Case: TC_UI_ADMIN_CAT_04
  @tc04 @regression
  Scenario: Verify System filters category list for multi-word names
    Given the user opens the Category Management page
    # Step 1: Create the category first
    When the user clicks the "Add A Category" button
    And the user enters "Rose SL" in the Category Name field
    And the user clicks the Save button
    Then a success message should be displayed
    
    # --- CRITICAL NEW STEP ---
    # This forces the test to WAIT and confirm "Rose SL" is in the list
    # BEFORE we try to search for it.
    And the new category "Rose SL" should appear in the list
    
    # Step 2: Perform the Search
    When the user enters "Rose SL" in the search box
    And the user clicks the Search button
    Then the category list should only display the "Rose SL" record

    # Test Case: TC_UI_ADMIN_CAT_05
  @tc05 @regression
  Scenario: Verify system prevents renaming a Sub-category to an existing name
    Given the user opens the Category Management page
    
    # --- SETUP: Create Parent Category ---
    When the user clicks the "Add A Category" button
    And the user enters "Coconut" in the Category Name field
    And the user clicks the Save button
    
    # --- SETUP: Create First Sub-Category (Cactus A) ---
    When the user clicks the "Add A Category" button
    And the user enters "Coconut_A" in the Category Name field
    And the user selects "Coconut" from the Parent Category dropdown
    And the user clicks the Save button
    
    # --- SETUP: Create Second Sub-Category (Cactus B) ---
    When the user clicks the "Add A Category" button
    And the user enters "Coconut_B" in the Category Name field
    And the user selects "Coconut" from the Parent Category dropdown
    And the user clicks the Save button
    
    # --- ACTUAL TEST: Rename "Coconut_A" to "Coconut_B" ---
    # We search for "Coconut_A" first to make sure it's the first row in the table
    When the user enters "Coconut_A" in the search box
    And the user clicks the Search button
    And the user clicks the Edit button for the searched category
    
    # Try to rename it to "Coconut_B" (which already exists)
    And the user enters "Coconut_B" in the Category Name field
    And the user clicks the Save button
    
    # --- VALIDATION (This is expected to FAIL due to backend error msg) ---
    Then a user-friendly error message "Category name already exists" should be displayed