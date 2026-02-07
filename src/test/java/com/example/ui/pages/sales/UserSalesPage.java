package com.example.ui.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserSalesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Sales table elements
    @FindBy(css = "table.table tbody tr")
    private List<WebElement> salesTableRows;

    @FindBy(css = "table.table thead")
    private WebElement salesTableHeader;

    @FindBy(css = "table.table")
    private WebElement salesTable;

    // Pagination elements
    @FindBy(css = "nav ul.pagination")
    private WebElement paginationContainer;

    @FindBy(xpath = "//a[contains(@class, 'page-link') and contains(text(), 'Next')]")
    private WebElement nextButton;

    @FindBy(xpath = "//a[contains(@class, 'page-link') and contains(text(), 'Previous')]")
    private WebElement previousButton;

    @FindBy(css = "ul.pagination li.page-item")
    private List<WebElement> pageItems;

    @FindBy(css = "ul.pagination li.page-item.active")
    private WebElement activePageItem;

    // Current page indicator (if available)
    @FindBy(css = ".page-info, .pagination-info, .current-page")
    private WebElement pageInfo;

    // Security check elements
    @FindBy(xpath = "//button[contains(text(), 'Sell Plant')]")
    private WebElement sellPlantButton;

    @FindBy(xpath = "//a[contains(text(), 'Sell Plant')]")
    private WebElement sellPlantLink;

    // For delete security checks
    @FindBy(css = "table tbody tr")
    private List<WebElement> salesRows;

    @FindBy(xpath = "//th[a[contains(text(), 'Plant')]]/a")
    private WebElement plantColumnHeaderLink;

    // Quantity column header
    @FindBy(xpath = "//th/a[contains(text(), 'Quantity')]")
    private WebElement quantityColumnHeaderLink;

    public UserSalesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ===== TABLE METHODS =====

    /**
     * Wait for sales table to load
     */
    public void waitForSalesTableToLoad() {
        try {
            // Simple wait - no complex conditions
            Thread.sleep(2000);

            // Check if table is present
            List<WebElement> tables = driver.findElements(By.cssSelector("table.table, table"));
            System.out.println("Found " + tables.size() + " table(s)");

        } catch (Exception e) {
            System.out.println("Table wait error (continuing): " + e.getMessage());
        }
    }

    /**
     * Check if sales table is displayed
     */
    public boolean isSalesTableDisplayed() {
        try {
            return salesTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get count of sales records on current page
     */
    public int getCurrentPageRecordCount() {
        try {
            // Count all rows in the table body
            List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));

            // Filter out empty state rows
            int count = 0;
            for (WebElement row : rows) {
                try {
                    // Check if this is not an empty state row
                    List<WebElement> emptyCells = row.findElements(
                            By.xpath("./td[contains(@class, 'text-center') and contains(@class, 'text-muted')]"));

                    if (emptyCells.isEmpty()) {
                        // Check if it has data
                        List<WebElement> cells = row.findElements(By.tagName("td"));
                        if (!cells.isEmpty()) {
                            String firstCellText = cells.get(0).getText().trim();
                            if (!firstCellText.isEmpty() && !firstCellText.equals("No sales found")) {
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Skip this row if there's an issue
                }
            }
            return count;
        } catch (Exception e) {
            System.out.println("Error getting current page record count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Get total sales record count (across all pages)
     * Note: This might require parsing from page info or counting all pages
     */
    public int getTotalSalesRecordCount() {
        try {
            // First get count from current page
            int currentPageCount = getCurrentPageRecordCount();

            // Check if there's a total count displayed somewhere
            String pageSource = driver.getPageSource();

            // Look for common patterns showing total records
            if (pageSource.contains("total") || pageSource.contains("Total") ||
                    pageSource.contains("records") || pageSource.contains("Records")) {
                // In real implementation, you'd parse this number
                System.out.println("Found total records indicator in page");
            }

            // For now, return current page count (will be inaccurate for pagination)
            return currentPageCount;

        } catch (Exception e) {
            System.out.println("Error getting total count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Check if sales records are displayed
     */
    public boolean areSalesRecordsDisplayed() {
        return getCurrentPageRecordCount() > 0;
    }

    /**
     * Get details of all records on current page
     */
    public List<String> getCurrentPageRecordDetails() {
        List<String> records = new ArrayList<>();
        try {
            for (WebElement row : salesTableRows) {
                records.add(row.getText().trim());
            }
        } catch (Exception e) {
            System.out.println("Error getting record details: " + e.getMessage());
        }
        return records;
    }

    /**
     * Get text of first record on current page
     */
    public String getFirstRecordText() {
        try {
            if (salesTableRows.size() > 0) {
                return salesTableRows.get(0).getText().trim();
            }
        } catch (Exception e) {
            System.out.println("Error getting first record: " + e.getMessage());
        }
        return "";
    }

    // ===== PAGINATION METHODS =====

    /**
     * Check if pagination controls are present
     */
    public boolean isPaginationPresent() {
        try {
            return paginationContainer.isDisplayed();
        } catch (Exception e) {
            // Also try direct find
            try {
                List<WebElement> pagination = driver
                        .findElements(By.cssSelector("nav ul.pagination, .pagination, ul.pagination"));
                return !pagination.isEmpty();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Check if Next button is visible
     */
    public boolean isNextButtonVisible() {
        try {
            return nextButton.isDisplayed();
        } catch (Exception e) {
            // Try direct find
            try {
                List<WebElement> nextButtons = driver.findElements(
                        By.xpath(
                                "//a[contains(@class, 'page-link') and (contains(text(), 'Next') or contains(text(), '>') or contains(text(), 'next'))]"));
                return !nextButtons.isEmpty() && nextButtons.get(0).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Check if Next button is enabled (clickable)
     */
    public boolean isNextButtonEnabled() {
        try {
            // Check if parent li has 'disabled' class
            WebElement parentLi = nextButton.findElement(By.xpath("./.."));
            String liClass = parentLi.getAttribute("class");
            return !liClass.contains("disabled");
        } catch (Exception e) {
            // Simple check - just see if it's displayed and clickable
            return isNextButtonVisible();
        }
    }

    /**
     * Check if Previous button is visible
     */
    public boolean isPreviousButtonVisible() {
        try {
            return previousButton.isDisplayed();
        } catch (Exception e) {
            // Try direct find
            try {
                List<WebElement> prevButtons = driver.findElements(
                        By.xpath(
                                "//a[contains(@class, 'page-link') and (contains(text(), 'Previous') or contains(text(), '<') or contains(text(), 'prev'))]"));
                return !prevButtons.isEmpty();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Check if Previous button is present (same as visible for compatibility)
     */
    public boolean isPreviousButtonPresent() {
        return isPreviousButtonVisible();
    }

    /**
     * Check if Previous button is enabled
     */
    public boolean isPreviousButtonEnabled() {
        try {
            // Check if parent li has 'disabled' class
            WebElement parentLi = previousButton.findElement(By.xpath("./.."));
            String liClass = parentLi.getAttribute("class");
            return !liClass.contains("disabled");
        } catch (Exception e) {
            // If we can't check parent, assume it's enabled if present
            return isPreviousButtonVisible();
        }
    }

    /**
     * Check if page numbers are visible
     */
    public boolean arePageNumbersVisible() {
        try {
            return pageItems.size() > 2; // Should have at least Prev, page numbers, Next
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current page number
     */
    public int getCurrentPageNumber() {
        try {
            if (activePageItem != null) {
                String pageText = activePageItem.getText().trim();
                return Integer.parseInt(pageText);
            }

            // Alternative: parse from URL
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("page=")) {
                String pageParam = currentUrl.split("page=")[1].split("&")[0];
                return Integer.parseInt(pageParam);
            }

        } catch (Exception e) {
            System.out.println("Error getting page number: " + e.getMessage());
        }
        return 0; // Default to page 0 (first page)
    }

    /**
     * Get total number of pages
     */
    public int getTotalPages() {
        try {
            // Count page number items (excluding Prev/Next)
            int pageCount = 0;
            for (WebElement pageItem : pageItems) {
                String text = pageItem.getText().trim();
                if (text.matches("\\d+")) { // Check if it's a number
                    pageCount++;
                }
            }
            return pageCount;
        } catch (Exception e) {
            // Estimate based on URL pattern if available
            return 2; // Default minimum for pagination
        }
    }

    /**
     * Click Next button
     */
    public void clickNextButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            nextButton.click();
        } catch (Exception e) {
            // Fallback: find and click directly
            try {
                WebElement nextBtn = driver.findElement(
                        By.xpath(
                                "//a[contains(@class, 'page-link') and (contains(text(), 'Next') or contains(text(), '>') or contains(text(), 'next'))]"));
                nextBtn.click();
            } catch (Exception ex) {
                throw new RuntimeException("Could not click Next button: " + ex.getMessage());
            }
        }
    }

    /**
     * Click Previous button
     */
    public void clickPreviousButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(previousButton));
            previousButton.click();
        } catch (Exception e) {
            // Fallback: find and click directly
            try {
                WebElement prevBtn = driver.findElement(
                        By.xpath(
                                "//a[contains(@class, 'page-link') and (contains(text(), 'Previous') or contains(text(), '<') or contains(text(), 'prev'))]"));
                prevBtn.click();
            } catch (Exception ex) {
                throw new RuntimeException("Could not click Previous button: " + ex.getMessage());
            }
        }
    }

    /**
     * Click specific page number
     */
    public void clickPageNumber(int pageNumber) {
        try {
            WebElement pageLink = driver.findElement(
                    By.xpath("//a[contains(@class, 'page-link') and text()='" + pageNumber + "']"));
            pageLink.click();
        } catch (Exception e) {
            throw new RuntimeException("Could not click page " + pageNumber + ": " + e.getMessage());
        }
    }

    /**
     * Get page info text (e.g., "Showing 1-10 of 50 records")
     */
    public String getPageInfoText() {
        try {
            return pageInfo.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if on first page
     */
    public boolean isOnFirstPage() {
        return getCurrentPageNumber() == 0;
    }

    /**
     * Check if on last page
     */
    public boolean isOnLastPage() {
        int currentPage = getCurrentPageNumber();
        int totalPages = getTotalPages();
        return currentPage == totalPages - 1; // Zero-based indexing
    }

    /**
     * Navigate to specific page via URL
     */
    public void navigateToPage(int pageNumber) {
        String baseUrl = driver.getCurrentUrl().split("\\?")[0];
        driver.get(baseUrl + "?page=" + pageNumber);
        waitForSalesTableToLoad();
    }

    // ===== SECURITY CHECK METHODS =====

    /**
     * Simple check: Is Sell Plant button visible?
     */
    public boolean isSellPlantButtonVisible() {
        System.out.println("=== Checking Sell Plant button visibility ===");

        boolean buttonVisible = false;
        boolean linkVisible = false;

        try {
            // Check button
            if (sellPlantButton != null && sellPlantButton.isDisplayed()) {
                buttonVisible = true;
                System.out.println("Found 'Sell Plant' BUTTON");
            }
        } catch (Exception e) {
            // Button not found or not visible
        }

        try {
            // Check link
            if (sellPlantLink != null && sellPlantLink.isDisplayed()) {
                linkVisible = true;
                System.out.println("Found 'Sell Plant' LINK");
            }
        } catch (Exception e) {
            // Link not found or not visible
        }

        // Also check common variations
        boolean anySellButtonVisible = checkAnySellButton();

        boolean result = buttonVisible || linkVisible || anySellButtonVisible;
        System.out.println("Sell Plant button visible: " + result);

        return result;
    }

    /**
     * Check for any button with "Sell" text
     */
    private boolean checkAnySellButton() {
        try {
            // Check for any button or link with "Sell" text
            List<WebElement> sellElements = driver.findElements(
                    By.xpath("//button[contains(text(), 'Sell')] | //a[contains(text(), 'Sell')]"));

            for (WebElement element : sellElements) {
                if (element.isDisplayed()) {
                    System.out.println("Found element with 'Sell' text: " + element.getText());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if delete icons exist in first N rows
     */
    public boolean checkDeleteIconsInFirstNRows(int n) {
        System.out.println("Checking first " + n + " rows for delete icons");

        try {
            int rowsToCheck = Math.min(n, salesRows.size());
            System.out.println("Will check " + rowsToCheck + " rows (found " + salesRows.size() + " total)");

            for (int i = 0; i < rowsToCheck; i++) {
                WebElement row = salesRows.get(i);
                String rowText = row.getText().substring(0, Math.min(50, row.getText().length()));
                System.out.println("Row " + (i + 1) + " text: " + rowText + "...");

                // Look for delete-related elements
                List<WebElement> deleteElements = row.findElements(
                        By.cssSelector("[class*='delete'], [class*='trash'], .fa-trash, .bi-trash"));

                if (!deleteElements.isEmpty()) {
                    System.out.println("Found " + deleteElements.size() + " delete elements in row " + (i + 1));
                    for (WebElement element : deleteElements) {
                        if (element.isDisplayed()) {
                            return true;
                        }
                    }
                }
            }

            return false;

        } catch (Exception e) {
            System.out.println("Error in checkDeleteIconsInFirstNRows: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check for Delete text buttons
     */
    public boolean hasDeleteTextButtons() {
        try {
            // Simple direct check
            List<WebElement> deleteButtons = driver.findElements(
                    By.xpath("//button[contains(text(), 'Delete')]"));

            for (WebElement button : deleteButtons) {
                if (button.isDisplayed()) {
                    System.out.println("Found Delete button with text: " + button.getText());
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Simple security check summary
     */
    public void printSecurityCheckSummary() {
        System.out.println("=== SECURITY CHECK SUMMARY ===");
        System.out.println("Sales table displayed: " + isSalesTableDisplayed());
        System.out.println("Delete icons in first 5 rows: " + checkDeleteIconsInFirstNRows(5));
        System.out.println("Delete text buttons: " + hasDeleteTextButtons());
        System.out.println("==============================");
    }

    /**
     * Simple check: Are Next/Previous buttons available?
     */
    public boolean hasNavigationButtons() {
        return isNextButtonVisible() || isPreviousButtonVisible();
    }

    /**
     * Get pagination status
     */
    public String getPaginationStatus() {
        if (isPaginationPresent()) {
            return "Pagination controls present";
        } else if (hasNavigationButtons()) {
            return "Navigation buttons present";
        } else {
            return "No pagination found";
        }
    }

    /**
     * Quick check of common button locations
     */
    public void checkCommonButtonLocations() {
        System.out.println("=== Checking common button locations ===");

        // Common locations where admin buttons might appear
        String[] locations = {
                "Top-right corner",
                "Above the sales table",
                "Page header area",
                "Floating action button"
        };

        String[] xpaths = {
                "//div[contains(@class, 'header')]//button[contains(text(), 'Sell')]",
                "//h1/following-sibling::button[contains(text(), 'Sell')]",
                "//table/preceding::button[contains(text(), 'Sell')]",
                "//button[contains(@class, 'fab') or contains(@class, 'floating')]"
        };

        for (int i = 0; i < xpaths.length; i++) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xpaths[i]));
                if (!elements.isEmpty()) {
                    System.out.println("Found button in " + locations[i]);
                }
            } catch (Exception e) {
                // Continue
            }
        }
    }

    /**
     * Simple permissions check
     */
    public String getSimplePermissionsCheck() {
        boolean hasSellButton = isSellPlantButtonVisible();

        if (hasSellButton) {
            return "User has CREATE permission (Sell Plant button visible)";
        } else {
            return "User has VIEW-ONLY permission (No Sell Plant button)";
        }
    }

    // ===== SORTING METHODS FOR TC_UI_SALES_USER_04 =====

    /**
     * Click Plant column header to sort (using your exact HTML structure)
     */
    public void clickPlantColumnHeader() {
        try {
            System.out.println("Clicking Plant column header...");

            // Using your exact HTML structure from the provided example
            // <th>
            // <a class="text-white text-decoration-none"
            // href="/ui/sales?page=0&sortField=plant.name&sortDir=asc">
            // Plant
            // </a>
            // </th>

            WebElement plantHeader = driver.findElement(
                    By.xpath("//th/a[@class='text-white text-decoration-none' and contains(text(), 'Plant')]"));

            // Get current URL and href for debugging
            String currentUrl = driver.getCurrentUrl();
            String href = plantHeader.getAttribute("href");
            System.out.println("Current URL: " + currentUrl);
            System.out.println("Plant header href: " + href);
            System.out.println("Plant header text: " + plantHeader.getText().trim());

            // Click the Plant column header
            plantHeader.click();
            System.out.println("✓ Plant column header clicked");

            // Wait for URL to change (page reload)
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

            // Short wait for table to reload
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("Error clicking Plant column header: " + e.getMessage());

            // Fallback: try PageFactory element
            try {
                if (plantColumnHeaderLink != null) {
                    plantColumnHeaderLink.click();
                    System.out.println("Clicked using PageFactory element");
                }
            } catch (Exception ex) {
                throw new RuntimeException("Could not click Plant column header: " + ex.getMessage());
            }
        }
    }

    /**
     * Get Plant column sort direction from URL
     */
    public String getPlantColumnSortDirection() {
        try {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Checking sort direction from URL: " + currentUrl);

            // Check URL for sort parameters
            if (currentUrl.contains("sortDir=asc")) {
                return "ascending";
            } else if (currentUrl.contains("sortDir=desc")) {
                return "descending";
            } else {
                return "none"; // No sort parameter
            }

        } catch (Exception e) {
            System.out.println("Error getting sort direction: " + e.getMessage());
            return "unknown";
        }
    }

    /**
     * Get plant names from first N records
     */
    public List<String> getPlantNamesFromFirstNRecords(int n) {
        List<String> plantNames = new ArrayList<>();

        try {
            System.out.println("Getting plant names from first " + n + " records");

            // Get all table rows
            List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
            System.out.println("Total rows found: " + rows.size());

            int rowsToCheck = Math.min(n, rows.size());

            for (int i = 0; i < rowsToCheck; i++) {
                WebElement row = rows.get(i);

                // Get all cells in this row
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() > 0) {
                    // Plant name is in first column (index 0)
                    String plantName = cells.get(0).getText().trim();

                    if (!plantName.isEmpty()) {
                        plantNames.add(plantName);
                        System.out.println("Row " + (i + 1) + " plant: " + plantName);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error getting plant names: " + e.getMessage());
        }

        return plantNames;
    }

    /**
     * Get all plant names from current page
     */
    public List<String> getAllPlantNames() {
        List<String> plantNames = new ArrayList<>();

        try {
            // Get all table rows
            List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
            System.out.println("Getting plant names from " + rows.size() + " rows");

            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);

                // Get all cells in this row
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() > 0) {
                    // Plant name is in first column
                    String plantName = cells.get(0).getText().trim();

                    if (!plantName.isEmpty()) {
                        plantNames.add(plantName);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error getting all plant names: " + e.getMessage());
        }

        return plantNames;
    }

    /**
     * Check if plant names are in ascending (A-Z) order
     */
    public boolean arePlantNamesInAscendingOrder() {
        try {
            List<String> plantNames = getAllPlantNames();

            if (plantNames.size() < 2) {
                System.out.println("Only " + plantNames.size() + " plant names - can't verify sorting");
                return true; // Not enough data to test
            }

            System.out.println("Checking " + plantNames.size() + " plant names for ascending order");

            // Check if sorted case-insensitively
            for (int i = 0; i < plantNames.size() - 1; i++) {
                String current = plantNames.get(i).toLowerCase();
                String next = plantNames.get(i + 1).toLowerCase();

                if (current.compareTo(next) > 0) {
                    System.out.println("❌ Sorting issue at position " + i + ":");
                    System.out.println(
                            "  '" + plantNames.get(i) + "' should come before '" + plantNames.get(i + 1) + "'");
                    return false;
                }
            }

            System.out.println("✓ All plant names are in ascending order");
            return true;

        } catch (Exception e) {
            System.out.println("Error checking ascending order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if plant names are in descending (Z-A) order
     */
    public boolean arePlantNamesInDescendingOrder() {
        try {
            List<String> plantNames = getAllPlantNames();

            if (plantNames.size() < 2) {
                System.out.println("Only " + plantNames.size() + " plant names - can't verify sorting");
                return true; // Not enough data to test
            }

            System.out.println("Checking " + plantNames.size() + " plant names for descending order");

            // Check if reverse sorted case-insensitively
            for (int i = 0; i < plantNames.size() - 1; i++) {
                String current = plantNames.get(i).toLowerCase();
                String next = plantNames.get(i + 1).toLowerCase();

                if (current.compareTo(next) < 0) {
                    System.out.println("❌ Sorting issue at position " + i + ":");
                    System.out
                            .println("  '" + plantNames.get(i) + "' should come after '" + plantNames.get(i + 1) + "'");
                    return false;
                }
            }

            System.out.println("✓ All plant names are in descending order");
            return true;

        } catch (Exception e) {
            System.out.println("Error checking descending order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Print sorting debug information
     */
    public void printSortingDebugInfo() {
        System.out.println("=== SORTING DEBUG INFO ===");

        try {
            // Current URL
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            // Check for sort parameters in URL
            if (currentUrl.contains("sortField=")) {
                String sortField = extractParam(currentUrl, "sortField");
                String sortDir = extractParam(currentUrl, "sortDir");
                System.out.println("Sort field: " + sortField);
                System.out.println("Sort direction: " + sortDir);
            }

            // Try to find Plant header
            try {
                WebElement plantHeader = driver.findElement(
                        By.xpath("//th/a[contains(text(), 'Plant')]"));
                System.out.println("Plant header found: " + plantHeader.getText().trim());
                System.out.println("Plant header href: " + plantHeader.getAttribute("href"));
            } catch (Exception e) {
                System.out.println("Plant header not found: " + e.getMessage());
            }

            // Plant names
            List<String> plantNames = getPlantNamesFromFirstNRecords(5);
            System.out.println("First 5 plants: " + plantNames);

        } catch (Exception e) {
            System.out.println("Error in debug info: " + e.getMessage());
        }

        System.out.println("=========================");
    }

    /**
     * Helper method to extract parameter from URL
     */
    private String extractParam(String url, String paramName) {
        try {
            String[] parts = url.split("\\?");
            if (parts.length > 1) {
                String[] params = parts[1].split("&");
                for (String param : params) {
                    if (param.startsWith(paramName + "=")) {
                        return param.split("=")[1];
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return "not found";
    }

    /**
     * Simple method to check if sorting works
     */
    public boolean testPlantSorting() {
        System.out.println("=== Testing Plant Name Sorting Functionality ===");

        try {
            // Get initial state
            List<String> initialPlants = getPlantNamesFromFirstNRecords(3);
            System.out.println("Initial plants: " + initialPlants);

            // Click for ascending sort
            clickPlantColumnHeader();
            waitForSalesTableToLoad();

            boolean ascResult = arePlantNamesInAscendingOrder();
            System.out.println("Ascending sort: " + (ascResult ? "✓ PASS" : "✗ FAIL"));

            if (!ascResult) {
                return false;
            }

            // Click for descending sort
            clickPlantColumnHeader();
            waitForSalesTableToLoad();

            boolean descResult = arePlantNamesInDescendingOrder();
            System.out.println("Descending sort: " + (descResult ? "✓ PASS" : "✗ FAIL"));

            return descResult;

        } catch (Exception e) {
            System.out.println("Error testing sorting: " + e.getMessage());
            return false;
        }
    }

    // ===== QUANTITY SORTING METHODS FOR TC_UI_SALES_USER_05 =====

    /**
     * Click Quantity column header to sort
     */
    public void clickQuantityColumnHeader() {
        try {
            System.out.println("Clicking Quantity column header...");

            // Using your exact HTML structure
            // <th>
            // <a class="text-white text-decoration-none"
            // href="/ui/sales?page=0&amp;sortField=quantity&amp;sortDir=asc">
            // Quantity
            // </a>
            // </th>

            WebElement quantityHeader = driver.findElement(
                    By.xpath("//th/a[@class='text-white text-decoration-none' and contains(text(), 'Quantity')]"));

            // Get current URL and href for debugging
            String currentUrl = driver.getCurrentUrl();
            String href = quantityHeader.getAttribute("href");
            System.out.println("Current URL: " + currentUrl);
            System.out.println("Quantity header href: " + href);
            System.out.println("Quantity header text: " + quantityHeader.getText().trim());

            // Click the Quantity column header
            quantityHeader.click();
            System.out.println("✓ Quantity column header clicked");

            // Wait for URL to change (page reload)
            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));

            // Short wait for table to reload
            Thread.sleep(1000);

        } catch (Exception e) {
            System.out.println("Error clicking Quantity column header: " + e.getMessage());

            // Fallback: try PageFactory element
            try {
                if (quantityColumnHeaderLink != null) {
                    quantityColumnHeaderLink.click();
                    System.out.println("Clicked using PageFactory element");
                }
            } catch (Exception ex) {
                throw new RuntimeException("Could not click Quantity column header: " + ex.getMessage());
            }
        }
    }

    /**
     * Get Quantity column sort direction from URL
     */
    public String getQuantityColumnSortDirection() {
        try {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Checking quantity sort direction from URL: " + currentUrl);

            // Check URL for sort parameters
            if (currentUrl.contains("sortField=quantity")) {
                if (currentUrl.contains("sortDir=asc")) {
                    return "ascending";
                } else if (currentUrl.contains("sortDir=desc")) {
                    return "descending";
                }
            }

            return "none"; // No sort parameter for quantity

        } catch (Exception e) {
            System.out.println("Error getting quantity sort direction: " + e.getMessage());
            return "unknown";
        }
    }

    /**
     * Get quantities from first N records
     */
    public List<Integer> getQuantitiesFromFirstNRecords(int n) {
        List<Integer> quantities = new ArrayList<>();

        try {
            System.out.println("Getting quantities from first " + n + " records");

            // Get all table rows
            List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
            System.out.println("Total rows found: " + rows.size());

            int rowsToCheck = Math.min(n, rows.size());

            for (int i = 0; i < rowsToCheck; i++) {
                WebElement row = rows.get(i);

                // Get all cells in this row
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 2) { // Quantity is usually second column (index 1)
                    // Quantity is in second column
                    String quantityText = cells.get(1).getText().trim();

                    try {
                        // Try to parse as integer
                        int quantity = Integer.parseInt(quantityText);
                        quantities.add(quantity);
                        System.out.println("Row " + (i + 1) + " quantity: " + quantity);
                    } catch (NumberFormatException e) {
                        System.out.println("Row " + (i + 1) + " quantity not a number: " + quantityText);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error getting quantities: " + e.getMessage());
        }

        return quantities;
    }

    /**
     * Get all quantities from current page
     */
    public List<Integer> getAllQuantities() {
        List<Integer> quantities = new ArrayList<>();

        try {
            // Get all table rows
            List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
            System.out.println("Getting quantities from " + rows.size() + " rows");

            for (int i = 0; i < rows.size(); i++) {
                WebElement row = rows.get(i);

                // Get all cells in this row
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 2) {
                    // Quantity is in second column
                    String quantityText = cells.get(1).getText().trim();

                    try {
                        // Try to parse as integer
                        int quantity = Integer.parseInt(quantityText);
                        quantities.add(quantity);
                    } catch (NumberFormatException e) {
                        // If not a number, skip or handle as needed
                        System.out.println("Row " + (i + 1) + " has non-numeric quantity: " + quantityText);
                    }
                }
            }

            System.out.println("Total quantities parsed: " + quantities.size());

        } catch (Exception e) {
            System.out.println("Error getting all quantities: " + e.getMessage());
        }

        return quantities;
    }

    /**
     * Check if quantities are in ascending order
     */
    public boolean areQuantitiesInAscendingOrder() {
        try {
            List<Integer> quantities = getAllQuantities();

            if (quantities.size() < 2) {
                System.out.println("Only " + quantities.size() + " quantities - can't verify sorting");
                return true; // Not enough data to test
            }

            System.out.println("Checking " + quantities.size() + " quantities for ascending order");

            // Check if sorted in ascending order
            for (int i = 0; i < quantities.size() - 1; i++) {
                int current = quantities.get(i);
                int next = quantities.get(i + 1);

                if (current > next) {
                    System.out.println("❌ Sorting issue at position " + i + ":");
                    System.out.println("  " + current + " should be <= " + next + " for ascending order");
                    return false;
                }
            }

            System.out.println("✓ All quantities are in ascending order");
            return true;

        } catch (Exception e) {
            System.out.println("Error checking ascending order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if quantities are in descending order
     */
    public boolean areQuantitiesInDescendingOrder() {
        try {
            List<Integer> quantities = getAllQuantities();

            if (quantities.size() < 2) {
                System.out.println("Only " + quantities.size() + " quantities - can't verify sorting");
                return true; // Not enough data to test
            }

            System.out.println("Checking " + quantities.size() + " quantities for descending order");

            // Check if sorted in descending order
            for (int i = 0; i < quantities.size() - 1; i++) {
                int current = quantities.get(i);
                int next = quantities.get(i + 1);

                if (current < next) {
                    System.out.println("❌ Sorting issue at position " + i + ":");
                    System.out.println("  " + current + " should be >= " + next + " for descending order");
                    return false;
                }
            }

            System.out.println("✓ All quantities are in descending order");
            return true;

        } catch (Exception e) {
            System.out.println("Error checking descending order: " + e.getMessage());
            return false;
        }
    }

    /**
     * Print quantity sorting debug information
     */
    public void printQuantitySortingDebugInfo() {
        System.out.println("=== QUANTITY SORTING DEBUG INFO ===");

        try {
            // Current URL
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            // Check for quantity sort parameters in URL
            if (currentUrl.contains("sortField=quantity")) {
                System.out.println("Quantity sorting is active in URL");
                String sortDir = extractParam(currentUrl, "sortDir");
                System.out.println("Sort direction: " + sortDir);
            }

            // Try to find Quantity header
            try {
                WebElement quantityHeader = driver.findElement(
                        By.xpath("//th/a[contains(text(), 'Quantity')]"));
                System.out.println("Quantity header found: " + quantityHeader.getText().trim());
                System.out.println("Quantity header href: " + quantityHeader.getAttribute("href"));
            } catch (Exception e) {
                System.out.println("Quantity header not found: " + e.getMessage());
            }

            // Quantities
            List<Integer> quantities = getQuantitiesFromFirstNRecords(5);
            System.out.println("First 5 quantities: " + quantities);

        } catch (Exception e) {
            System.out.println("Error in quantity debug info: " + e.getMessage());
        }

        System.out.println("====================================");
    }

    /**
     * Test quantity sorting functionality
     */
    public boolean testQuantitySorting() {
        System.out.println("=== Testing Quantity Sorting Functionality ===");

        try {
            // Get initial state
            List<Integer> initialQuantities = getQuantitiesFromFirstNRecords(3);
            System.out.println("Initial quantities: " + initialQuantities);

            // Click for ascending sort
            clickQuantityColumnHeader();
            waitForSalesTableToLoad();

            boolean ascResult = areQuantitiesInAscendingOrder();
            System.out.println("Ascending quantity sort: " + (ascResult ? "✓ PASS" : "✗ FAIL"));

            if (!ascResult) {
                return false;
            }

            // Click for descending sort
            clickQuantityColumnHeader();
            waitForSalesTableToLoad();

            boolean descResult = areQuantitiesInDescendingOrder();
            System.out.println("Descending quantity sort: " + (descResult ? "✓ PASS" : "✗ FAIL"));

            return descResult;

        } catch (Exception e) {
            System.out.println("Error testing quantity sorting: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get column index by name (helper method)
     */
    private int getColumnIndex(String columnName) {
        try {
            // Get all header cells
            List<WebElement> headers = driver.findElements(By.cssSelector("table.table thead th"));

            for (int i = 0; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equalsIgnoreCase(columnName)) {
                    return i;
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding column index for " + columnName + ": " + e.getMessage());
        }

        // Default positions based on common table structure
        if (columnName.equalsIgnoreCase("Plant"))
            return 0;
        if (columnName.equalsIgnoreCase("Quantity"))
            return 1;
        if (columnName.equalsIgnoreCase("Price"))
            return 2;
        if (columnName.equalsIgnoreCase("Sold Date"))
            return 3;

        return -1; // Not found
    }

    /**
     * Extract quantity from table row
     */
    private Integer extractQuantityFromRow(WebElement row) {
        try {
            int quantityIndex = getColumnIndex("Quantity");
            if (quantityIndex == -1) {
                quantityIndex = 1; // Default to second column
            }

            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > quantityIndex) {
                String quantityText = cells.get(quantityIndex).getText().trim();
                return Integer.parseInt(quantityText);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return null;
    }
}