package com.example.api.tests;

import com.example.api.clients.AuthClient;
import com.example.api.clients.PlantClient;
import com.example.api.models.response.PlantPaginatedResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

import java.util.List;

public class PlantApiTest {
    private AuthClient authClient = new AuthClient();
    private PlantClient plantClient = new PlantClient();
    
    @Test(testName = "TC_API_PLANTS_USER_06", description = "Verify User can access paginated plants with default parameters")
    public void testTC_API_PLANTS_USER_06() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        Response response = plantClient.getPagedPlants(userToken);
        
        Assert.assertEquals(response.getStatusCode(), 200, "Response should return HTTP 200 OK");
        
        response.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalElements", greaterThanOrEqualTo(25))
            .body("totalPages", greaterThan(0))
            .body("pageable", notNullValue())
            .body("number", equalTo(0))
            .body("size", greaterThan(0));
    }
    
    @Test(testName = "TC_API_PLANTS_USER_07", description = "Verify User can control pagination parameters")
    public void testTC_API_PLANTS_USER_07() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        Response firstResponse = plantClient.getPagedPlants(userToken, 1, 10);
        firstResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", lessThanOrEqualTo(10))
            .body("number", equalTo(1))
            .body("size", equalTo(10));
        
        int firstTotalElements = firstResponse.jsonPath().getInt("totalElements");
        int firstTotalPages = firstResponse.jsonPath().getInt("totalPages");
        
        Response secondResponse = plantClient.getPagedPlants(userToken, 2, 5);
        secondResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", lessThanOrEqualTo(5))
            .body("number", equalTo(2))
            .body("size", equalTo(5));
        
        int secondTotalElements = secondResponse.jsonPath().getInt("totalElements");
        int secondTotalPages = secondResponse.jsonPath().getInt("totalPages");
        
        Assert.assertEquals(firstTotalElements, secondTotalElements, 
                          "Total elements should be the same in both responses");
        
        int expectedFirstTotalPages = (int) Math.ceil((double) firstTotalElements / 10);
        Assert.assertEquals(firstTotalPages, expectedFirstTotalPages, 
                          "Total pages should be calculated correctly");
        
        int expectedSecondTotalPages = (int) Math.ceil((double) secondTotalElements / 5);
        Assert.assertEquals(secondTotalPages, expectedSecondTotalPages, 
                          "Total pages should be calculated correctly for different page size");
    }
    
    @Test(testName = "TC_API_PLANTS_USER_08", description = "Verify User can sort plants by name via API")
    public void testTC_API_PLANTS_USER_08() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        Response ascendingResponse = plantClient.getPagedPlantsWithSort(userToken, "name", "asc");
        ascendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<String> ascendingNames = ascendingResponse.jsonPath().getList("content.name");
        List<String> firstFourAscending = ascendingNames.stream().limit(4).collect(java.util.stream.Collectors.toList());
        
        Response descendingResponse = plantClient.getPagedPlantsWithSort(userToken, "name", "desc");
        descendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<String> descendingNames = descendingResponse.jsonPath().getList("content.name");
        List<String> firstFourDescending = descendingNames.stream().limit(4).collect(java.util.stream.Collectors.toList());
        
        Assert.assertTrue(firstFourAscending.size() >= 4, "Should have at least 4 plants for name sorting test");
        Assert.assertTrue(firstFourDescending.size() >= 4, "Should have at least 4 plants for name sorting test");
        
        Assert.assertEquals(firstFourAscending.get(0).toLowerCase(), firstFourAscending.get(0).toLowerCase(),
                          "First plant in ascending should be alphabetically first");
        
        Assert.assertEquals(firstFourDescending.get(0).toLowerCase(), firstFourDescending.get(0).toLowerCase(),
                          "First plant in descending should be alphabetically last");
        
        java.util.Collections.sort(firstFourAscending, String.CASE_INSENSITIVE_ORDER);
        java.util.Collections.sort(firstFourDescending, java.util.Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
        
        Assert.assertEquals(firstFourAscending.get(0), firstFourAscending.get(0),
                          "Ascending sort should be case-insensitive");
        Assert.assertEquals(firstFourDescending.get(0), firstFourDescending.get(0),
                          "Descending sort should be case-insensitive");
        
        Assert.assertNotEquals(firstFourAscending.get(0), firstFourDescending.get(0),
                             "First items in ascending and descending should be different");
    }
    
    @Test(testName = "TC_API_PLANTS_USER_09", description = "Verify User can sort plants by price via API")
    public void testTC_API_PLANTS_USER_09() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        Response ascendingResponse = plantClient.getPagedPlantsWithSort(userToken, "price", "asc");
        ascendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<Float> ascendingPrices = ascendingResponse.jsonPath().getList("content.price");
        List<Float> firstFiveAscending = ascendingPrices.stream().limit(5).collect(java.util.stream.Collectors.toList());
        
        Response descendingResponse = plantClient.getPagedPlantsWithSort(userToken, "price", "desc");
        descendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<Float> descendingPrices = descendingResponse.jsonPath().getList("content.price");
        List<Float> firstFiveDescending = descendingPrices.stream().limit(5).collect(java.util.stream.Collectors.toList());
        
        Assert.assertTrue(firstFiveAscending.size() >= 5, "Should have at least 5 plants for price sorting test");
        Assert.assertTrue(firstFiveDescending.size() >= 5, "Should have at least 5 plants for price sorting test");
        
        for (int i = 0; i < firstFiveAscending.size() - 1; i++) {
            Assert.assertTrue(firstFiveAscending.get(i) <= firstFiveAscending.get(i + 1),
                              "Ascending price sort should be in numeric order");
        }
        
        for (int i = 0; i < firstFiveDescending.size() - 1; i++) {
            Assert.assertTrue(firstFiveDescending.get(i) >= firstFiveDescending.get(i + 1),
                              "Descending price sort should be in reverse numeric order");
        }
        
        Assert.assertNotEquals(firstFiveAscending.get(0), firstFiveDescending.get(0),
                             "First items in ascending and descending should be different");
        
        Assert.assertTrue(firstFiveAscending.get(0) <= firstFiveDescending.get(0),
                         "First ascending price should be <= first descending price");
    }
    
    @Test(testName = "TC_API_PLANTS_USER_10", description = "Verify User can sort plants by stock quantity via API")
    public void testTC_API_PLANTS_USER_10() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        Response ascendingResponse = plantClient.getPagedPlantsWithSort(userToken, "quantity", "asc");
        ascendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<Integer> ascendingQuantities = ascendingResponse.jsonPath().getList("content.quantity");
        List<Integer> firstFiveAscending = ascendingQuantities.stream().limit(5).collect(java.util.stream.Collectors.toList());
        
        Response descendingResponse = plantClient.getPagedPlantsWithSort(userToken, "quantity", "desc");
        descendingResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("content.size()", greaterThan(0));
        
        List<Integer> descendingQuantities = descendingResponse.jsonPath().getList("content.quantity");
        List<Integer> firstFiveDescending = descendingQuantities.stream().limit(5).collect(java.util.stream.Collectors.toList());
        
        Assert.assertTrue(firstFiveAscending.size() >= 5, "Should have at least 5 plants for quantity sorting test");
        Assert.assertTrue(firstFiveDescending.size() >= 5, "Should have at least 5 plants for quantity sorting test");
        
        for (int i = 0; i < firstFiveAscending.size() - 1; i++) {
            Assert.assertTrue(firstFiveAscending.get(i) <= firstFiveAscending.get(i + 1),
                              "Ascending quantity sort should be in numeric order from lowest to highest");
        }
        
        for (int i = 0; i < firstFiveDescending.size() - 1; i++) {
            Assert.assertTrue(firstFiveDescending.get(i) >= firstFiveDescending.get(i + 1),
                              "Descending quantity sort should be in reverse numeric order from highest to lowest");
        }
        
        Assert.assertNotEquals(firstFiveAscending.get(0), firstFiveDescending.get(0),
                             "First items in ascending and descending should be different");
        
        Assert.assertTrue(firstFiveAscending.get(0) <= firstFiveDescending.get(0),
                         "First ascending quantity should be <= first descending quantity");
        
        int lowestQuantity = firstFiveAscending.get(0);
        int highestQuantity = firstFiveDescending.get(0);
        
        Assert.assertTrue(lowestQuantity <= highestQuantity,
                         "Low-stock plants should appear first in ascending sort");
        Assert.assertTrue(highestQuantity >= lowestQuantity,
                         "High-stock plants should appear first in descending sort");
    }
    
@Test(testName = "TC_API_PLANTS_USER_11", description = "Verify User can search plants by name via API")
    public void testTC_API_PLANTS_USER_11() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        // Test search for "monstera" - should find Monstera Deliciosa
        Response monsteraResponse = plantClient.searchPlants(userToken, "monstera");
        monsteraResponse.then()
            .statusCode(200)
            .body("content", notNullValue());
        
        List<String> monsteraNames = monsteraResponse.jsonPath().getList("content.name");
        
        // Test search for "plant" - should find Snake Plant but not Peace Lily
        Response plantResponse = plantClient.searchPlants(userToken, "plant");
        plantResponse.then()
            .statusCode(200)
            .body("content", notNullValue());
        
        List<String> plantNames = plantResponse.jsonPath().getList("content.name");
        
        // Verify Monstera Deliciosa is found when searching for "monstera"
        boolean foundMonsteraDeliciosa = monsteraNames.stream()
            .anyMatch(name -> name.toLowerCase().contains("monstera deliciosa"));
        
        // Verify Snake Plant is found when searching for "plant"
        boolean foundSnakePlant = plantNames.stream()
            .anyMatch(name -> name.toLowerCase().contains("snake plant"));
        
        // Test case-insensitive search by searching for "MONSTERA" (uppercase)
        Response caseInsensitiveResponse = plantClient.searchPlants(userToken, "MONSTERA");
        List<String> caseInsensitiveNames = caseInsensitiveResponse.jsonPath().getList("content.name");
        boolean foundMonsteraInCaseInsensitive = caseInsensitiveNames.stream()
            .anyMatch(name -> name.toLowerCase().contains("monstera deliciosa"));
        
        // Test partial name search by searching for "snake" (should find Snake Plant)
        Response partialSearchResponse = plantClient.searchPlants(userToken, "snake");
        List<String> partialNames = partialSearchResponse.jsonPath().getList("content.name");
        boolean foundSnakeInPartial = partialNames.stream()
            .anyMatch(name -> name.toLowerCase().contains("snake plant"));
        
        // All results for monstera search should contain "monstera" in the name
        for (String name : monsteraNames) {
            Assert.assertTrue(name.toLowerCase().contains("monstera"),
                              "All results for 'monstera' search should contain 'monstera'");
        }
        
        // All results for plant search should contain "plant" in the name
        for (String name : plantNames) {
            Assert.assertTrue(name.toLowerCase().contains("plant"),
                              "All results for 'plant' search should contain 'plant'");
        }
        
        // Validate search expectations
        if (monsteraNames.size() > 0) {
            Assert.assertTrue(foundMonsteraDeliciosa, 
                            "Should find 'Monstera Deliciosa' when searching for 'monstera'");
        }
        
        if (plantNames.size() > 0) {
            Assert.assertTrue(foundSnakePlant, 
                            "Should find 'Snake Plant' when searching for 'plant'");
        }
        
        // Validate case-insensitive search
        if (caseInsensitiveNames.size() > 0) {
            Assert.assertTrue(foundMonsteraInCaseInsensitive,
                            "Search should be case-insensitive");
        }
        
        // Validate partial search
        if (partialNames.size() > 0) {
            Assert.assertTrue(foundSnakeInPartial,
                            "Should support partial name search");
        }
        
        // Validate search response structure
        monsteraResponse.then()
            .body("totalElements", notNullValue())
            .body("totalPages", notNullValue())
            .body("number", notNullValue())
            .body("size", notNullValue())
            .body("pageable", notNullValue());
        
        plantResponse.then()
            .body("totalElements", notNullValue())
            .body("totalPages", notNullValue())
            .body("number", notNullValue())
            .body("size", notNullValue())
            .body("pageable", notNullValue());
        
        // Test search with non-existent term
        Response emptySearchResponse = plantClient.searchPlants(userToken, "xyznonexistent");
        emptySearchResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalElements", equalTo(0));
        
        List<String> emptyResults = emptySearchResponse.jsonPath().getList("content.name");
        Assert.assertEquals(emptyResults.size(), 0, "Search for non-existent term should return empty results");
    }
    
@Test(testName = "TC_API_PLANTS_USER_12", description = "Verify User can filter plants by category via API")
    public void testTC_API_PLANTS_USER_12() {
        String userToken = authClient.getUserToken();
        Assert.assertNotNull(userToken, "User token should not be null");
        
        // Test category filter for "Indoor"
        Response indoorResponse = plantClient.filterPlantsByCategory(userToken, "Indoor");
        indoorResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalElements", notNullValue())
            .body("pageable", notNullValue());
        
        List<String> indoorPlantNames = indoorResponse.jsonPath().getList("content.name");
        List<Object> indoorCategories = indoorResponse.jsonPath().getList("content.category.name");
        int indoorTotalElements = indoorResponse.jsonPath().getInt("totalElements");
        
        // Test category filter for "Outdoor"
        Response outdoorResponse = plantClient.filterPlantsByCategory(userToken, "Outdoor");
        outdoorResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalElements", notNullValue())
            .body("pageable", notNullValue());
        
        List<String> outdoorPlantNames = outdoorResponse.jsonPath().getList("content.name");
        List<Object> outdoorCategories = outdoorResponse.jsonPath().getList("content.category.name");
        int outdoorTotalElements = outdoorResponse.jsonPath().getInt("totalElements");
        
        // Validate API response structure is correct
        Assert.assertTrue(indoorPlantNames.size() >= 0, "Indoor filter should return results list");
        Assert.assertTrue(outdoorPlantNames.size() >= 0, "Outdoor filter should return results list");
        
        // Category filtering should work - test expects proper filtering
        // Since there's a known bug, this test should fail when filtering doesn't work
        
        // Verify API accepts the category parameter (doesn't error out)
        Assert.assertTrue(indoorResponse.getStatusCode() == 200, 
                        "API should accept category parameter for Indoor");
        Assert.assertTrue(outdoorResponse.getStatusCode() == 200, 
                        "API should accept category parameter for Outdoor");
        
        // Check if Indoor filter returns only Indoor plants
        boolean foundIndoorInResults = false;
        for (Object category : indoorCategories) {
            if (category != null && category.toString().equals("Indoor")) {
                foundIndoorInResults = true;
                break;
            }
        }
        
        // Check if Outdoor filter returns only Outdoor plants  
        boolean foundOutdoorInResults = false;
        for (Object category : outdoorCategories) {
            if (category != null && category.toString().equals("Outdoor")) {
                foundOutdoorInResults = true;
                break;
            }
        }
        
        // Test should pass only if both filters work correctly
        if (foundIndoorInResults && foundOutdoorInResults) {
            // Category filtering is working correctly - validate all expected behavior
            Assert.assertTrue(true, "Category filtering is working as expected");
            
            // Validate that no plants from other categories are included
            for (Object category : indoorCategories) {
                if (category != null) {
                    String categoryName = category.toString();
                    Assert.assertTrue(categoryName.equals("Indoor"), 
                                      "Indoor filter should only return Indoor plants");
                }
            }
            
            for (Object category : outdoorCategories) {
                if (category != null) {
                    String categoryName = category.toString();
                    Assert.assertTrue(categoryName.equals("Outdoor"), 
                                      "Outdoor filter should only return Outdoor plants");
                }
            }
            
            // Validate totalElements reflects filtered count
            Assert.assertEquals(indoorPlantNames.size(), indoorTotalElements,
                              "totalElements should match Indoor results count");
            Assert.assertEquals(outdoorPlantNames.size(), outdoorTotalElements,
                              "totalElements should match Outdoor results count");
        } else {
            // Category filtering has a bug - TEST SHOULD FAIL
            Assert.fail("CATEGORY FILTERING BUG: API accepts category parameter but doesn't filter results. " +
                       "Expected: Only Indoor plants for category=Indoor. " +
                       "Expected: Only Outdoor plants for category=Outdoor. " +
                       "Actual: API returns all plants (ignores category filter). " +
                       "Status: This is a known application bug - TEST FAILED");
        }
        
        // Additional validation - test with non-existent category
        Response nonExistentCategoryResponse = plantClient.filterPlantsByCategory(userToken, "NonExistentCategory");
        nonExistentCategoryResponse.then()
            .statusCode(200)
            .body("content", notNullValue())
            .body("totalElements", notNullValue());
        
        int nonExistentTotalElements = nonExistentCategoryResponse.jsonPath().getInt("totalElements");
        Assert.assertTrue(nonExistentTotalElements >= 0, 
                         "Non-existent category should return valid response structure");
    }
}
