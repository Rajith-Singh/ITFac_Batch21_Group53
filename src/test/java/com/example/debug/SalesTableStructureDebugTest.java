package com.example.debug;

import com.example.ui.pages.sales.SalesListPage;
import com.example.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class SalesTableStructureDebugTest {
    
    @Test
    public void debugSalesTableStructure() {
        WebDriver driver = DriverManager.getDriver();
        
        try {
            // First login properly
            driver.get("http://localhost:8081/ui/login");
            Thread.sleep(2000);
            
            // Fill login form
            driver.findElement(By.name("username")).sendKeys("testuser");
            driver.findElement(By.name("password")).sendKeys("test123");
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            Thread.sleep(3000);
            
            // Navigate to sales page
            driver.get("http://localhost:8081/ui/sales");
            Thread.sleep(3000);
            
            // Check all table headers
            List<WebElement> headers = driver.findElements(By.xpath("//table//th | //table//thead//tr//th"));
            System.out.println("Found " + headers.size() + " table headers:");
            for (int i = 0; i < headers.size(); i++) {
                System.out.println("Header " + i + ": '" + headers.get(i).getText().trim() + "'");
            }
            
            // Check all sortable links
            List<WebElement> sortableLinks = driver.findElements(By.xpath("//a[contains(@href, 'sortField')]"));
            System.out.println("\nFound " + sortableLinks.size() + " sortable links:");
            for (int i = 0; i < sortableLinks.size(); i++) {
                WebElement link = sortableLinks.get(i);
                System.out.println("Link " + i + ": '" + link.getText().trim() + "' -> href='" + link.getAttribute("href") + "'");
            }
            
            // Check table data
            List<WebElement> rows = driver.findElements(By.xpath("//table//tbody//tr"));
            System.out.println("\nFound " + rows.size() + " table rows");
            
            if (!rows.isEmpty()) {
                // Get first row data
                List<WebElement> cells = rows.get(0).findElements(By.xpath("./td"));
                System.out.println("First row has " + cells.size() + " cells:");
                for (int i = 0; i < cells.size(); i++) {
                    System.out.println("Cell " + i + ": '" + cells.get(i).getText().trim() + "'");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}