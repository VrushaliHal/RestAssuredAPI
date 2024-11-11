package TestCases;

import com.aventstack.extentreports.ExtentReports;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.specification.RequestSpecification;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pojo_class.UserDetails;
import utilities.ExtentListenerClass;

import static org.apache.logging.log4j.LogManager.getLogger;


public class BaseTest {


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



}
