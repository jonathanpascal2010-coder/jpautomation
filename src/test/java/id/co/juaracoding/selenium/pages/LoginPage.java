package id.co.juaracoding.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput = By.cssSelector("[data-testid='login-username-input']");
    private final By passwordInput = By.cssSelector("[data-testid='login-password-input'] input");
    private final By captchaValueHint = By.cssSelector("[data-testid='login-captcha-value']");
    private final By captchaInput = By.cssSelector("[data-testid='login-captcha-input']");
    private final By loginButton = By.cssSelector("[data-testid='login-submit-button']");
    private final By errorToastIcon = By.cssSelector("[data-testid='toast-icon-error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String readCaptchaValue() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(captchaValueHint)).getText();
    }

    public void fillUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }

    public void fillPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void fillCaptcha(String captcha) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(captchaInput)).clear();
        driver.findElement(captchaInput).sendKeys(captcha);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void loginAs(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        fillCaptcha(readCaptchaValue());
        clickLogin();
    }

    public boolean isErrorToastShown() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorToastIcon)) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
