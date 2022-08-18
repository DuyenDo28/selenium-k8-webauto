package test;

import driver.DriverFactory;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BaseTest {

    protected static WebDriver driver;

    @BeforeTest
    public void initBrowserSession(){
        driver = DriverFactory.getChromeDriver();

    }

    @AfterTest(alwaysRun = true)
    public void closeBrowserSession(){
        if(driver!=null) driver.quit();
    }

    @AfterMethod
    public void captureScreenshot(ITestResult result){
        if(result.getStatus()==ITestResult.FAILURE){
            // testMethodName-yyyy-mm-dd-hr-mm=sec.png

            // 1. Get method name
            String methodName = result.getName();
            // 2. Get Taken time
            Calendar calendar = new GregorianCalendar();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DATE);
            int hr = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            String filename = methodName + "_" + y + "_" + m + "_" + d + "_" + hr
                    + "_" + min + "_" + sec + ".png";



            // 3. Take Screenshot

            File screenshotBase64Data = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // 4. Save and attach to allure reporter
            try{
                String filelocation = System.getProperty("user.dir") + "/screenshots/" + filename;
                FileUtils.copyFile(screenshotBase64Data, new File(filelocation));

                // 5. Attach to report
                Path content = Paths.get(filelocation);
                try (InputStream inputStream = Files.newInputStream(content)) {
                    Allure.addAttachment(methodName, inputStream);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}