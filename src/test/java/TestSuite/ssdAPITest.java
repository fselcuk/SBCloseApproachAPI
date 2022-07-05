package TestSuite;
/* this suite gets all Close Approach objects data of Mars for the next 6 months sorted by distance
and executes the test steps in the testcase1 and testcase2
 */

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class ssdAPITest {

    @BeforeClass
    public void beforeclass(){

        baseURI = ConfigurationReader.get("ssdAPI_url");
    }

    /* TESTCASE1
      Given accept type is Json
      And query parameters
        body is Mars, date-min is 2022-07-05, date-max is 2023-01-05 and sort is date
      When user sends a get request to https://ssd-api.jpl.nasa.gov/cad.api
      Then status code should be 200
      And content type should be application/json
      And json payload should contain signature object with the version 1.4
       */
@Test
    public void testCase1(){

        Response response = given().accept(ContentType.JSON)
                .when().get("?body=Mars&date-min=2022-07-05&date-max=2023-01-05&sort=date");
        //response.prettyPrint();
        //verify that status code is 200
        assertEquals(response.statusCode(), 200);
        //verify content type
        assertEquals(response.contentType(), "application/json");
        //verify body contains signature
        assertTrue(response.body().asString().contains("signature"));
        //verify if the version is 1.4
        String version= response.path("signature.version");
        assertEquals(version,"1.4");

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
        Response response = given().accept(ContentType.JSON)
                .when()
                .get("?body=Mars&date-min=2022-07-05&date-max=2023-01-05&sort=date");
        //1. verify that status code is 200
        assertEquals(response.statusCode(), 200);
        //2. verify content type
        assertEquals(response.contentType(), "application/json");
        //3. verify if the number of total objects is 26
        Map<String,String> body = response.body().as(Map.class);
        assertEquals(body.get("count"),"26");
        //4. verify that the first object name is "2019 RD2"
        JsonPath jsonPath =response.jsonPath();
        String firstObjectName = jsonPath.getString("data[0][0]");
        System.out.println("firstObjectName = " + firstObjectName);
        Assert.assertEquals(firstObjectName,"2019 RD2");


    }
}
