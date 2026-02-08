// package com.example.api.tests;

// import com.example.api.base.BaseTest;
// import com.example.api.constants.ApiEndpoints;
// import io.restassured.RestAssured;
// import org.testng.annotations.Test;

// import static org.hamcrest.Matchers.*;

// public class PlantPaginationSortTest extends BaseTest {

//     @Test
//     public void TC_API_PLANTS_001_verifyPaginationAndSorting() {
//         // Test 1: Pagination - first page, size 1
//         RestAssured.given()
//                 .spec(requestSpec)
//                 .header("Authorization", "Bearer " + userToken)
//                 .queryParam("page", 0)
//                 .queryParam("size", 1)
//                 .queryParam("sort", "name,asc")
//         .when()
//                 .get(ApiEndpoints.PLANTS_PAGED)
//         .then()
//                 .spec(responseSpec)
//                 .statusCode(200)
//                 .body("content.size()", equalTo(1))
//                 .body("content[0].name", notNullValue())
//                 .body("pageable.pageNumber", equalTo(0))
//                 .body("pageable.pageSize", equalTo(1));

//         // Test 2: Sorting by name descending
//         RestAssured.given()
//                 .spec(requestSpec)
//                 .header("Authorization", "Bearer " + userToken)
//                 .queryParam("page", 0)
//                 .queryParam("size", 5)
//                 .queryParam("sort", "name,desc")
//         .when()
//                 .get(ApiEndpoints.PLANTS_PAGED)
//         .then()
//                 .spec(responseSpec)
//                 .statusCode(200)
//                 .body("content.size()", lessThanOrEqualTo(5))
//                 .body("content[0].name", notNullValue());

//         // Test 3: Sorting by price ascending
//         RestAssured.given()
//                 .spec(requestSpec)
//                 .header("Authorization", "Bearer " + userToken)
//                 .queryParam("page", 0)
//                 .queryParam("size", 5)
//                 .queryParam("sort", "price,asc")
//         .when()
//                 .get(ApiEndpoints.PLANTS_PAGED)
//         .then()
//                 .spec(responseSpec)
//                 .statusCode(200)
//                 .body("content.size()", lessThanOrEqualTo(5))
//                 .body("content[0].price", notNullValue());
//     }
// }



package com.example.api.tests;

import com.example.api.base.BaseTest;
import com.example.api.constants.ApiEndpoints;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PlantPaginationSortTest extends BaseTest {

    @Test
    public void TC_API_PLANTS_ADM_015_verifyPaginationAndSortingForAllFields() {
        // Fields to test
        String[] fields = {"name", "price", "quantity"};

        for (String field : fields) {
            // Ascending order test
            List<?> ascendingValues = RestAssured.given()
                    .spec(requestSpec)
                    .header("Authorization", "Bearer " + userToken)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .queryParam("sort", field + ",asc")
            .when()
                    .get(ApiEndpoints.PLANTS_PAGED)
            .then()
                    .spec(responseSpec)
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("content." + field);

            verifyAscendingOrder(ascendingValues, field);

            // Descending order test
            List<?> descendingValues = RestAssured.given()
                    .spec(requestSpec)
                    .header("Authorization", "Bearer " + userToken)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .queryParam("sort", field + ",desc")
            .when()
                    .get(ApiEndpoints.PLANTS_PAGED)
            .then()
                    .spec(responseSpec)
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("content." + field);

            verifyDescendingOrder(descendingValues, field);
        }
    }

    // Utility to check ascending order
private void verifyAscendingOrder(List<?> values, String field) {
    for (int i = 1; i < values.size(); i++) {
        Object previous = values.get(i - 1);
        Object current = values.get(i);

        if (previous instanceof Number && current instanceof Number) {
            // Cast to Double for comparison
            Double prevNum = ((Number) previous).doubleValue();
            Double currNum = ((Number) current).doubleValue();
            assertThat(currNum, greaterThanOrEqualTo(prevNum));
        } else if (previous instanceof String && current instanceof String) {
            assertThat((String) current, greaterThanOrEqualTo((String) previous));
        }
    }
}

// Utility to check descending order
private void verifyDescendingOrder(List<?> values, String field) {
    for (int i = 1; i < values.size(); i++) {
        Object previous = values.get(i - 1);
        Object current = values.get(i);

        if (previous instanceof Number && current instanceof Number) {
            // Cast to Double for comparison
            Double prevNum = ((Number) previous).doubleValue();
            Double currNum = ((Number) current).doubleValue();
            assertThat(currNum, lessThanOrEqualTo(prevNum));
        } else if (previous instanceof String && current instanceof String) {
            assertThat((String) current, lessThanOrEqualTo((String) previous));
        }
    }
}

}
