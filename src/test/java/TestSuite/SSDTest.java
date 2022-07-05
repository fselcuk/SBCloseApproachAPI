package TestSuite;
/* this suite gets all Close Approach objects data of Mars for the next 6 months sorted by distance
and executes the test steps in the testcase1 and testcase2
 */
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SSDTest extends TestBase {

    /* TESTCASE1
      Given accept type is Json
      And query parameters:
        body is Mars, date-min is 2022-07-05, date-max is 2023-01-05 and sort is date
      When user sends a get request to https://ssd-api.jpl.nasa.gov/cad.api
      Then status code should be 200
      And content type should be application/json
      And json payload should contain signature object with the version 1.4
       */
    @Test
    public void testCase1(){
        extentLogger=report.createTest("TestCase-01");
        Response response = given().accept(ContentType.JSON)
                .when().get("?body=Mars&date-min=2022-07-05&date-max=2023-01-05&sort=date");


        extentLogger.info("verify that status code is 200");
        assertEquals(response.statusCode(), 200);
        extentLogger.pass("Step 1 PASSED");

        extentLogger.info("verify if content type json");
        assertEquals(response.contentType(), "application/json");
        extentLogger.pass("Step 2 PASSED");

        extentLogger.info("verify body contains signature");
        assertTrue(response.body().asString().contains("signature"));
        extentLogger.pass("Step 3 PASSED");

        extentLogger.info("verify if the version is 1.4");
        assertEquals(response.path("signature.version"),"1.4");
        extentLogger.pass("Step 4 PASSED");

    }

    /*
      Given accept type is Json
      And query parameters
        body is Mars, date-min is 2022-07-05, date-max is 2023-01-05 and sort is date
      When user sends a get request to https://ssd-api.jpl.nasa.gov/cad.api
      1. Then status code should be 200
      2. And content type should be application/json
      3. And the response body should contain 26 Close Approach objects
      4. And the first Closing Approach object name should be  "2019 RD2"

       */
    @Test
    public void testCase2(){
        extentLogger=report.createTest("TestCase-02");
        Response response = given().accept(ContentType.JSON)
                .when()
                .get("?body=Mars&date-min=2022-07-05&date-max=2023-01-05&sort=date");

        extentLogger.info("verify that status code is 200");
        assertEquals(response.statusCode(), 200);
        extentLogger.pass("Step 1 PASSED");

        extentLogger.info("verify if content type json");
        assertEquals(response.contentType(), "application/json");
        extentLogger.pass("Step 2 PASSED");

        extentLogger.info("verify if the number of total objects is 26");
        Map<String,String> body = response.body().as(Map.class);
        assertEquals(body.get("count"),"26");
        extentLogger.pass("Step 3 PASSED");

        extentLogger.info("verify that the first object name is '2019 RD2' ");
        JsonPath jsonPath =response.jsonPath();
        assertEquals(jsonPath.getString("data[0][0]"),"2019 RD2");
        extentLogger.pass("Step 4 PASSED");

    }


}
