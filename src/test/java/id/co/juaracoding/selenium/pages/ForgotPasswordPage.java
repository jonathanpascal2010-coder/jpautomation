package id.co.juaracoding.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotPasswordPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By emailInput = By.cssSelector("[data-testid='forgot-password-email-input']");
    private final By captchaValueHint = By.cssSelector("[data-testid='forgot-password-captcha-value']");
    private final By captchaInput = By.cssSelector("[data-testid='forgot-password-captcha-input']");
    private final By submitButton = By.cssSelector("[data-testid='forgot-password-submit-button']");
    private final By errorToastIcon = By.cssSelector("[data-testid='toast-icon-error']");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).clear();
        driver.findElement(emailInput).sendKeys(email);
    }

    public void isiCaptchaOtomatis() {
        String captchaValue = wait.until(ExpectedConditions.visibilityOfElementLocated(captchaValueHint)).getText();
        wait.until(ExpectedConditions.visibilityOfElementLocated(captchaInput)).clear();
        driver.findElement(captchaInput).sendKeys(captchaValue);
    }

    public void clickSubmit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void submit(String email) {
        fillEmail(email);
        isiCaptchaOtomatis();
        clickSubmit();
    }

    public boolean isErrorToastShown() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorToastIcon)) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
