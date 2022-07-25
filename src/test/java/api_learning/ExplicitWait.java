package api_learning;

import driver.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import support.ui.WaitMoreThanOneTab;
import url.Urls;

import java.time.Duration;

public class ExplicitWait {

    public static void main(String[] args) {
        WebDriver driver = DriverFactory.getChromeDriver();

        try{
            driver.get(Urls.baseUrl.concat(Urls.loginSlug));
            By taolaoSel = By.cssSelector("#teo");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            //wait.until(ExpectedConditions.visibilityOfElementLocated(taolaoSel));
           // wait.until(ExpectedConditions.visibilityOf(driver.findElement(taolaoSel)));


            wait.until(new WaitMoreThanOneTab());
        // customize  wait untill tabs >1

        }catch (Exception e){
            e.printStackTrace();

        }

        driver.quit();
    }

}
