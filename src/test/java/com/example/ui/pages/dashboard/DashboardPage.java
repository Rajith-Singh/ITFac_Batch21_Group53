package com.example.ui.pages.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Dashboard heading
    @FindBy(xpath = "//h3[contains(text(), 'Dashboard')]")
    private WebElement dashboardHeading;

    // Navigation menu items
    @FindBy(xpath = "//a[@href='/ui/dashboard' and contains(@class, 'nav-link')]")
    private WebElement dashboardMenuLink;

    @FindBy(xpath = "//a[@href='/ui/categories' and contains(@class, 'nav-link')]")
    private WebElement categoriesMenuLink;

    @FindBy(xpath = "//a[@href='/ui/plants' and contains(@class, 'nav-link')]")
    private WebElement plantsMenuLink;

    @FindBy(xpath = "//a[@href='/ui/sales' and contains(@class, 'nav-link')]")
    private WebElement salesMenuLink;

    @FindBy(xpath = "//a[@href='javascript:void(0);' and contains(text(), 'Inventory')]")
    private WebElement inventoryMenuLink;

    @FindBy(xpath = "//a[@href='/ui/logout' and contains(@class, 'nav-link')]")
    private WebElement logoutLink;

    // All menu items
    @FindBy(xpath = "//div[@class='sidebar']//a[@class='nav-link text-white active'] | //div[@class='sidebar']//a[contains(@class, 'nav-link')]")
    private List<WebElement> allMenuItems;

    // Summary cards
    @FindBy(xpath = "//div[contains(@class, 'dashboard-card')]//h6[contains(text(), 'Categories')]/ancestor::div[contains(@class, 'card')]")
    private WebElement categoriesCard;

    @FindBy(xpath = "//div[contains(@class, 'dashboard-card')]//h6[contains(text(), 'Plants')]/ancestor::div[contains(@class, 'card')]")
    private WebElement plantsCard;

    @FindBy(xpath = "//div[contains(@class, 'dashboard-card')]//h6[contains(text(), 'Sales')]/ancestor::div[contains(@class, 'card')]")
    private WebElement salesCard;

    @FindBy(xpath = "//div[contains(@class, 'dashboard-card')]//h6[contains(text(), 'Inventory')]/ancestor::div[contains(@class, 'card')]")
    private WebElement inventoryCard;

    @FindBy(xpath = "//div[contains(@class, 'dashboard-card')]")
    private List<WebElement> allCards;

    // Manage buttons (Admin)
    @FindBy(xpath = "//a[@href='/ui/categories' and contains(text(), 'Manage Categories')]")
    private WebElement manageCategoriesButton;

    @FindBy(xpath = "//a[@href='/ui/plants' and contains(text(), 'Manage Plants')]")
    private WebElement managePlantsButton;

    @FindBy(xpath = "//a[@href='/ui/sales' and contains(text(), 'View Sales')]")
    private WebElement manageSalesButton;

    // View buttons (User) - same as manage for now
    @FindBy(xpath = "//a[@href='/ui/categories' and contains(text(), 'Manage Categories')]")
    private WebElement viewCategoriesButton;

    @FindBy(xpath = "//a[@href='/ui/plants' and contains(text(), 'Manage Plants')]")
    private WebElement viewPlantsButton;

    @FindBy(xpath = "//a[@href='/ui/sales' and contains(text(), 'View Sales')]")
    private WebElement viewSalesButton;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardLoaded() {
        try {
            wait.until(ExpectedConditions.visibilityOf(dashboardHeading));
            return dashboardHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDashboardHeadingVisible() {
        try {
            return dashboardHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDashboardTitle() {
        return driver.getTitle();
    }

    public boolean isDashboardMenuHighlighted() {
        try {
            String classAttribute = dashboardMenuLink.getAttribute("class");
            return classAttribute != null && (classAttribute.contains("active") || classAttribute.contains("selected") || classAttribute.contains("current"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMenuItemVisible(String menuItem) {
        try {
            WebElement element = null;
            switch (menuItem.toLowerCase()) {
                case "dashboard":
                    element = dashboardMenuLink;
                    break;
                case "categories":
                    element = categoriesMenuLink;
                    break;
                case "plants":
                    element = plantsMenuLink;
                    break;
                case "sales":
                    element = salesMenuLink;
                    break;
                case "inventory":
                    element = inventoryMenuLink;
                    break;
                case "logout":
                    element = logoutLink;
                    break;
                default:
                    return false;
            }
            return element != null && element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getVisibleMenuItemsCount() {
        int count = 0;
        for (WebElement item : allMenuItems) {
            if (item.isDisplayed()) {
                count++;
            }
        }
        return count;
    }

    public boolean isCategoriesCardVisible() {
        try {
            return categoriesCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPlantsCardVisible() {
        try {
            return plantsCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSalesCardVisible() {
        try {
            return salesCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInventoryCardVisible() {
        try {
            return inventoryCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areAllCardsVisible() {
        return isCategoriesCardVisible() && isPlantsCardVisible() && 
               isSalesCardVisible() && isInventoryCardVisible();
    }

    public boolean isCardDataValid(String cardName) {
        try {
            WebElement card = driver.findElement(By.xpath("//div[contains(@class, 'dashboard-card')]//h6[contains(text(), '" + cardName + "')]/ancestor::div[contains(@class, 'card')]"));
            // Just check if card is displayed and has content
            return card.isDisplayed() && !card.getText().trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickManageCategoriesButton() {
        wait.until(ExpectedConditions.elementToBeClickable(manageCategoriesButton));
        manageCategoriesButton.click();
    }

    public void clickManagePlantsButton() {
        wait.until(ExpectedConditions.elementToBeClickable(managePlantsButton));
        managePlantsButton.click();
    }

    public void clickManageSalesButton() {
        wait.until(ExpectedConditions.elementToBeClickable(manageSalesButton));
        manageSalesButton.click();
    }

    public void clickViewCategoriesButton() {
        wait.until(ExpectedConditions.elementToBeClickable(viewCategoriesButton));
        viewCategoriesButton.click();
    }

    public void clickViewPlantsButton() {
        wait.until(ExpectedConditions.elementToBeClickable(viewPlantsButton));
        viewPlantsButton.click();
    }

    public void clickViewSalesButton() {
        wait.until(ExpectedConditions.elementToBeClickable(viewSalesButton));
        viewSalesButton.click();
    }

    public void clickCategoriesMenu() {
        categoriesMenuLink.click();
    }

    public void clickPlantsMenu() {
        plantsMenuLink.click();
    }

    public void clickSalesMenu() {
        salesMenuLink.click();
    }

    public void clickLogout() {
        logoutLink.click();
    }

    public boolean isOnCategoriesPage() {
        return driver.getCurrentUrl().contains("categories") || 
               driver.getTitle().toLowerCase().contains("categories");
    }

    public boolean isOnPlantsPage() {
        return driver.getCurrentUrl().contains("plants") || 
               driver.getTitle().toLowerCase().contains("plants");
    }

    public boolean isOnSalesPage() {
        return driver.getCurrentUrl().contains("sales") || 
               driver.getTitle().toLowerCase().contains("sales");
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("login") || 
               driver.getTitle().toLowerCase().contains("login");
    }

    public void navigateBackToDashboard() {
        driver.navigate().back();
    }
}
