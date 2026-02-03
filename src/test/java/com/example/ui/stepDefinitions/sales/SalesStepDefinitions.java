// src/test/java/com/example/ui/stepDefinitions/sales/SalesStepDefinitions.java
package com.example.ui.stepDefinitions.sales;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;
import com.example.ui.pages.sales.SalesListPage;
import com.example.ui.pages.sales.SellPlantPage;
import com.example.ui.pages.plants.PlantListPage;

public class SalesStepDefinitions {

    private SalesListPage salesListPage;
    private SellPlantPage sellPlantPage;
    private PlantListPage plantListPage;

    // Background steps
    @Given("I am on the sales list page")
    public void i_am_on_the_sales_list_page() {
        salesListPage = new SalesListPage(DriverManager.getDriver());
        salesListPage.navigateTo();
        Assert.assertTrue(salesListPage.isPageLoaded());
    }

    @Given("I navigate to sell plant page")
    public void i_navigate_to_sell_plant_page() {
        salesListPage.clickSellButton();
        sellPlantPage = new SellPlantPage(DriverManager.getDriver());
        Assert.assertTrue(sellPlantPage.isPageLoaded());
    }

    // TC_UI_SALES_ADM_01 steps
    @When("I select plant {string} from dropdown")
    public void i_select_plant_from_dropdown(String plantName) {
        sellPlantPage.selectPlant(plantName);
    }

    @When("I enter quantity {string}")
    public void i_enter_quantity(String quantity) {
        sellPlantPage.enterQuantity(quantity);
    }

    @When("I click sell button")
    public void i_click_sell_button() {
        sellPlantPage.clickSellButton();
    }

    @Then("I should see success message {string}")
    public void i_should_see_success_message(String expectedMessage) {
        String actualMessage = sellPlantPage.getSuccessMessage();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Then("I should be redirected to sales list page")
    public void i_should_be_redirected_to_sales_list_page() {
        Assert.assertTrue(salesListPage.isPageLoaded());
    }

    @Then("plant {string} stock should be reduced by {int}")
    public void plant_stock_should_be_reduced_by(String plantName, int quantity) {
        plantListPage = new PlantListPage(DriverManager.getDriver());
        plantListPage.navigateTo();
        int currentStock = plantListPage.getPlantStock(plantName);
        // Compare with previous stock (you'll need to store previous stock)
        Assert.assertEquals(currentStock, previousStock - quantity);
    }

    // TC_UI_SALES_ADM_02 steps
    @Given("there is a plant {string} with stock {int}")
    public void there_is_a_plant_with_stock(String plantName, int stock) {
        // Setup test data - create plant with specific stock
        // This could be via API or database setup
    }

    @Then("I should see error message {string}")
    public void i_should_see_error_message(String expectedError) {
        String actualError = sellPlantPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError);
    }

    @Then("I should remain on sell plant page")
    public void i_should_remain_on_sell_plant_page() {
        Assert.assertTrue(sellPlantPage.isPageLoaded());
    }

    // TC_UI_SALES_ADM_03, 04 steps
    @Given("there is a sale for {string} with quantity {int}")
    public void there_is_a_sale_for_with_quantity(String plantName, int quantity) {
        // Create a sale via API or UI for testing
    }

    @When("I click delete icon for {string} sale")
    public void i_click_delete_icon_for_sale(String plantName) {
        salesListPage.clickDeleteIcon(plantName);
    }

    @Then("I should see confirmation dialog {string}")
    public void i_should_see_confirmation_dialog(String expectedMessage) {
        String dialogMessage = salesListPage.getConfirmationDialogMessage();
        Assert.assertEquals(dialogMessage, expectedMessage);
    }

    @When("I confirm deletion")
    public void i_confirm_deletion() {
        salesListPage.confirmDeletion();
    }

    @When("I cancel deletion")
    public void i_cancel_deletion() {
        salesListPage.cancelDeletion();
    }

    @Then("the sale should be removed from the list")
    public void the_sale_should_be_removed_from_the_list() {
        Assert.assertFalse(salesListPage.isSalePresent("Snake Plant"));
    }

    @Then("plant {string} stock should be restored by {int}")
    public void plant_stock_should_be_restored_by(String plantName, int quantity) {
        plantListPage = new PlantListPage(DriverManager.getDriver());
        plantListPage.navigateTo();
        int currentStock = plantListPage.getPlantStock(plantName);
        // Compare with stock after sale
        Assert.assertEquals(currentStock, stockAfterSale + quantity);
    }

    @Then("the sale should remain in the list")
    public void the_sale_should_remain_in_the_list() {
        Assert.assertTrue(salesListPage.isSalePresent("Rose Plant"));
    }

    @Then("delete icon should not be visible for {string} sale")
    public void delete_icon_should_not_be_visible_for_sale(String plantName) {
        Assert.assertFalse(salesListPage.isDeleteIconVisible(plantName));
    }

    // TC_UI_SALES_ADM_05 steps
    @Given("there are multiple sales with different dates")
    public void there_are_multiple_sales_with_different_dates() {
        // Create test sales with different dates
    }

    @Then("sales should be sorted by {string} in descending order")
    public void sales_should_be_sorted_by_in_descending_order(String columnName) {
        Assert.assertTrue(salesListPage.isSortedBy(columnName, "descending"));
    }

    @Then("the first sale should be the most recent")
    public void the_first_sale_should_be_the_most_recent() {
        Assert.assertTrue(salesListPage.isFirstSaleMostRecent());
    }

    @Then("sort indicator should show {string} for {string} column")
    public void sort_indicator_should_show_for_column(String indicator, String columnName) {
        String actualIndicator = salesListPage.getSortIndicator(columnName);
        Assert.assertEquals(actualIndicator, indicator);
    }

    @When("I click on {string} column header")
    public void i_click_on_column_header(String columnName) {
        salesListPage.clickColumnHeader(columnName);
    }

    @Then("sales should be sorted by {string} in ascending order")
    public void sales_should_be_sorted_by_in_ascending_order(String columnName) {
        Assert.assertTrue(salesListPage.isSortedBy(columnName, "ascending"));
    }

    @When("I click on {string} column header twice")
    public void i_click_on_column_header_twice(String columnName) {
        salesListPage.clickColumnHeader(columnName);
        salesListPage.clickColumnHeader(columnName);
    }

    @Given("sales are sorted by {string} descending")
    public void sales_are_sorted_by_descending(String columnName) {
        salesListPage.clickColumnHeader(columnName);
        Assert.assertTrue(salesListPage.isSortedBy(columnName, "descending"));
    }

    @When("I refresh the page")
    public void i_refresh_the_page() {
        DriverManager.getDriver().navigate().refresh();
    }

    @Then("sales should remain sorted by {string} descending")
    public void sales_should_remain_sorted_by_descending(String columnName) {
        Assert.assertTrue(salesListPage.isSortedBy(columnName, "descending"));
    }

    @Given("there are no sales")
    public void there_are_no_sales() {
        // Delete all sales or ensure no sales exist
    }

    @Then("I should see message {string}")
    public void i_should_see_message(String expectedMessage) {
        String actualMessage = salesListPage.getEmptyStateMessage();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Then("sale should be created successfully")
    public void sale_should_be_created_successfully() {
        Assert.assertTrue(salesListPage.isPageLoaded());
        Assert.assertTrue(salesListPage.isSuccessMessageDisplayed());
    }
}