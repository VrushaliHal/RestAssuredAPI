package TestCases;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo_class.UserDetails;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.unregisterParser;

public class GetUsersTest extends BaseTest {


    @BeforeMethod
    public void sendRequestUrl() {
        requestSpecification = given()
        .baseUri("https://gorest.co.in")
        .basePath("/public/v2/users")
        .header("Authorization", AUTH_TOKEN)
        .contentType(ContentType.JSON);
    }

    @Test(description = "TC_001: Verify user details for single user")
    public void test_001() throws JsonProcessingException {
        userDetails = new UserDetails();
        userDetails.setName("Chaitali Kulkarni");
        userDetails.setEmail("chai.kool@gmail.com");
        userDetails.setStatus("active");
        userDetails.setGender("male");
        objectMapper = new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);
        requestSpecification.basePath("/public/v2/users");
        response = requestSpecification.post();
        response.prettyPrint();

        requestSpecification.queryParam("name", "Chaitali Kulkarni");
        response = requestSpecification.get();
        jsonPathView = response.jsonPath();

        String Delete_User_Id = jsonPathView.getString("[0].id");
        Assert.assertEquals(jsonPathView.getString("[0].name"), "Chaitali Kulkarni");
        Assert.assertEquals(jsonPathView.getString("[0].email"), "chai.kool@gmail.com");
        Assert.assertEquals(jsonPathView.getString("[0].gender"), "male");
        Assert.assertEquals(jsonPathView.getString("[0].status"), "active");

        requestSpecification.basePath("/public/v2/users" + Delete_User_Id);
        requestSpecification.delete();


    }


    @Test(description = "TC_003: Verify user detail for gender Female and status Inactive")
    public void test_003()
    {
        requestSpecification.queryParam("status", "inactive");
        requestSpecification.queryParam("gender", "female");
        response = requestSpecification.get();

        responseBodyString = response.asString();
        jsonPathView = new JsonPath(responseBodyString);

        for( int i =0 ; i< jsonPathView.getInt("id.size()") ;i++)
        {
        Assert.assertEquals(jsonPathView.getString("["+i+"].gender"),"female");
        Assert.assertEquals(jsonPathView.getString("["+i+"].status"),"inactive");

        }
        Assert.assertTrue(true);
    }
    @Test(description = "TC_004: Verify user Detail for gender male and status active")
    public void test_004()
    {
        requestSpecification.queryParam("gender","male")
                .queryParam("status","active");

        response = requestSpecification.get();
        responseBodyString = response.asString();
        jsonPathView = new JsonPath(responseBodyString);

        for( int i =0 ; i< jsonPathView.getInt("id.size()") ;i++)
        {
            Assert.assertEquals(jsonPathView.getString("["+i+"].gender"),"male");
            Assert.assertEquals(jsonPathView.getString("["+i+"].status"),"active");

        }
        Assert.assertTrue(true);
    }

    @Test(description = "TC_005: Verify Response schema is valid")
    public void test_005()
    {
        response =requestSpecification.get();
        given().then()
                .body("" , Matchers.notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchema(new File("C:\\Users\\govin\\IdeaProjects\\RestAssuredFramework\\src\\test\\java\\TestData\\get_test_005.json")));
        Assert.assertTrue(true);
    }
    @Test(description = "TC_006: Verify Response time is less than 200ms")
    public void test_006()
    {

        response = requestSpecification.get();
        response.prettyPrint();
        response.then().spec(responseSpecification);
    }
}
