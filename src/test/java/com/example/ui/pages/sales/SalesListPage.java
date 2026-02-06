package com.example.ui.pages.sales;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalesListPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page heading
    @FindBy(xpath = "//h3[contains(text(), 'Sales') or contains(text(), 'Sales List')]")
    private WebElement salesListHeading;

    // Plant Name column header with sorting indicator
    @FindBy(xpath = "//a[@class='text-white text-decoration-none' and contains(@href, '/ui/sales') and contains(text(), 'Plant')]")
    private WebElement plantNameHeader;

    // Alternative xpath for Plant Name header with more specificity
    @FindBy(xpath = "//a[contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]")
    private WebElement plantNameHeaderAlternative;

    // Additional fallback for table header version
    @FindBy(xpath = "//a[contains(@href, '/ui/sales') and (contains(@href, 'sortField=plant.name') or contains(@href, 'sortDir=')) and contains(text(), 'Plant')]")
    private WebElement plantNameHeaderFallback;

    // Quantity column header with sorting
    @FindBy(xpath = "//a[contains(@href, 'sortField=quantity') and contains(text(), 'Quantity')]")
    private WebElement quantityHeader;

    // Alternative xpath for Quantity header
    @FindBy(xpath = "//th[contains(text(), 'Quantity')]//a | //a[contains(@href, 'quantity') and contains(text(), 'Quantity')]")
    private WebElement quantityHeaderAlternative;

    // Total Price column header with sorting
    @FindBy(xpath = "//a[contains(@href, 'sortField=totalPrice') and contains(text(), 'Total Price')]")
    private WebElement totalPriceHeader;

    // Alternative xpath for Total Price header
    @FindBy(xpath = "//th[contains(text(), 'Total Price')]//a | //a[contains(@href, 'totalPrice') and contains(text(), 'Total Price')]")
    private WebElement totalPriceHeaderAlternative;

    // Sell Plant button
    @FindBy(xpath = "//a[contains(@href, '/ui/sales/new') and contains(text(), 'Sell Plant')]")
    private WebElement sellPlantButton;

    // Sales table rows
    @FindBy(xpath = "//table//tbody//tr")
    private List<WebElement> salesTableRows;

    // All column headers
    @FindBy(xpath = "//table//th")
    private List<WebElement> tableHeaders;

    // Plant name cells in table rows
    @FindBy(xpath = "//table//tbody//tr//td[contains(@class, 'plant') or contains(@class, 'name') or position()=1]")
    private List<WebElement> plantNameCells;

    // Sales table
    @FindBy(xpath = "//table")
    private WebElement salesTable;

    // Sort indicators (up/down arrows) - look for sortDir parameter in href
    @FindBy(xpath = "//a[contains(@href, 'sortDir=desc') and contains(text(), 'Plant')]")
    private WebElement descendingSortIndicator;

    @FindBy(xpath = "//a[contains(@href, 'sortDir=asc') and contains(text(), 'Plant')]")
    private WebElement ascendingSortIndicator;

    public SalesListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isSalesListPageLoaded() {
        try {
            // Wait for URL to contain sales
            wait.until(ExpectedConditions.urlContains("/ui/sales"));
            
            // Check if we're on sales page by URL
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.contains("/ui/sales")) {
                return false;
            }
            
            // Try to find any sales-related element to confirm page is loaded
            boolean hasSalesElements = false;
            
            // Check for sales heading (try multiple variations)
            try {
                WebElement heading = driver.findElement(By.xpath("//h1[contains(text(), 'Sales') or contains(text(), 'sales')] | //h2[contains(text(), 'Sales') or contains(text(), 'sales')] | //h3[contains(text(), 'Sales') or contains(text(), 'sales')]"));
                if (heading.isDisplayed()) {
                    hasSalesElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Check for sales table
            try {
                WebElement table = driver.findElement(By.xpath("//table"));
                if (table.isDisplayed()) {
                    hasSalesElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // Check for Plant Name sorting link
            try {
                WebElement plantLink = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales') and contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
                if (plantLink.isDisplayed()) {
                    hasSalesElements = true;
                }
            } catch (Exception e) {
                // Continue checking other elements
            }
            
            // If no specific elements found, at least check if page title contains Sales
            if (!hasSalesElements) {
                try {
                    String pageTitle = driver.getTitle();
                    if (pageTitle.toLowerCase().contains("sales")) {
                        hasSalesElements = true;
                    }
                } catch (Exception e) {
                    // Use final fallback
                }
            }
            
            return hasSalesElements;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickPlantNameHeader() {
        try {
            // Try the primary xpath first (matching the provided element)
            wait.until(ExpectedConditions.elementToBeClickable(plantNameHeader));
            plantNameHeader.click();
            Thread.sleep(1000); // Wait for UI to update
        } catch (Exception e) {
            try {
                // Fallback to alternative xpath
                wait.until(ExpectedConditions.elementToBeClickable(plantNameHeaderAlternative));
                plantNameHeaderAlternative.click();
                Thread.sleep(1000); // Wait for UI to update
            } catch (Exception ex) {
                try {
                    // Third fallback
                    wait.until(ExpectedConditions.elementToBeClickable(plantNameHeaderFallback));
                    plantNameHeaderFallback.click();
                    Thread.sleep(1000); // Wait for UI to update
                } catch (Exception exc) {
                    // Last resort - find by href pattern
                    WebElement header = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales') and contains(@href, 'sortField=plant.name') and contains(text(), 'Plant')]"));
                    header.click();
                    try {
                        Thread.sleep(1000); // Wait for UI to update
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }
    }

    public void clickQuantityHeader() {
        try {
            // Try the primary xpath first
            wait.until(ExpectedConditions.elementToBeClickable(quantityHeader));
            quantityHeader.click();
            Thread.sleep(2000); // Wait for UI to update
        } catch (Exception e) {
            try {
                // Fallback to alternative xpath
                wait.until(ExpectedConditions.elementToBeClickable(quantityHeaderAlternative));
                quantityHeaderAlternative.click();
                Thread.sleep(2000); // Wait for UI to update
            } catch (Exception ex) {
                // Last resort - find by href pattern
                WebElement header = driver.findElement(By.xpath("//a[contains(@href, 'sortField=quantity') and contains(text(), 'Quantity')]"));
                header.click();
                try {
                    Thread.sleep(2000); // Wait for UI to update
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public List<String> getPlantNamesFromTable() {
        List<String> plantNames = new ArrayList<>();
        
        try {
            // Wait for table rows to be visible
            wait.until(ExpectedConditions.visibilityOfAllElements(salesTableRows));
            
            // Extract plant names from table rows
            for (WebElement row : salesTableRows) {
                try {
                    // Try to find plant name in the first column or a column with plant/name class
                    WebElement plantCell = row.findElement(By.xpath("./td[contains(@class, 'plant') or contains(@class, 'name') or position()=1]"));
                    String plantName = plantCell.getText().trim();
                    if (!plantName.isEmpty()) {
                        plantNames.add(plantName);
                    }
                } catch (Exception e) {
                    // Skip rows that don't have plant names
                    continue;
                }
            }
        } catch (Exception e) {
            // Alternative approach: get all cells that might contain plant names
            try {
                List<WebElement> cells = driver.findElements(By.xpath("//table//tbody//td[contains(text(), 'Aloe Vera') or contains(text(), 'Monstera') or contains(text(), 'Snake Plant') or contains(text(), 'ZZ Plant')]"));
                for (WebElement cell : cells) {
                    String plantName = cell.getText().trim();
                    if (!plantName.isEmpty() && !plantNames.contains(plantName)) {
                        plantNames.add(plantName);
                    }
                }
            } catch (Exception ex) {
                // Return empty list if no plant names found
            }
        }
        
        return plantNames;
    }

    public List<Integer> getQuantitiesFromTable() {
        List<Integer> quantities = new ArrayList<>();
        
        try {
            // Wait for table rows to be visible
            wait.until(ExpectedConditions.visibilityOfAllElements(salesTableRows));
            
            // Extract quantities from table rows (assuming quantity is in column 2)
            for (WebElement row : salesTableRows) {
                try {
                    WebElement quantityCell = row.findElement(By.xpath("./td[position()=2]"));
                    String quantityText = quantityCell.getText().trim();
                    if (!quantityText.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityText);
                            quantities.add(quantity);
                        } catch (NumberFormatException e) {
                            // Skip invalid numbers
                            continue;
                        }
                    }
                } catch (Exception e) {
                    // Skip rows without quantity data
                    continue;
                }
            }
        } catch (Exception e) {
            // Return empty list if error occurs
        }
        
        return quantities;
    }

    public boolean isQuantityHeaderClickable() {
        try {
            WebElement header = quantityHeader.isDisplayed() ? quantityHeader : quantityHeaderAlternative;
            return header != null && header.isEnabled() && header.getAttribute("href") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateQuantitySortOrder(List<Integer> actualQuantities, boolean isAscending) {
        if (actualQuantities.size() < 2) {
            return true; // Cannot determine sort order with less than 2 items
        }

        for (int i = 0; i < actualQuantities.size() - 1; i++) {
            int current = actualQuantities.get(i);
            int next = actualQuantities.get(i + 1);

            if (isAscending) {
                if (current > next) {
                    return false; // Ascending order is broken
                }
            } else {
                if (current < next) {
                    return false; // Descending order is broken
                }
            }
        }
        
        return true; // All quantities are in the correct order
    }

    public List<Integer> getExpectedQuantitiesAscending() {
        List<Integer> expected = new ArrayList<>();
        expected.add(2);
        expected.add(3);
        expected.add(5);
        return expected;
    }

    public List<Integer> getExpectedQuantitiesDescending() {
        List<Integer> expected = new ArrayList<>();
        expected.add(5);
        expected.add(3);
        expected.add(2);
        return expected;
    }

    public boolean isPlantNameHeaderClickable() {
        try {
            WebElement header = null;
            if (plantNameHeader.isDisplayed()) {
                header = plantNameHeader;
            } else if (plantNameHeaderAlternative.isDisplayed()) {
                header = plantNameHeaderAlternative;
            } else if (plantNameHeaderFallback.isDisplayed()) {
                header = plantNameHeaderFallback;
            }
            
            return header != null && header.isEnabled() && header.getAttribute("href") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSortIndicator() {
        try {
            // Check for descending sort indicator in href
            if (descendingSortIndicator.isDisplayed()) {
                return "desc";
            }
            // Check for ascending sort indicator in href
            if (ascendingSortIndicator.isDisplayed()) {
                return "asc";
            }
            
            // Check the href attribute of the main header for sort direction
            WebElement header = null;
            if (plantNameHeader.isDisplayed()) {
                header = plantNameHeader;
            } else if (plantNameHeaderAlternative.isDisplayed()) {
                header = plantNameHeaderAlternative;
            } else if (plantNameHeaderFallback.isDisplayed()) {
                header = plantNameHeaderFallback;
            }
            
            if (header != null) {
                String href = header.getAttribute("href");
                if (href != null) {
                    if (href.contains("sortDir=desc")) {
                        return "desc";
                    } else if (href.contains("sortDir=asc")) {
                        return "asc";
                    }
                }
            }
        } catch (Exception e) {
            // No sort indicator found
        }
        return "none";
    }

    public boolean areSalesRecordsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(salesTableRows));
            return !salesTableRows.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getSalesRecordCount() {
        try {
            return salesTableRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isColumnHeaderVisible(String columnName) {
        try {
            // First try the new link-based headers
            if (columnName.equalsIgnoreCase("Plant") || columnName.equalsIgnoreCase("Plant Name")) {
                if (plantNameHeader.isDisplayed() || plantNameHeaderAlternative.isDisplayed() || plantNameHeaderFallback.isDisplayed()) {
                    return true;
                }
            }
            
            if (columnName.equalsIgnoreCase("Quantity")) {
                if (quantityHeader.isDisplayed() || quantityHeaderAlternative.isDisplayed()) {
                    return true;
                }
            }
            
            if (columnName.equalsIgnoreCase("Total Price")) {
                if (totalPriceHeader.isDisplayed() || totalPriceHeaderAlternative.isDisplayed()) {
                    return true;
                }
            }
            
            // Fallback to table headers if needed
            for (WebElement header : tableHeaders) {
                if (header.getText().trim().equalsIgnoreCase(columnName)) {
                    return header.isDisplayed();
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForPageLoad() {
        try {
            // Wait for URL to contain sales
            wait.until(ExpectedConditions.urlContains("/ui/sales"));
            
            // Try to wait for sales table
            try {
                wait.until(ExpectedConditions.visibilityOf(salesTable));
            } catch (Exception e) {
                // Table might not be present, continue
            }
            
            // Try to wait for heading
            try {
                wait.until(ExpectedConditions.visibilityOf(salesListHeading));
            } catch (Exception e) {
                // Heading might not be present, continue
            }
            
            // At least wait for some content to load
            Thread.sleep(1000);
            
        } catch (Exception e) {
            // Page might still be loading, continue anyway
        }
    }

    public List<String> getExpectedPlantNamesAscending() {
        List<String> expected = new ArrayList<>();
        expected.add("Aloe Vera");
        expected.add("Money Plant");
        expected.add("ZZZ Plant");
        return expected;
    }

    public List<String> getExpectedPlantNamesDescending() {
        List<String> expected = new ArrayList<>();
        expected.add("ZZZ Plant");
        expected.add("Money Plant");
        expected.add("Aloe Vera");
        return expected;
    }

    public boolean validateSortOrder(List<String> actualNames, boolean isAscending) {
        if (actualNames.size() < 2) {
            return true; // Cannot determine sort order with less than 2 items
        }

        for (int i = 0; i < actualNames.size() - 1; i++) {
            String current = actualNames.get(i);
            String next = actualNames.get(i + 1);

            int comparison = current.compareTo(next);

            if (isAscending) {
                if (comparison > 0) {
                    return false; // Ascending order is broken
                }
            } else {
                if (comparison < 0) {
                    return false; // Descending order is broken
                }
            }
        }
        
        return true;
    }

    // Total Price sorting methods
    public void clickTotalPriceHeader() {
        try {
            // Try the primary xpath first
            wait.until(ExpectedConditions.elementToBeClickable(totalPriceHeader));
            totalPriceHeader.click();
            Thread.sleep(2000); // Wait for UI to update
        } catch (Exception e) {
            try {
                // Fallback to alternative xpath
                wait.until(ExpectedConditions.elementToBeClickable(totalPriceHeaderAlternative));
                totalPriceHeaderAlternative.click();
                Thread.sleep(2000); // Wait for UI to update
            } catch (Exception ex) {
                // Last resort - find by href pattern
                WebElement header = driver.findElement(By.xpath("//a[contains(@href, 'sortField=totalPrice') and contains(text(), 'Total Price')]"));
                header.click();
                try {
                    Thread.sleep(2000); // Wait for UI to update
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public List<Double> getTotalPricesFromTable() {
        List<Double> totalPrices = new ArrayList<>();
        
        try {
            // Wait for table rows to be visible
            wait.until(ExpectedConditions.visibilityOfAllElements(salesTableRows));
            
            // Extract total prices from table rows (assuming total price is in column 3 or 4)
            for (WebElement row : salesTableRows) {
                try {
                    // Try different column positions for total price
                    WebElement totalPriceCell = null;
                    try {
                        totalPriceCell = row.findElement(By.xpath("./td[position()=3]"));
                    } catch (Exception e1) {
                        try {
                            totalPriceCell = row.findElement(By.xpath("./td[position()=4]"));
                        } catch (Exception e2) {
                            // Try to find cell containing $ symbol
                            totalPriceCell = row.findElement(By.xpath("./td[contains(text(), '$')]"));
                        }
                    }
                    
                    if (totalPriceCell != null) {
                        String priceText = totalPriceCell.getText().trim();
                        if (!priceText.isEmpty()) {
                            // Remove $ symbol and any commas, then parse as double
                            String cleanPrice = priceText.replaceAll("[^\\d.]", "");
                            try {
                                double price = Double.parseDouble(cleanPrice);
                                totalPrices.add(price);
                            } catch (NumberFormatException e) {
                                // Skip invalid numbers
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Skip rows without total price data
                    continue;
                }
            }
        } catch (Exception e) {
            // Return empty list if error occurs
        }
        
        return totalPrices;
    }

    public boolean isTotalPriceHeaderClickable() {
        try {
            WebElement header = totalPriceHeader.isDisplayed() ? totalPriceHeader : totalPriceHeaderAlternative;
            return header != null && header.isEnabled() && header.getAttribute("href") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateTotalPriceSortOrder(List<Double> actualPrices, boolean isAscending) {
        if (actualPrices.size() < 2) {
            return true; // Cannot determine sort order with less than 2 items
        }

        for (int i = 0; i < actualPrices.size() - 1; i++) {
            double current = actualPrices.get(i);
            double next = actualPrices.get(i + 1);

            if (isAscending) {
                if (current > next) {
                    return false; // Ascending order is broken
                }
            } else {
                if (current < next) {
                    return false; // Descending order is broken
                }
            }
        }
        
        return true; // All prices are in the correct order
    }

    public List<Double> getExpectedTotalPricesAscending() {
        List<Double> expected = new ArrayList<>();
        // Use actual data from the system - these will be updated based on real data
        expected.add(10.00);
        expected.add(20.00);
        expected.add(30.00);
        expected.add(40.00);
        return expected;
    }

    public List<Double> getExpectedTotalPricesDescending() {
        List<Double> expected = new ArrayList<>();
        // Use actual data from the system - these will be updated based on real data
        expected.add(40.00);
        expected.add(30.00);
        expected.add(20.00);
        expected.add(10.00);
        return expected;
    }

    // Sell Plant button related methods
    public boolean isSellPlantButtonVisible() {
        try {
            return sellPlantButton.isDisplayed();
        } catch (Exception e) {
            System.out.println("Sell Plant button not found with primary locator, trying fallback...");
            try {
                // Try alternative locator
                WebElement fallbackButton = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales/new')]"));
                if (fallbackButton.isDisplayed()) {
                    System.out.println("Found Sell Plant button with fallback locator");
                    return true;
                }
            } catch (Exception ex) {
                System.out.println("Sell Plant button not found with fallback locator either");
                // Try even more flexible locator
                try {
                    WebElement flexibleButton = driver.findElement(By.xpath("//a[contains(text(), 'Sell Plant')]"));
                    if (flexibleButton.isDisplayed()) {
                        System.out.println("Found Sell Plant button with flexible locator");
                        return true;
                    }
                } catch (Exception exc) {
                    System.out.println("Sell Plant button not found with any locator");
                }
            }
            return false;
        }
    }

    public boolean isSellPlantButtonClickable() {
        try {
            return sellPlantButton.isEnabled() && sellPlantButton.getAttribute("href") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickSellPlantButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sellPlantButton));
            sellPlantButton.click();
            Thread.sleep(2000); // Wait for navigation
        } catch (Exception e) {
            try {
                // Fallback - find by direct xpath
                WebElement button = driver.findElement(By.xpath("//a[contains(@href, '/ui/sales/new')]"));
                System.out.println("Clicking Sell Plant button with fallback locator");
                button.click();
                Thread.sleep(2000);
            } catch (Exception ex) {
                try {
                    // Even more flexible fallback
                    WebElement flexibleButton = driver.findElement(By.xpath("//a[contains(text(), 'Sell Plant')]"));
                    System.out.println("Clicking Sell Plant button with flexible locator");
                    flexibleButton.click();
                    Thread.sleep(2000);
                } catch (Exception exc) {
                    throw new RuntimeException("Failed to click Sell Plant button", exc);
                }
            }
        }
    }

    public String getSellPlantButtonHref() {
        try {
            return sellPlantButton.getAttribute("href");
        } catch (Exception e) {
            return null;
        }
    }

    // Sold Date sorting methods
    public List<LocalDate> getSoldDatesFromTable() {
        List<LocalDate> soldDates = new ArrayList<>();
        
        try {
            // Wait for table rows to be visible
            wait.until(ExpectedConditions.visibilityOfAllElements(salesTableRows));
            
            // Extract sold dates from table rows (dates are in column 4 based on debug analysis)
            for (WebElement row : salesTableRows) {
                try {
                    // Sold date is in column 4 (position=4)
                    WebElement soldDateCell = row.findElement(By.xpath("./td[position()=4]"));
                    
                    if (soldDateCell != null) {
                        String dateText = soldDateCell.getText().trim();
                        if (!dateText.isEmpty()) {
                            try {
                                LocalDate date = parseDate(dateText);
                                if (date != null) {
                                    soldDates.add(date);
                                }
                            } catch (Exception e) {
                                // Skip invalid dates
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Skip rows without sold date data
                    continue;
                }
            }
        } catch (Exception e) {
            // Return empty list if error occurs
        }
        
        return soldDates;
    }

    private LocalDate parseDate(String dateText) {
        // Remove any extra spaces and common separators
        dateText = dateText.trim().replaceAll("[,\\s]+", " ");
        
        // Try common date formats
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("MMM dd, yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        };
        
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateText, formatter);
            } catch (Exception e) {
                // Continue to next format
            }
        }
        
        return null;
    }

    public boolean validateSoldDateSortOrder(List<LocalDate> actualDates, boolean isAscending) {
        if (actualDates.size() < 2) {
            return true; // Cannot determine sort order with less than 2 items
        }

        for (int i = 0; i < actualDates.size() - 1; i++) {
            LocalDate current = actualDates.get(i);
            LocalDate next = actualDates.get(i + 1);

            if (isAscending) {
                if (current.isAfter(next)) {
                    return false; // Ascending order is broken
                }
            } else {
                if (current.isBefore(next)) {
                    return false; // Descending order is broken
                }
            }
        }
        
        return true; // All dates are in the correct order
    }

    public boolean areSoldDatesDisplayed() {
        List<LocalDate> dates = getSoldDatesFromTable();
        return !dates.isEmpty();
    }

    public LocalDate getMostRecentDate(List<LocalDate> dates) {
        if (dates.isEmpty()) {
            return null;
        }
        
        LocalDate mostRecent = dates.get(0);
        for (LocalDate date : dates) {
            if (date.isAfter(mostRecent)) {
                mostRecent = date;
            }
        }
        return mostRecent;
    }

    public LocalDate getOldestDate(List<LocalDate> dates) {
        if (dates.isEmpty()) {
            return null;
        }
        
        LocalDate oldest = dates.get(0);
        for (LocalDate date : dates) {
            if (date.isBefore(oldest)) {
                oldest = date;
            }
        }
        return oldest;
    }

    public void refreshPage() {
        try {
            driver.navigate().refresh();
            Thread.sleep(2000); // Wait for page to reload
            waitForPageLoad();
        } catch (Exception e) {
            // Continue if refresh fails
        }
    }
}
