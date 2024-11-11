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

public class DeleteUsersTest extends  BaseTest{

   String Delete_User_id;

    @BeforeMethod
    public void sendRequestUrl() {

        requestSpecification = given();
        requestSpecification.baseUri("https://gorest.co.in");
        requestSpecification.basePath("/public/v2/users");
        requestSpecification.contentType(ContentType.JSON);

    }

    @Test(description = "TC_015 : verify response 204 No Content when user deleted.")
    public void tc_015() throws JsonProcessingException {

        requestSpecification.header("Authorization", AUTH_TOKEN);
        userDetails = new UserDetails();
        userDetails.setName("HrudayNath");
        userDetails.setEmail("hrudayNath.joshi06@gmail.com");
        userDetails.setGender("Male");
        userDetails.setStatus("active");

        objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
        requestSpecification.body(jsonPayload);

        response = requestSpecification.post();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();

        jsonPathView = new JsonPath(responseBodyDataString);
        Delete_User_id = jsonPathView.getString("id");

        requestSpecification.basePath("/public/v2/users" + "/" + Delete_User_id);
        response = requestSpecification.delete();

        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 204 No Content");

    }

    @Test(description = "TC_016 : verify response 404,Not Found when for repeated user provided")
    public void tc_016() {
        requestSpecification.header("Authorization", AUTH_TOKEN);
        response = requestSpecification.get();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);
        Delete_User_id = jsonPathView.getString("[0].id");

        requestSpecification.basePath("/public/v2/users" + "/" + Delete_User_id);
        requestSpecification.delete();
        /*----------------again deleting same user--------*/
        response = requestSpecification.delete();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath(responseBodyDataString);

        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 404 Not Found");
        Assert.assertEquals(jsonPathView.getString("message"), "Resource not found");

    }

    @Test(description = "TC_017 : verify response 404 Not Found when authentication not provided.")
    public void tc_017() {
        requestSpecification.basePath("/public/v2/users" + "/" + 770456);
        response = requestSpecification.delete();
        responseBodyData = response.getBody();
        responseBodyDataString = responseBodyData.asString();
        jsonPathView = new JsonPath((responseBodyDataString));
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 404 Not Found");
        Assert.assertEquals(jsonPathView.getString("message"), "Resource not found");
    }

    @Test(description = "TC_018: verify response time less than 400 when user deleted")
    public void tc_018() throws JsonProcessingException {
        requestSpecification.header("Authorization",AUTH_TOKEN);
        /*----------- using Pojo class ------------------------*/
        userDetails = new UserDetails();
        userDetails.setName("kunal");
        userDetails.setEmail("kunal.kulkarni@gmail.com");
        userDetails.setGender("female");
        userDetails.setStatus("active");
        objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDetails);
         requestSpecification.body(jsonPayload);
         /*------------------ before deleting first create user --------*/
         response = requestSpecification.post();
         responseBodyData = response.getBody();
         responseBodyDataString = responseBodyData.asString();
         jsonPathView = new JsonPath(responseBodyDataString);

         Delete_User_id = jsonPathView.getString("id");
         requestSpecification.basePath("/public/v2/users" + "/" +Delete_User_id);
         response =  requestSpecification.delete();
         Assert.assertEquals(response.getStatusCode(), 204);
         /*-----------------check response time less than 400----------------------*/
         response.then().time(Matchers.lessThan(400L));

    }
}
