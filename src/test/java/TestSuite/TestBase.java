package TestSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import utilities.ConfigurationReader;

import java.io.IOException;

import static io.restassured.RestAssured.baseURI;

public class TestBase {
    //this class is used for starting nad building reports
    protected ExtentReports report;
    //this class is used to create HTML report file
    protected ExtentHtmlReporter htmlReporter;
    //this will  define a test, enables adding logs, authors, test steps
    protected ExtentTest extentLogger;

    @BeforeTest
    public void setup() {
        baseURI = ConfigurationReader.get("ssdAPI_url");

        //create reporting structure
        report = new ExtentReports();
        String projectPath = System.getProperty("user.dir");
        String path = projectPath + "/test-output/report.html";
        htmlReporter = new ExtentHtmlReporter(path);
        report.attachReporter(htmlReporter);
        htmlReporter.config().setReportName("Close Approach Objects API Test Suite");
        report.setSystemInfo("Environment", "SBDB Close-Approach Data API");
        report.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) throws InterruptedException, IOException {
        //if test fails
        if (result.getStatus() == ITestResult.FAILURE) {
            //record the name of failed test case
            extentLogger.fail(result.getName());
            //capture the exception and put inside the report
            extentLogger.fail(result.getThrowable());
        }

    }
    @AfterTest
    public void tearDown(){
        //this is step the report is actually created
        report.flush();
    }
}