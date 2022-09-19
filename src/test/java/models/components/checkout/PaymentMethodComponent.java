package models.components.checkout;

import models.components.Component;
import models.components.ComponentCssSelector;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@ComponentCssSelector("#opc-shipping_method")
public class ShippingMethodComponent extends Component {

    public ShippingMethodComponent(WebDriver driver, WebElement component) {
        super(driver, component);
    }
}
