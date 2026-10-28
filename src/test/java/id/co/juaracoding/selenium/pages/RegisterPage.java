package id.co.juaracoding.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput = By.cssSelector("[data-testid='register-username-input']");
    private final By passwordInput = By.cssSelector("[data-testid='register-password-input'] input");
    private final By fullNameInput = By.cssSelector("[data-testid='register-full-name-input']");
    private final By emailInput = By.cssSelector("[data-testid='register-email-input']");
    private final By phoneInput = By.cssSelector("[data-testid='register-phone-number-input']");
    private final By addressInput = By.cssSelector("[data-testid='register-address-input']");
    private final By birthDateInput = By.cssSelector("[data-testid='register-birth-date-input'] input");
    private final By genderSelect = By.cssSelector("[data-testid='register-gender-input']");
    private final By idCardInput = By.cssSelector("[data-testid='register-id-card-number-input']");
    private final By taxIdInput = By.cssSelector("[data-testid='register-tax-id-number-input']");
    private final By captchaValueHint = By.cssSelector("[data-testid='register-captcha-value']");
    private final By captchaInput = By.cssSelector("[data-testid='register-captcha-input']");
    private final By submitButton = By.cssSelector("[data-testid='register-submit-button']");
    private final By chooseYearButton = By.cssSelector(".p-datepicker-select-year");
    private final By errorToastIcon = By.cssSelector("[data-testid='toast-icon-error']");
    private final By validationError = By.cssSelector(".p-error, .p-invalid");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillWajibFields(String username, String password, String fullName, String email,
                                String phone, String address, String idCardNumber, String taxIdNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).clear();
        driver.findElement(usernameInput).sendKeys(username);

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).clear();
        driver.findElement(passwordInput).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput)).clear();
        driver.findElement(fullNameInput).sendKeys(fullName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).clear();
        driver.findElement(emailInput).sendKeys(email);

        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).clear();
        driver.findElement(phoneInput).sendKeys(phone);

        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).clear();
        driver.findElement(addressInput).sendKeys(address);

        wait.until(ExpectedConditions.visibilityOfElementLocated(idCardInput)).clear();
        driver.findElement(idCardInput).sendKeys(idCardNumber);

        wait.until(ExpectedConditions.visibilityOfElementLocated(taxIdInput)).clear();
        driver.findElement(taxIdInput).sendKeys(taxIdNumber);
    }

    public void pilihJenisKelamin(String label) {
        wait.until(ExpectedConditions.elementToBeClickable(genderSelect)).click();
        By option = By.xpath("//ul[@id='reg-gender_list']//li[@aria-label='" + label + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void pilihTanggalLahir2020Januari15() {
        wait.until(ExpectedConditions.elementToBeClickable(birthDateInput)).click();
        wait.until(ExpectedConditions.elementToBeClickable(chooseYearButton)).click();
        By year2020 = By.xpath("//span[contains(@class,'p-datepicker-year') and normalize-space(text())='2020']");
        wait.until(ExpectedConditions.elementToBeClickable(year2020)).click();
        By monthJan = By.xpath("//span[contains(@class,'p-datepicker-month') and normalize-space(text())='Jan']");
        wait.until(ExpectedConditions.elementToBeClickable(monthJan)).click();
        By day15 = By.xpath("//td[@aria-label='15']");
        wait.until(ExpectedConditions.elementToBeClickable(day15)).click();
    }

    public void isiCaptchaOtomatis() {
        String captchaValue = wait.until(ExpectedConditions.visibilityOfElementLocated(captchaValueHint)).getText();
        wait.until(ExpectedConditions.visibilityOfElementLocated(captchaInput)).clear();
        driver.findElement(captchaInput).sendKeys(captchaValue);
    }

    public void submit() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public boolean isErrorToastShown() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorToastIcon)) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasValidationErrors() {
        return !driver.findElements(validationError).isEmpty();
    }
}
