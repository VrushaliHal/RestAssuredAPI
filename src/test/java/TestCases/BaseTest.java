package TestCases;

import com.aventstack.extentreports.ExtentReports;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.specification.RequestSpecification;

import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pojo_class.UserDetails;
import utilities.ExtentListenerClass;

import static org.apache.logging.log4j.LogManager.getLogger;


public class BaseTest {

    ResponseSpecification responseSpecification =null;
    RequestSpecification requestSpecification = null;
    Response response =null;
    ResponseBodyData responseBodyData =null;
    String responseBodyString =null;
    JsonPath jsonPathView =null;
    String AUTH_TOKEN = "Bearer afb2d306632860f7639d6d4c8d12e6869176e0a8642a3276c75f7ba6fd3a15df";
    ObjectMapper objectMapper = new ObjectMapper();
    UserDetails userDetails = new UserDetails();
    String responseBodyDataString;
    String PatchUserId;
    String jsonPayload;
    public Logger log  =getLogger(BaseTest.class);


    @BeforeClass
    public void createResponseSpec(){
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusLine("HTTP/1.1 200 OK").expectStatusCode(200).expectContentType(ContentType.JSON).expectResponseTime( Matchers.lessThan(700L));
        responseSpecification = responseSpecBuilder.build();
    }

}
