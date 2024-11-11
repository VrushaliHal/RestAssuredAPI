package TestCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo_class.UserDetails;

import java.io.File;

import static io.restassured.RestAssured.given;

public class PatchUsersTest extends  BaseTest{



    @BeforeMethod
    public void sendRequestUrl(){

        requestSpecification =given();
        requestSpecification.baseUri("https://gorest.co.in").basePath("/public/v2/users");
        requestSpecification.contentType(ContentType.JSON);
    }

    @Test(description = "TC_023 : verify response when only single field changed.")
    public void test_023() throws JsonProcessingException {
        requestSpecification.header("Authorization" , AUTH_TOKEN);
        userDetails =new UserDetails();
        userDetails.setName("Pratiksha Joshi");
        userDetails.setEmail("pratiksha.shinde01@gmail.com");
        userDetails.setStatus("active");
        userDetails.setGender("female");

         objectMapper = new ObjectMapper();
         jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
         requestSpecification.body(jsonPayload);
         log.info("---------------Post Response---------------------");
         response = requestSpecification.post();
         response.prettyPrint();
         responseBodyData =response.getBody();
         responseBodyDataString =responseBodyData.asString();
         jsonPathView = new JsonPath(responseBodyDataString);
         PatchUserId = jsonPathView.getString("id");
         userDetails.setEmpId(PatchUserId);
         Assert.assertEquals(jsonPathView.getString("email"),userDetails.getEmail());

          userDetails.setStatus("inactive");
          requestSpecification.basePath("/public/v2/users/"+PatchUserId);

          jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
          requestSpecification.body(jsonPayload);
          response = requestSpecification.patch();
          log.info("------------Patch Response-------------------");
          response.prettyPrint();

          responseBodyData = response.getBody();
          responseBodyDataString = responseBodyData.asString();
          jsonPathView = new JsonPath(responseBodyDataString);

          Assert.assertEquals(jsonPathView.getString("status"),userDetails.getStatus());

          requestSpecification.basePath("/public/v2/users/"+PatchUserId);
          requestSpecification.delete();
          given().then().statusCode(204).statusLine("HTTP/1.1 204 No Content");


    }

    @Test(description = "TC_024 : verify response when two field changed gender and status.")
    public void tc_024() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);
        log.info("----------------get Response----------------");
        response = requestSpecification.get();
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        PatchUserId = jsonPathView.getString("[5].id");
        userDetails = new UserDetails();
        userDetails.setName(jsonPathView.getString("[5].name"));
        userDetails.setEmail(jsonPathView.getString("[5].email"));
        userDetails.setGender("male");
        userDetails.setStatus("inactive");

        objectMapper =new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

         requestSpecification.basePath("/public/v2/users/"+PatchUserId);
         response = requestSpecification.patch();
         log.info("----------------Patch Response----------------");
        response.prettyPrint();
         responseBodyData = response.getBody();
         responseBodyDataString = responseBodyData.asString();
         jsonPathView = new JsonPath(responseBodyDataString);

         Assert.assertEquals(jsonPathView.getString("id"),PatchUserId);
         Assert.assertEquals(jsonPathView.getString("gender"), userDetails.getGender());
         Assert.assertEquals(jsonPathView.getString("status"),userDetails.getStatus());






    }

    @Test(description = "TC_025 : verify response time  less than 250 millisecond for the PATH request")
    public void tc_025() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);
        log.info("----------------get Response----------------");
        response = requestSpecification.get();
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        PatchUserId = jsonPathView.getString("[6].id");
        userDetails = new UserDetails();
        userDetails.setName(jsonPathView.getString("[6].name"));
        userDetails.setEmail(jsonPathView.getString("[6].email"));
        userDetails.setGender("male");
        userDetails.setStatus("inactive");

        objectMapper =new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        response = requestSpecification.patch();
        log.info("----------------Patch Response----------------");
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

        Assert.assertEquals(jsonPathView.getString("id"),PatchUserId);
        Assert.assertEquals(jsonPathView.getString("gender"), userDetails.getGender());
        Assert.assertEquals(jsonPathView.getString("status"),userDetails.getStatus());
        System.out.println(response.getTime());
        given().then().time(Matchers.lessThan(300L));






    }

    @Test(description = "TC_026 : Verify Response when Authentication not provided")
    public void tc_026() throws JsonProcessingException {

        userDetails = new UserDetails();
        userDetails.setName("Lilia");
        userDetails.setEmail("lilia.haldankar@gmail.com");
        userDetails.setGender("female");
        userDetails.setStatus("inactive");

        objectMapper =new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        requestSpecification.basePath("/public/v2/users/"+324567);
        response = requestSpecification.patch();
        log.info("----------------Patch Response----------------");
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

        Assert.assertEquals(jsonPathView.getString("message"),"Resource not found");
        Assert.assertEquals(response.getStatusLine(),"HTTP/1.1 404 Not Found");

    }

    @Test(description = "TC_027 : verify response Schema validation")
    public void tc_027() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);
        log.info("----------------get Response----------------");
        response = requestSpecification.get();
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        PatchUserId = jsonPathView.getString("[5].id");
        userDetails = new UserDetails();
        userDetails.setName(jsonPathView.getString("[5].name"));
        userDetails.setEmail(jsonPathView.getString("[5].email"));
        userDetails.setGender("male");
        userDetails.setStatus("inactive");

        objectMapper =new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        response = requestSpecification.patch();
        log.info("----------------Patch Response----------------");
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

        Assert.assertEquals(jsonPathView.getString("id"),PatchUserId);
        given().then().body("",Matchers.notNullValue())
                .body(JsonSchemaValidator.matchesJsonSchema(new File("C:\\Users\\govin\\IdeaProjects\\RestAssuredFramework\\src\\test\\java\\TestData\\patch_tc_026.json")));








    }

}
