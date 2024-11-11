package TestCases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pojo_class.UserDetails;

import static io.restassured.RestAssured.given;

public class PutUsersTest extends BaseTest {

    @BeforeMethod
    public void sendRequestUrl(){
         requestSpecification = given();
         requestSpecification.baseUri("https://gorest.co.in");
         requestSpecification.basePath("/public/v2/users/");
         requestSpecification.contentType(ContentType.JSON);
    }

    @Test(description = "TC_019 : verify response when name updated.")
    public void tc_019() throws JsonProcessingException {
         requestSpecification.header("Authorization",AUTH_TOKEN);
         userDetails = new UserDetails();
         userDetails.setName("Nikhil Joshi");
         userDetails.setEmail("nikhil.joshi03@gmail.com");
         userDetails.setStatus("active");
         userDetails.setGender("male");

        objectMapper = new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);
        response = requestSpecification.post();
        System.out.println("-----------POST Response-------------");
        response.prettyPrint();
        responseBodyData =response.getBody();
        responseBodyDataString =responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        PatchUserId = jsonPathView.getString("id");
        userDetails.setEmpId(PatchUserId);
        System.out.println(PatchUserId);

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        userDetails.setName("Nithin Joshi ");
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

         response = requestSpecification.put();
         System.out.println("/----------------------PUT response-----------------/");
         response.prettyPrint();
         responseBodyData = response.getBody();
         responseBodyDataString = responseBodyData.asString();
         jsonPathView = new JsonPath(responseBodyDataString);

         Assert.assertEquals(jsonPathView.getString("email"),userDetails.getEmail());
         Assert.assertEquals(jsonPathView.getString("name"),userDetails.getName());
         Assert.assertEquals(jsonPathView.getString("gender"), userDetails.getGender());
         Assert.assertEquals(jsonPathView.getString("status"),userDetails.getStatus());
         Assert.assertEquals(jsonPathView.getString("id"),userDetails.getEmpId());

        System.out.println("-----------------Deleting user Id----------------------");

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        response = requestSpecification.delete();
        response.then().statusCode(204).statusLine("HTTP/1.1 204 No Content");
    }

    @Test(description = "TC_020 : verify response when whole request schema changed")
    public void tc_020() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);
        userDetails = new UserDetails();
        userDetails.setName("Dinesh Kale");
        userDetails.setEmail("dinesh.kale@gmail.com");
        userDetails.setGender("male");
        userDetails.setStatus("active");

        objectMapper = new ObjectMapper();
        jsonPayload= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        response = requestSpecification.post();
        System.out.println("-----------POST Response-----------------");
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

        PatchUserId = jsonPathView.getString("id");
        userDetails.setEmpId(PatchUserId);

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        userDetails.setName("Kavita Mishra");
        userDetails.setEmail("kavita.mishr02@mail.com");
        userDetails.setGender("female");
        userDetails.setStatus("inactive");
        jsonPayload =  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);
        response = requestSpecification.put();
        System.out.println("-------------Put Response -------------------");
        response.prettyPrint();
        responseBodyData = response.getBody();
        responseBodyDataString =responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

       Assert.assertEquals(jsonPathView.getString("email"),userDetails.getEmail());
       Assert.assertEquals(jsonPathView.getString("name"),userDetails.getName());
       Assert.assertEquals(jsonPathView.getString("gender"),userDetails.getGender());
       Assert.assertEquals(jsonPathView.getString("status"),userDetails.getStatus());
       Assert.assertEquals(jsonPathView.getString("id"),userDetails.getEmpId());

       requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        response = requestSpecification.delete();
        given().then().statusCode(204).statusLine("HTTP/1.1 204 No Content");

    }
    @Test(description = "TC_021 : verify response when authentication not provided.")
    public void tc_021() throws JsonProcessingException {
        requestSpecification.basePath("/public/v2/users/"+7505180);
        userDetails =new UserDetails();
        userDetails.setName("Nithin Joshi ");
        objectMapper = new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);
        response = requestSpecification.put();

        responseBodyData = response.getBody();
        responseBodyDataString =responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        given().then().statusCode(404).statusLine("HTTP/1.1 404 Not Found");
        Assert.assertEquals(jsonPathView.getString("message"),"Resource not found");
    }

    @Test(description = "TC_022 : verify response time less than <150 when schema updated.")
    public void tc_022() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);

        userDetails =new UserDetails();
        userDetails.setName("Madhav");
        userDetails.setEmail("madhav.joshi01@gmail.com");
        userDetails.setGender("male");
        userDetails.setStatus("active");
        objectMapper =new ObjectMapper();
        jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);

        requestSpecification.body(jsonPayload);
        response = requestSpecification.post();
        responseBodyData =response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        PatchUserId =jsonPathView.getString("id");

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
        userDetails.setName("lilia Sabanci");

        jsonPayload =  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        response =  requestSpecification.put();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();

        jsonPathView = new JsonPath(responseBodyDataString);

        Assert.assertEquals(jsonPathView.getString("id"),PatchUserId);
        given().then().time(Matchers.lessThan(500L));

        requestSpecification.basePath("/public/v2/users/"+PatchUserId);
         response = requestSpecification.delete();
         given().then().statusLine("HTTP/1.1 204 No Content");







    }
}
