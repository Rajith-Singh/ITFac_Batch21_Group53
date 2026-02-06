package com.example.ui.pages.plants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class PlantListPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//a[contains(@href, '/ui/plants/add') and contains(text(), 'Add a Plant')]")
    private WebElement addPlantButton;

    @FindBy(xpath = "//table[@class='table table-striped table-bordered align-middle']")
    private WebElement plantsTable;

    public PlantListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void navigateToPlantsList(String baseUrl) {
        driver.navigate().to(baseUrl + "/ui/plants");
    }

    public void clickAddPlantButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addPlantButton)).click();
    }

    public boolean isPlantListPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(plantsTable));
            return true;
        } catch (Exception e) {
            // Alternatively check for header if table is empty
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Plants')]")));
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isPlantDisplayedInList(String plantName) {
        try {
            // Give it a small wait to ensure page is loaded
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }

            String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '"
                    + plantName.toLowerCase() + "')]";
            java.util.List<WebElement> plants = driver.findElements(By.xpath(xpath));
            return !plants.isEmpty() && plants.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void clickDeleteButtonForPlant(String plantName) {
        try {
            // XPath to find the delete button for a specific plant
            String xpath = "//td[contains(text(), '" + plantName
                    + "')]//ancestor::tr//form[contains(@action, '/plants/delete')]//button[@title='Delete']";
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            deleteButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking delete button for plant: " + plantName + " - " + e.getMessage());
            throw new RuntimeException("Could not click delete button for plant: " + plantName);
        }
    }

    public void clickEditButtonForPlant(String plantName) {
        try {
            // XPath to find the edit button (link with pencil icon) for a specific plant
            String xpath = "//td[contains(text(), '" + plantName
                    + "')]//ancestor::tr//a[contains(@href, '/edit') or contains(@class, 'btn-warning')]";
            WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            editButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking edit button for plant: " + plantName + " - " + e.getMessage());
            throw new RuntimeException("Could not click edit button for plant: " + plantName);
        }
    }

    public void confirmDeleteAction() {
        try {
            // Handle browser's native confirm dialog
            org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception e) {
            System.out.println("Could not handle confirm dialog: " + e.getMessage());
            throw new RuntimeException("Failed to confirm delete action");
        }
    }

    public boolean isSuccessMessageDisplayed(String message) {
        try {
            String xpath = "//div[contains(@class, 'alert-success')]//span[contains(text(), '" + message + "')]";
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return successMsg.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantDeletedFromList(String plantName) {
        try {
            // Wait for the element to disappear (max 5 seconds)
            String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(text(), '"
                    + plantName + "')]";
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(xpath))));
            return true;
        } catch (Exception e) {
            // Also try to check if element is not present at all
            try {
                String xpath = "//table[@class='table table-striped table-bordered align-middle']//tbody//td[contains(text(), '"
                        + plantName + "')]";
                return driver.findElements(By.xpath(xpath)).isEmpty();
            } catch (Exception ex) {
                return true;
            }
        }
    }

    public boolean isLowStockBadgeDisplayedForPlant(String plantName) {
        try {
            // Check if the "Low" badge exists in the same row as the plant name
            String xpath = "//td[contains(text(), '" + plantName
                    + "')]/following-sibling::td//span[contains(@class, 'badge') and contains(text(), 'Low')]";
            return !driver.findElements(By.xpath(xpath)).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPlantNameWithQuantityLessThan(int quantityLimit) {
        java.util.List<WebElement> dataRows = driver
                .findElements(By.xpath("//table[@class='table table-striped table-bordered align-middle']//tbody//tr"));
        for (WebElement row : dataRows) {
            try {
                // Determine structure based on provided HTML:
                // Name is likely 1st TD (index 0 in list, or td[1] xpath)
                // Quantity is 4th TD.
                // Note: The HTML shows Name is in first td.

                // Helper method logic
                WebElement nameCell = row.findElement(By.xpath("./td[1]"));
                // Quantity cell has span
                WebElement qtyCell = row.findElement(By.xpath("./td[4]"));
                String qtyText = qtyCell.findElement(By.tagName("span")).getText().trim();

                int qty = Integer.parseInt(qtyText);

                if (qty < quantityLimit) {
                    return nameCell.getText().trim();
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public String getPlantNameWithQuantityGreaterThanOrEqualTo(int quantityLimit) {
        java.util.List<WebElement> dataRows = driver
                .findElements(By.xpath("//table[@class='table table-striped table-bordered align-middle']//tbody//tr"));
        for (WebElement row : dataRows) {
            try {
                WebElement nameCell = row.findElement(By.xpath("./td[1]"));
                WebElement qtyCell = row.findElement(By.xpath("./td[4]"));
                String qtyText = qtyCell.findElement(By.tagName("span")).getText().trim();

                int qty = Integer.parseInt(qtyText);

                if (qty >= quantityLimit) {
                    return nameCell.getText().trim();
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public void clickSortByColumn(String columnName) {
        // Construct xpath for sorting headers based on column name
        String xpath = "//th[contains(., '" + columnName + "')]//a";
        try {
            WebElement sortLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            sortLink.click();
            // Wait for refresh? - usually waiting for stale element or some indicator
            try {
                Thread.sleep(1000); // Simple wait for demo, better to use staleness
            } catch (InterruptedException e) {
            }
        } catch (Exception e) {
            System.out.println("Could not find sort link for column: " + columnName + " - " + e.getMessage());
            // Intentionally not throwing for now to let test continue and potentially fail
            // on validation if needed,
            // or we can throw if we want strict failure.
            // The user expectation is failure, so throwing is fine, but maybe validation
            // failure is better.
            // Let's throw to be clear.
            throw new RuntimeException("Could not find sort link for column: " + columnName);
        }
    }

    public java.util.List<String> getPlantNames() {
        return getColumnTexts(1);
    }

    public java.util.List<Double> getPlantPrices() {
        return getColumnDoubles(3);
    }

    public java.util.List<Integer> getPlantQuantities() {
        return getColumnIntegers(4);
    }

    private java.util.List<String> getColumnTexts(int columnIndex) {
        java.util.List<String> values = new java.util.ArrayList<>();
        java.util.List<WebElement> rows = driver
                .findElements(By.xpath("//table[@class='table table-striped table-bordered align-middle']//tbody//tr"));
        for (WebElement row : rows) {
            try {
                // Skip rows that have colspan (empty state rows)
                WebElement cell = row.findElement(By.xpath("./td[" + columnIndex + "]"));
                String text = cell.getText().trim();

                // Skip if this is the empty state message
                if (!text.equalsIgnoreCase("No plants found") && !text.isEmpty()) {
                    values.add(text);
                }
            } catch (Exception e) {
                // Cell not found, skip this row
            }
        }
        return values;
    }

    private java.util.List<Double> getColumnDoubles(int columnIndex) {
        java.util.List<Double> values = new java.util.ArrayList<>();
        java.util.List<WebElement> rows = driver
                .findElements(By.xpath("//table[@class='table table-striped table-bordered align-middle']//tbody//tr"));
        for (WebElement row : rows) {
            try {
                WebElement cell = row.findElement(By.xpath("./td[" + columnIndex + "]"));
                values.add(Double.parseDouble(cell.getText().replaceAll("[^0-9.]", "")));
            } catch (Exception e) {
            }
        }
        return values;
    }

    private java.util.List<Integer> getColumnIntegers(int columnIndex) {
        java.util.List<Integer> values = new java.util.ArrayList<>();
        java.util.List<WebElement> rows = driver
                .findElements(By.xpath("//table[@class='table table-striped table-bordered align-middle']//tbody//tr"));
        for (WebElement row : rows) {
            try {
                WebElement cell = row.findElement(By.xpath("./td[" + columnIndex + "]"));
                // Quantity is inside a span in some cases, or just text
                String text = cell.getText().trim();
                // If there's a span with text, use that (for badge logic handled separately,
                // but raw number might be there)
                // Check if it has a span child?
                try {
                    text = cell.findElement(By.tagName("span")).getText().trim();
                } catch (Exception e) {
                }

                values.add(Integer.parseInt(text.replaceAll("[^0-9]", "")));
            } catch (Exception e) {
            }
        }
        return values;
    }

    public void selectCategory(String categoryName) {
        try {
            WebElement categorySelect = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.name("categoryId")));
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(categorySelect);
            // Need to select by visible text
            // The option text might be explicitly "Mango" or similar.
            try {
                select.selectByVisibleText(categoryName);
            } catch (Exception e) {
                // Should we try value? The user request says "Select a plant sub category...
                // (mango)".
                // Let's assume text works.
                throw new RuntimeException("Could not select category: " + categoryName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Category dropdown not found or not interactable: " + e.getMessage());
        }
    }

    public void clickSearchButton() {
        try {
            // Looking for button with text "Search" inside the form or generic
            WebElement searchBtn = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Search')]")));
            searchBtn.click();
            // Wait for potential refresh
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        } catch (Exception e) {
            throw new RuntimeException("Search button not found: " + e.getMessage());
        }
    }

    public void enterSearchText(String searchText) {
        try {
            WebElement searchField = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
            searchField.clear();
            searchField.sendKeys(searchText);
        } catch (Exception e) {
            throw new RuntimeException("Search field not found: " + e.getMessage());
        }
    }

    public java.util.List<String> getPlantCategories() {
        return getColumnTexts(2); // Category is column 2
    }
}
