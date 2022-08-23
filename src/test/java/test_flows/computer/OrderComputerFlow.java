package test_flows.computer;

import models.components.cart.TotalComponent;
import models.components.order.ComputerEssentialComponent;
import models.pages.ComputerItemDetailsPage;
import models.pages.ShoppingCartPage;
import org.openqa.selenium.WebDriver;
import test_data.computer.ComputerData;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderComputerFlow<T extends ComputerEssentialComponent> {

    private final WebDriver driver;
    private final Class<T> computerEssentialComponentt;
    private final ComputerData computerData;
    private final int quantity;
    private double totalItemPrice;

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialComponent, ComputerData computerData) {
        this.driver = driver;
        this.computerEssentialComponentt = computerEssentialComponent;
        this.computerData = computerData;
        this.quantity = 1;


    }

    public OrderComputerFlow(WebDriver driver, Class<T> computerEssentialComponent, ComputerData computerData, int quantity) {
        this.driver = driver;
        this.computerEssentialComponentt = computerEssentialComponent;
        this.computerData = computerData;
        this.quantity = quantity;

    }

    public void buildCompSpecAndAddToCart() {
        ComputerItemDetailsPage computerItemDetailsPage = new ComputerItemDetailsPage(driver);
        //find component of class loại T (trong BuyingCheapComputer sẽ truyền cụ thể class CheapComputerComponent hay StandardComputerComponent)
        T computerEssentialCompp = computerItemDetailsPage.computerComp(computerEssentialComponentt);

        // Unselect all default options
        computerEssentialCompp.unselectDefaultOptions();

        String processorFullStr = computerEssentialCompp.selectProcessorType(computerData.getProcessorType());
        double processorAdditionalPrice = extractAdditionalPrice(processorFullStr);
        //System.out.println("processorAdditionalPrice: " + processorAdditionalPrice);

        String ramFullStr = computerEssentialCompp.selectRAMType(computerData.getRam());
        double ramAdditionalPrice = extractAdditionalPrice(ramFullStr);
       // System.out.println("ramFullStr: " + ramAdditionalPrice);
        String hddFullStr = computerEssentialCompp.selectHDD(computerData.getHdd());
        double additionalHddPrice = extractAdditionalPrice(hddFullStr);
      //  System.out.println("additionalHddPrice: " + additionalHddPrice);

        double additionalOsPrice = 0;
        if (computerData.getOs() != null) {
            String fullOsStr = computerEssentialCompp.selectOS(computerData.getOs());
            additionalOsPrice = extractAdditionalPrice(fullOsStr);
        }
        //System.out.println("additionalOSPrice: " + additionalOsPrice);
        String fullSoftwareStr = computerEssentialCompp.selectSoftware(computerData.getSoftware());
        double additionalSoftwarePrice = extractAdditionalPrice(fullSoftwareStr);

        // Debug only
//        try{
//            Thread.sleep(3000);
//        } catch (Exception ignored){
//
//        }
        // Calculate item's  price and add to card
        double basePrice = computerEssentialCompp.productPrice();
        double allAdditionalPrices =
                processorAdditionalPrice
                        + ramAdditionalPrice
                        + additionalHddPrice
                        + additionalOsPrice
                        + additionalSoftwarePrice;
        totalItemPrice = (basePrice + allAdditionalPrices) * quantity;

        // And add to cart
        computerEssentialCompp.clickOnAddToCartBtn();
        computerEssentialCompp.waitUntilItemAddedToCart();


//        System.out.println(processorAdditionalPrice);
//        System.out.println(ramAdditionalPrice);
//        System.out.println(additionalHddPrice);
//        System.out.println(additionalOsPrice);
//        System.out.println(additionalSoftwarePrice);
//        System.out.println(basePrice);
//        System.out.println("totalItemPrice:" + totalItemPrice);
        // Then Navigate to shopping cart
        computerItemDetailsPage.headerComp().clickOnShoppingCartLink();
        try {
            Thread.sleep(3000);
        } catch (Exception ignore) {
        }

    }

    private double extractAdditionalPrice(String itemStr) {
        double price = 0;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(itemStr);
        if (matcher.find()) {
            price = Double.parseDouble(matcher.group(1).replaceAll("[-+]", ""));

        }
        return price;
    }

    public void verifyShoppingCartPage(){
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage(driver);
        TotalComponent totalComp = shoppingCartPage.totalComp();
        Map<String, Double> priceCategories = totalComp.priceCategories();
        for (String priceType : priceCategories.keySet()) {
            System.out.println(priceType + ": " + priceCategories.get(priceType));
        }
    }
}
