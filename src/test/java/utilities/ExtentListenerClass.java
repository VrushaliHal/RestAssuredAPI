package utilities;

import TestCases.BaseTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Date;


public class ExtentListenerClass  extends BaseTest implements ITestListener {


    public  ExtentSparkReporter extentSparkReporter; // UI -Look and feel
    public  ExtentReports extent; // populate common info in the report
    public  ExtentTest test; // creating Test case entries  and update the status of report


    public void onStart(ITestContext context) {
        // not implemented
        Date now = new Date();
        String timeStamp = now.toString().replace(":","-");
        extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"//Reports//"+timeStamp+".html");
        extentSparkReporter.config().setDocumentTitle("API Automation Suite");
        extentSparkReporter.config().setReportName("Functional Testing");
        extentSparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(extentSparkReporter);


        extent.setSystemInfo("User",System.getProperty("user.name"));
        extent.setSystemInfo("JAVA version",System.getProperty("java.version"));
        extent.setSystemInfo("JAVA vm Name",System.getProperty("java.vm.name"));
        extent.setSystemInfo("JAVA Runtime",System.getProperty("java.runtime.name"));
        extent.setSystemInfo("OS Version",System.getProperty("os.version"));

    }

    public void onTestSuccess(ITestResult result) {
        test  =extent.createTest(result.getName()); //create new entry in report
        test.log(Status.PASS,"Test Case PASSED is : "+result.getName());
    }
    public void onTestFailure(ITestResult result) {
        test  =extent.createTest(result.getName());
        test.log(Status.FAIL,"Test Case FAILED is : "+result.getName());
        test.log(Status.FAIL,"Failure cause is : "+result.getThrowable());
    }
    public void onTestSkipped(ITestResult result) {
        test  =extent.createTest(result.getName());
        test.log(Status.FAIL,"Test Case SKIPPED is : "+result.getName());
    }
    public void onFinish(ITestContext context) {
        extent.flush();
    }





}
