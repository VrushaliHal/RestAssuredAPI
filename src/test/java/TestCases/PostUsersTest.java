package TestCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.io.File;

import static io.restassured.RestAssured.given;

public class PostUsersTest  extends  BaseTest{


    @BeforeMethod
    public void sendRequestUrl(){
        requestSpecification =given();

        requestSpecification.baseUri("https://gorest.co.in");
        requestSpecification.basePath("/public/v2/users");


    }

    @Test(description= "TC_007: Verify user creation WITHOUT Authentication")
    public void tc_007(){


        response =requestSpecification.post();
        responseBodyData =response.getBody();
        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);
        System.out.println(jsonPathView);

        Assert.assertEquals(jsonPathView.getString("message"),"Authentication failed");

    }

    @Test(description = "TC_008: Verify the response for Empty id")
    public void tc_008() throws JsonProcessingException {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Geeta Joshi");
        userDetails.setEmail("geeta.joshi03@gmail.com");
        userDetails.setGender("Female");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);
        Assert.assertEquals(jsonPathView.getString("name"),"Geeta Joshi");
        Assert.assertEquals(jsonPathView.getString("email"),"geeta.joshi03@gmail.com");
        Assert.assertEquals(jsonPathView.getString("gender"),"female");
        Assert.assertEquals(jsonPathView.getString("status"),"active");

        String DeleteUserId = jsonPathView.getString("id");
        requestSpecification.basePath("/public/v2/users"+"/"+DeleteUserId);
        requestSpecification.delete();

    }

    @Test(description = "TC_009: Verify validation response for Empty name")
    public void test_009() throws JsonProcessingException {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName(" ");
        userDetails.setEmail("nikita.joshi03@gmail.com");
        userDetails.setGender("Female");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);



        Assert.assertEquals(jsonPathView.getString("[0].field"),"name");
        Assert.assertEquals(jsonPathView.getString("[0].message"),"can't be blank");


    }

    @Test(description = "TC_010: Verify the response for Empty email")
    public void test_010() throws JsonProcessingException {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Nita ");
        userDetails.setEmail("");
        userDetails.setGender("Female");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);

        Assert.assertEquals(jsonPathView.getString("[0].field"),"email");
        Assert.assertEquals(jsonPathView.getString("[0].message"),"can't be blank");


    }

    @Test(description = "TC_011: Verify the response for Empty gender")
    public void test_011() throws JsonProcessingException {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Nita ");
        userDetails.setEmail("nota.joshi@gmail.com");
        userDetails.setGender("");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);

        Assert.assertEquals(jsonPathView.getString("[0].field"),"gender");
        Assert.assertEquals(jsonPathView.getString("[0].message"),"can't be blank, can be male of female");


    }

    @Test(description = "TC_012: Verify the response for Empty status")
    public void test_012() throws JsonProcessingException {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Nikhil");
        userDetails.setEmail("nota.joshi@gmail.com");
        userDetails.setGender("male");
        userDetails.setStatus("");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);

        Assert.assertEquals(jsonPathView.getString("[0].field"),"status");
        Assert.assertEquals(jsonPathView.getString("[0].message"),"can't be blank");


    }

    @Test(description = "TC_013: Verify the response schema")
    public void test_013() throws JsonProcessingException {

        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Anita Joshi");
        userDetails.setEmail("anita.joshi@gmail.com");
        userDetails.setGender("Female");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);
        Assert.assertEquals(jsonPathView.getString("name"),"Anita Joshi");
        Assert.assertEquals(jsonPathView.getString("email"),"anita.joshi@gmail.com");
        Assert.assertEquals(jsonPathView.getString("gender"),"female");
        Assert.assertEquals(jsonPathView.getString("status"),"active");

        given().then()
                .body("" , Matchers.notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchema(new File("C:\\Users\\govin\\IdeaProjects\\RestAssuredFramework\\src\\test\\java\\TestData\\test_014.json")));


        String DeleteUserId = jsonPathView.getString("id");
        requestSpecification.basePath("/public/v2/users"+"/"+DeleteUserId);
        requestSpecification.delete();




    }

    @Test(description = "TC_014 : Verify the Response time less than 480milisecond")
    public void test_014() throws JsonProcessingException {

        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails.setName("Anita Joshi");
        userDetails.setEmail("anita.joshi@gmail.com");
        userDetails.setGender("Female");
        userDetails.setStatus("active");

        String jsonPayload =objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        requestSpecification.contentType(ContentType.JSON);
        response =requestSpecification.post();
        response.prettyPrint();
        responseBodyData =response.getBody();

        responseBodyString = responseBodyData.asString();
        jsonPathView =new JsonPath(responseBodyString);
        Assert.assertEquals(jsonPathView.getString("name"),"Anita Joshi");
        Assert.assertEquals(jsonPathView.getString("email"),"anita.joshi@gmail.com");
        Assert.assertEquals(jsonPathView.getString("gender"),"female");
        Assert.assertEquals(jsonPathView.getString("status"),"active");

        given().then().statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .time(Matchers.lessThan(500L));
        String DeleteUserId = jsonPathView.getString("id");
        requestSpecification.basePath("/public/v2/users"+"/"+DeleteUserId);
        requestSpecification.delete();




    }

}
