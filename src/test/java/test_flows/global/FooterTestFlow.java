package test_flows.global;


import models.components.global.TopMenuComponent;
import static models.components.global.TopMenuComponent.MainCatItem;
import static models.components.global.TopMenuComponent.SublistComponent;
import models.components.global.footer.FooterColumnComponent;
import models.components.global.footer.FooterComponent;
import models.pages.BasePage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import url.Urls;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FooterTestFlow {

    private final WebDriver driver;

    public FooterTestFlow(WebDriver driver) {
        this.driver = driver;
    }

    public void verifyFooterComponent(){
        BasePage basePage = new BasePage(driver);
        FooterComponent footerComp = basePage.footerComp();
        verifyInformationColumn(footerComp.informationColumnComp());
        verifyCustomerService(footerComp.customerServiceColumnComp());
//        verifyAccountColumn(footerComp.accountColumnComp());
//        verifyFollowUsColumn(footerComp.followUsColumnComp());
    }

    private void verifyInformationColumn(FooterColumnComponent footerColumnComp) {
        String baseUrl = Urls.demoBaseUrl;
        List<String> expectedLinkTexts = Arrays.asList("Sitemap", "Shipping & Returns", "Privacy Notice", "Conditions of Use",
                "About us", "Contact us");
        List<String> expectedHrefs = Arrays.asList(
                baseUrl + "/sitemap", baseUrl + "/shipping-returns", baseUrl + "/privacy-policy", baseUrl + "/conditions-of-use",
                baseUrl + "/about-us", baseUrl + "/contactus");
        verifyFooterColumn(footerColumnComp, expectedLinkTexts,expectedHrefs);

    }

    private void verifyCustomerService(FooterColumnComponent footerColumnComp) {
        String baseUrl = Urls.demoBaseUrl;
        List<String> expectedLinkTexts = Arrays.asList(
                "Search", "News", "Blog", "Recently viewed products",
                "Compare products list", "New products");
        List<String> expectedHrefs = Arrays.asList(
                baseUrl + "/search", baseUrl + "/news", baseUrl + "/blog", baseUrl + "/recentlyviewedproducts",
                baseUrl + "/compareproducts", baseUrl + "/newproducts");
        verifyFooterColumn(footerColumnComp, expectedLinkTexts, expectedHrefs);
    }

    private void verifyAccountColumn(FooterColumnComponent footerColumnComp) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        verifyFooterColumn(footerColumnComp, expectedLinkTexts,expectedHrefs);
    }

    private void verifyFollowUsColumn(FooterColumnComponent footerColumnComp) {
        List<String> expectedLinkTexts = new ArrayList<>();
        List<String> expectedHrefs = new ArrayList<>();
        verifyFooterColumn(footerColumnComp, expectedLinkTexts,expectedHrefs);
    }

    public void verifyProductCatFooterComponent(){
        // Random pickup an Item
        BasePage basePage = new BasePage(driver);
        TopMenuComponent topMenuComp = basePage.topMenuComp();
        // danh sách các main item như book, computer...
        List<MainCatItem> mainCatsElem = topMenuComp.mainItemsElem();
        if(mainCatsElem.isEmpty()) {
            Assert.fail("[ERR] There is no item on top menu!");
        }
        //random những main item như book, computer...
        MainCatItem randomMainItemElem = mainCatsElem.get(new SecureRandom().nextInt(mainCatsElem.size()));
        // hard code de test item tai index1
        // randomMainItemElem = mainCatsElem.get(1);
        String randomCatHref = randomMainItemElem.catItemLinkElem().getAttribute("href");

        // Get sublist, danh sách các items con nằm trong book, computer...
        List<SublistComponent> sublistComponents = randomMainItemElem.sublistComps();
        if(sublistComponents.isEmpty()){

            randomMainItemElem.catItemLinkElem().click();
        }
        else {
            int randomIndex = new SecureRandom().nextInt(sublistComponents.size());
            SublistComponent randomCatItemComp = sublistComponents.get(randomIndex);
            randomCatHref = randomCatItemComp.getComponent().getAttribute("href");
            randomCatItemComp.getComponent().click();
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.urlContains(randomCatHref));
        } catch (TimeoutException e) {
            Assert.fail("[ERR] target page is not matched!");
        }
        // verify footer component
        verifyFooterComponent();
    }

    private void verifyFooterColumn(
            FooterColumnComponent footerColumnComponent, List<String> expectedLinkTexts, List<String> expectedHrefs) {

        List<String> actualLinkTexts = new ArrayList<>();
        List<String> actualHrefs = new ArrayList<>();

        for(WebElement link : footerColumnComponent.linksElem()){
            actualLinkTexts.add(link.getText().trim());
            actualHrefs.add(link.getAttribute("href"));
        }
        if(actualLinkTexts.isEmpty() || actualHrefs.isEmpty()){
            Assert.fail("[ERR] Texts or hyperlinks is empty in footer column");
        }

       // actualLinkTexts.equals(actualHrefs);

        // verify link text
        Assert.assertEquals(actualLinkTexts, expectedLinkTexts, "[ERR] Actual and expected link texts are different!");

        // verify hrefs
        Assert.assertEquals(actualHrefs, expectedHrefs, "[ERR] Actual and expected hrefs are different!");


    }

}