package id.co.juaracoding.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LicenseGatePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By licenseInput = By.cssSelector("[data-testid='license-key-input']");
    private final By activateButton = By.cssSelector("[data-testid='license-submit-button']");

    public LicenseGatePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isDisplayed() {
        return !driver.findElements(licenseInput).isEmpty();
    }

    public void activate(String licenseKey) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(licenseInput)).sendKeys(licenseKey);
        wait.until(ExpectedConditions.elementToBeClickable(activateButton)).click();
    }
}
