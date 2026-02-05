package com.example.debug;

import com.example.ui.pages.sales.SalesListPage;
import com.example.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;

public class TotalPriceDebugTest {
    
    @Test
    public void debugTotalPrices() {
        WebDriver driver = DriverManager.getDriver();
        SalesListPage salesListPage = new SalesListPage(driver);
        
        try {
            // Navigate to sales page
            driver.get("http://localhost:8081/ui/sales");
            Thread.sleep(3000);
            
            // Get all total prices from table
            List<Double> prices = salesListPage.getTotalPricesFromTable();
            System.out.println("Found " + prices.size() + " total prices:");
            for (int i = 0; i < prices.size(); i++) {
                System.out.println("Price " + i + ": $" + prices.get(i));
            }
            
            // Check if Total Price header is available
            boolean headerVisible = salesListPage.isColumnHeaderVisible("Total Price");
            System.out.println("Total Price header visible: " + headerVisible);
            
            boolean headerClickable = salesListPage.isTotalPriceHeaderClickable();
            System.out.println("Total Price header clickable: " + headerClickable);
            
            // Print page source for debugging
            System.out.println("Page URL: " + driver.getCurrentUrl());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}